package com.kosovandrey.calorietracker.domain.user.model;

import jakarta.validation.constraints.*;

/**
 * Модель запроса пользователя
 */
public record UserRequest(
    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов")
    String name,

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    String email,

    @NotNull(message = "Возраст не может быть пустым")
    @Min(value = 1, message = "Возраст должен быть больше 0")
    @Max(value = 150, message = "Некорректный возраст")
    Integer age,

    @NotNull(message = "Вес не может быть пустым")
    @DecimalMin(value = "20.0", message = "Вес должен быть больше 20 кг")
    @DecimalMax(value = "300.0", message = "Вес должен быть меньше 300 кг")
    Double weight,

    @NotNull(message = "Рост не может быть пустым")
    @DecimalMin(value = "50.0", message = "Рост должен быть больше 50 см")
    @DecimalMax(value = "250.0", message = "Рост должен быть меньше 250 см")
    Double height,

    @NotNull(message = "Цель не может быть пустой")
    UserGoal goal
) {} 