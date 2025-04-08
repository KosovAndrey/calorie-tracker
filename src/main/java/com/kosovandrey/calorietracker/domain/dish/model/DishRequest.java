package com.kosovandrey.calorietracker.domain.dish.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Модель запроса для создания/обновления блюда
 */
public record DishRequest(
        Long id,

        @NotBlank(message = "Название блюда не может быть пустым")
        @Size(min = 2, max = 100, message = "Название блюда должно быть от 2 до 100 символов")
        String name,

        @NotNull(message = "Калории не могут быть null")
        @Min(value = 1, message = "Калории должны быть больше 0")
        int calories,

        @NotNull(message = "Белки не могут быть null")
        @Min(value = 0, message = "Белки не могут быть отрицательными")
        double proteins,

        @NotNull(message = "Жиры не могут быть null")
        @Min(value = 0, message = "Жиры не могут быть отрицательными")
        double fats,

        @NotNull(message = "Углеводы не могут быть null")
        @Min(value = 0, message = "Углеводы не могут быть отрицательными")
        double carbohydrates
) {}
