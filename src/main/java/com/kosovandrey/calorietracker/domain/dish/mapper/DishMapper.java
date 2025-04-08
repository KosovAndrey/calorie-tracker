package com.kosovandrey.calorietracker.domain.dish.mapper;

import com.kosovandrey.calorietracker.domain.dish.entity.DishEntity;
import com.kosovandrey.calorietracker.domain.dish.model.DishRequest;
import com.kosovandrey.calorietracker.domain.dish.model.DishResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

/**
 * Маппер для преобразования между сущностями и моделями блюд
 */
@Component
public class DishMapper {

    public DishEntity toDish(final @Valid DishRequest request) {
        return DishEntity.builder()
                .id(request.id())
                .name(request.name())
                .calories(request.calories())
                .proteins(request.proteins())
                .fats(request.fats())
                .carbohydrates(request.carbohydrates())
                .build();
    }

    public DishResponse toDishResponse(final DishEntity dishEntity) {
        return new DishResponse(
                dishEntity.getId(),
                dishEntity.getName(),
                dishEntity.getCalories(),
                dishEntity.getProteins(),
                dishEntity.getFats(),
                dishEntity.getCarbohydrates()
        );
    }
} 