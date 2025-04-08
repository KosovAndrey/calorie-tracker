package com.kosovandrey.calorietracker.domain.user.model;

/**
 * Модель ответа пользователя
 */
public record UserResponse(
    Long id,
    String name,
    String email,
    Integer age,
    Double weight,
    Double height,
    UserGoal goal,
    Double dailyCalorieNorm
) {} 