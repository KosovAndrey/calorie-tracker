package com.kosovandrey.calorietracker.domain.meal.service;


import com.kosovandrey.calorietracker.domain.dish.entity.DishEntity;
import com.kosovandrey.calorietracker.domain.dish.service.DishService;
import com.kosovandrey.calorietracker.domain.meal.entity.MealEntity;
import com.kosovandrey.calorietracker.domain.meal.exception.MealNotFoundException;
import com.kosovandrey.calorietracker.domain.meal.mapper.MealMapper;
import com.kosovandrey.calorietracker.domain.meal.model.MealRequest;
import com.kosovandrey.calorietracker.domain.meal.model.MealResponse;
import com.kosovandrey.calorietracker.domain.meal.model.MealType;
import com.kosovandrey.calorietracker.domain.meal.repository.MealRepository;
import com.kosovandrey.calorietracker.domain.meal_item.entity.MealItemEntity;
import com.kosovandrey.calorietracker.domain.meal_item.mapper.MealItemMapper;
import com.kosovandrey.calorietracker.domain.meal_item.model.MealItemRequest;
import com.kosovandrey.calorietracker.domain.meal_item.model.MealItemResponse;
import com.kosovandrey.calorietracker.domain.meal_item.repository.MealItemRepository;
import com.kosovandrey.calorietracker.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с приемами пищи
 * Предоставляет методы для создания, чтения, обновления и удаления приемов пищи,
 * а также управления их элементами
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MealService {

    private final MealRepository mealRepository;
    private final MealItemRepository mealItemRepository;
    private final DishService dishService;
    private final MealMapper mealMapper;
    private final MealItemMapper mealItemMapper;
    private final UserService userService;

    public MealResponse getMeal(final Long id) {
        return mealRepository.findById(id)
                .map(this::enrichWithMealItems)
                .orElseThrow(() -> new MealNotFoundException(id));
    }

    public List<MealResponse> getMealsByDate(final LocalDate date, final Long userId) {
        return mealRepository.findByDateTimeBetweenAndUserId(
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay(),
                userId
        )
                .stream()
                .map(this::enrichWithMealItems)
                .collect(Collectors.toList());
    }

    @Transactional
    public MealResponse createMeal(final MealRequest request) {
        MealEntity mealEntity = mealMapper.toEntity(request);
        var user = userService.findEntityById(request.userId());
        mealEntity.setUser(user);

        double totalCalories = 0.0;
        double totalProteins = 0.0;
        double totalCarbs = 0.0;
        double totalFats = 0.0;
        List<MealItemEntity> items = new ArrayList<>();
        
        if (request.items() != null) {
            for (MealItemRequest mealItemRequest : request.items()) {
                MealItemEntity item = mealItemMapper.toEntity(mealItemRequest, mealEntity);

                var dish = dishService.getDishEntity(mealItemRequest.dishId());
                item.setDish(dish);

                calculateCalories(mealItemRequest, item, dish);
                items.add(item);
                totalCalories += item.getCalories();
                totalProteins += item.getProteins();
                totalCarbs += item.getCarbohydrates();
                totalFats += item.getFats();
            }
        }

        mealEntity.setTotalCalories(totalCalories);
        mealEntity.setTotalProteins(totalProteins);
        mealEntity.setTotalCarbohydrates(totalCarbs);
        mealEntity.setTotalFats(totalFats);
        mealEntity = mealRepository.save(mealEntity);

        for (MealItemEntity item : items) {
            mealItemRepository.save(item);
        }

        return enrichWithMealItems(mealEntity);
    }

    private void calculateCalories(MealItemRequest mealItemRequest, MealItemEntity item, DishEntity dish) {
        double portionMultiplier = mealItemRequest.portion() / 100.0;
        item.setCalories(dish.getCalories() * portionMultiplier);
        item.setProteins(dish.getProteins() * portionMultiplier);
        item.setFats(dish.getFats() * portionMultiplier);
        item.setCarbohydrates(dish.getCarbohydrates() * portionMultiplier);
    }

    @Transactional
    public MealResponse updateMeal(final Long id, final MealRequest request) {

        var existingMeal = mealRepository.findById(id)
                .orElseThrow(() -> new MealNotFoundException(id));

        mealItemRepository.deleteByMealId(existingMeal.getId());

        existingMeal.setDateTime(request.dateTime());
        existingMeal.setMealType(MealType.fromValue(request.mealType()));

        existingMeal.getItems().clear();

        double totalCalories = 0.0;
        double totalProteins = 0.0;
        double totalFats = 0.0;
        double totalCarbohydrates = 0.0;

        existingMeal.setItems(new ArrayList<>());

        if (request.items() != null) {
            for (var mealItemRequest : request.items()) {
                MealItemEntity item = mealItemMapper.toEntity(mealItemRequest, existingMeal);

                var dish = dishService.getDishEntity(mealItemRequest.dishId());
                item.setDish(dish);

                calculateCalories(mealItemRequest, item, dish);

                existingMeal.getItems().add(item);

                totalCalories += item.getCalories();
                totalProteins += item.getProteins();
                totalFats += item.getFats();
                totalCarbohydrates += item.getCarbohydrates();
            }
        }

        existingMeal.setTotalCalories(totalCalories);
        existingMeal.setTotalProteins(totalProteins);
        existingMeal.setTotalFats(totalFats);
        existingMeal.setTotalCarbohydrates(totalCarbohydrates);

        existingMeal = mealRepository.save(existingMeal);

        return enrichWithMealItems(existingMeal);
    }

    @Transactional
    public void deleteMeal(final Long id) {
        if (!mealRepository.existsById(id)) {
            throw new MealNotFoundException(id);
        }
        mealItemRepository.deleteByMealId(id);
        mealRepository.deleteById(id);
    }

    @Transactional
    public MealItemResponse createMealItem(final Long mealId, final MealItemRequest request) {
        var dish = dishService.getDishEntity(request.dishId());
        var meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new MealNotFoundException(mealId));
        
        var mealItemEntity = mealItemMapper.toEntity(request, meal);
        mealItemEntity.setDish(dish);
        
        calculateCalories(request, mealItemEntity, dish);
        
        mealItemEntity = mealItemRepository.save(mealItemEntity);
        
        return mealItemMapper.toResponse(mealItemEntity);
    }

    private MealResponse enrichWithMealItems(final MealEntity meal) {
        var mealItems = mealItemRepository.findByMealId(meal.getId());
        return mealMapper.toResponse(meal, mealItems);
    }
}