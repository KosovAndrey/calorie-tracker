package com.kosovandrey.calorietracker.domain.meal.model;

import com.kosovandrey.calorietracker.domain.meal_item.model.MealItemResponse;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Модель ответа приема пищи
 */
public record MealResponse(
    Long id,
    Long userId,
    LocalDateTime dateTime,
    String mealType,
    double totalCalories,
    double totalProteins,
    double totalFats,
    double totalCarbohydrates,
    List<MealItemResponse> items
) {

}

