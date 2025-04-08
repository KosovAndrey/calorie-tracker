package com.kosovandrey.calorietracker.domain.report.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

/**
 * Ответ с историей питания по дням
 */
public record NutritionHistoryResponse(
        List<DailySummary> dailySummaries
) {
    /**
     * Сводная информация о питании за день
     */
    public record DailySummary(
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
            LocalDate date,
            double totalCalories,
            double totalProteins,
            double totalFats,
            double totalCarbohydrates,
            int mealsCount
    ) {}
} 