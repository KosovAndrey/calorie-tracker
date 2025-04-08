package com.kosovandrey.calorietracker.domain.meal_item.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

/**
 * Модель запроса элемента приема пищи
 */
public record MealItemRequest(
    @NotNull(message = "ID блюда не может быть пустым")
    Long dishId,

    @NotNull(message = "Порция не может быть пустой")
    @DecimalMin(value = "0.1", message = "Порция должна быть больше 0")
    Double portion
) {} 