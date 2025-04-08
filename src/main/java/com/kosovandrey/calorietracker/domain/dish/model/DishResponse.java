package com.kosovandrey.calorietracker.domain.dish.model;

/**
 * Модель ответа блюда
 */
public record DishResponse(
        Long id,
        String name,
        int calories,
        double proteins,
        double fats,
        double carbohydrates
) {}