package com.kosovandrey.calorietracker.domain.report.model;

/**
 * Ответ с информацией о соблюдении дневной нормы калорий
 */
public record CalorieLimitResponse(
        double dailyLimit,
        double consumedCalories,
        double remainingCalories,
        boolean isLimitExceeded
) {} 