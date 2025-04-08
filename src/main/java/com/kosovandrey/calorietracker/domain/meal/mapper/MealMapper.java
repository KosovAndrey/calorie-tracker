package com.kosovandrey.calorietracker.domain.meal.mapper;

import com.kosovandrey.calorietracker.domain.meal.entity.MealEntity;
import com.kosovandrey.calorietracker.domain.meal.model.MealRequest;
import com.kosovandrey.calorietracker.domain.meal.model.MealResponse;
import com.kosovandrey.calorietracker.domain.meal.model.MealType;
import com.kosovandrey.calorietracker.domain.meal_item.entity.MealItemEntity;
import com.kosovandrey.calorietracker.domain.meal_item.mapper.MealItemMapper;
import com.kosovandrey.calorietracker.domain.meal_item.model.MealItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Маппер для преобразования между сущностями и моделями приемов пищи
 */
@Component
@RequiredArgsConstructor
public class MealMapper {

    private final MealItemMapper mealItemMapper;

    public MealEntity toEntity(MealRequest request) {
        return MealEntity.builder()
                .dateTime(request.dateTime())
                .mealType(MealType.fromValue(request.mealType()))
                .totalCalories(0.0)
                .totalProteins(0.0)
                .totalFats(0.0)
                .totalCarbohydrates(0.0)
                .build();
    }

    public MealResponse toResponse(MealEntity entity, List<MealItemEntity> items) {
        List<MealItemResponse> itemResponses = items.stream()
                .map(mealItemMapper::toResponse)
                .collect(Collectors.toList());

        return new MealResponse(
                entity.getId(),
                entity.getUser().getId(),
                entity.getDateTime(),
                entity.getMealType().name(),
                entity.getTotalCalories(),
                entity.getTotalProteins(),
                entity.getTotalFats(),
                entity.getTotalCarbohydrates(),
                itemResponses
        );
    }
}