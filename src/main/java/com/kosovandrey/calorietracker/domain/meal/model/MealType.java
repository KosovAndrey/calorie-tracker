package com.kosovandrey.calorietracker.domain.meal.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Тип приема пищи
 */
@RequiredArgsConstructor
@Getter

public enum MealType {
    BREAKFAST(1),
    LUNCH(2),
    DINNER(3),
    SNACK(4);

    private final int value;

    public static MealType fromValue(int value) {
        for (MealType type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Неизвестный тип приема пищи: " + value);
    }
} 