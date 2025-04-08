package com.kosovandrey.calorietracker.domain.meal.model;

import com.kosovandrey.calorietracker.domain.meal_item.model.MealItemRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Модель запроса приема пищи
 */
public record MealRequest(
    @NotNull(message = "ID пользователя не может быть пустым")
    Long userId,

    @NotNull(message = "Дата и время не могут быть пустыми")
    LocalDateTime dateTime,

    @NotEmpty(message = "Список блюд не может быть пустым")
    @Valid
    List<MealItemRequest> items,

    @NotNull(message = "Тип приема пищи не может быть пустым")
    Integer mealType
) {} 