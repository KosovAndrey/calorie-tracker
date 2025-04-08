package com.kosovandrey.calorietracker.domain.meal_item.mapper;

import com.kosovandrey.calorietracker.domain.meal.entity.MealEntity;
import com.kosovandrey.calorietracker.domain.meal_item.entity.MealItemEntity;
import com.kosovandrey.calorietracker.domain.meal_item.model.MealItemRequest;
import com.kosovandrey.calorietracker.domain.meal_item.model.MealItemResponse;
import org.springframework.stereotype.Component;

/**
 * Маппер для преобразования между сущностями и моделями элементов приема пищи
 */
@Component
public class MealItemMapper {

    public MealItemEntity toEntity(MealItemRequest request, MealEntity meal) {
        return MealItemEntity.builder()
                .meal(meal)
                .portion(request.portion())
                .calories(0.0)
                .proteins(0.0)
                .fats(0.0)
                .carbohydrates(0.0)
                .build();
    }

    public MealItemResponse toResponse(MealItemEntity entity) {
        return new MealItemResponse(
                entity.getId(),
                entity.getMeal().getId(),
                entity.getDish().getId(),
                entity.getDish().getName(),
                entity.getPortion(),
                entity.getCalories(),
                entity.getProteins(),
                entity.getFats(),
                entity.getCarbohydrates()
        );
    }
}