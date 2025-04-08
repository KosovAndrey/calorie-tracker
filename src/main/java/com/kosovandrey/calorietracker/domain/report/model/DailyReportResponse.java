package com.kosovandrey.calorietracker.domain.report.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

/**
 * Ответ с ежедневным отчетом о питании
 */
public record DailyReportResponse(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate date,
        double totalCalories,
        double totalProteins,
        double totalFats,
        double totalCarbohydrates,
        List<MealReport> meals
) {
    /**
     * Информация о приеме пищи в отчете
     */
    public record MealReport(
            String mealType,
            double calories,
            double proteins,
            double fats,
            double carbohydrates
    ) {}
} 