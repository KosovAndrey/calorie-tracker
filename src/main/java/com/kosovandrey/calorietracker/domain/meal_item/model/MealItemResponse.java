package com.kosovandrey.calorietracker.domain.meal_item.model;

/**
 * Модель ответа элемента приема пищи
 */
public record MealItemResponse(
    Long id,
    Long mealId,
    Long dishId,
    String dishName,
    Double portion,
    Double calories,
    Double proteins,
    Double fats,
    Double carbohydrates
) {} 