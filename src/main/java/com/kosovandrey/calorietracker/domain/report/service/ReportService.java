package com.kosovandrey.calorietracker.domain.report.service;

import com.kosovandrey.calorietracker.domain.meal.entity.MealEntity;
import com.kosovandrey.calorietracker.domain.meal.repository.MealRepository;
import com.kosovandrey.calorietracker.domain.report.model.CalorieLimitResponse;
import com.kosovandrey.calorietracker.domain.report.model.DailyReportResponse;
import com.kosovandrey.calorietracker.domain.report.model.NutritionHistoryResponse;
import com.kosovandrey.calorietracker.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.stream.Collectors;

/**
 * Сервис для работы с отчетами о питании
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final MealRepository mealRepository;
    private final UserService userService;

    public DailyReportResponse getDailyReport(LocalDate date, Long userId) {
        var startOfDay = date.atStartOfDay();
        var endOfDay = date.plusDays(1).atStartOfDay();
        
        var meals = mealRepository.findByDateTimeBetweenAndUserId(startOfDay, endOfDay, userId);
        
        var totalCalories = meals.stream()
                .mapToDouble(MealEntity::getTotalCalories)
                .sum();
                
        var totalProteins = meals.stream()
                .mapToDouble(MealEntity::getTotalProteins)
                .sum();
                
        var totalFats = meals.stream()
                .mapToDouble(MealEntity::getTotalFats)
                .sum();
                
        var totalCarbohydrates = meals.stream()
                .mapToDouble(MealEntity::getTotalCarbohydrates)
                .sum();
                
        var mealReports = meals.stream()
                .map(meal -> new DailyReportResponse.MealReport(
                        meal.getMealType().name(),
                        meal.getTotalCalories(),
                        meal.getTotalProteins(),
                        meal.getTotalFats(),
                        meal.getTotalCarbohydrates()
                ))
                .collect(Collectors.toList());
                
        return new DailyReportResponse(
                date,
                totalCalories,
                totalProteins,
                totalFats,
                totalCarbohydrates,
                mealReports
        );
    }

    public CalorieLimitResponse checkCalorieLimit(LocalDate date, Long userId) {
        var user = userService.findEntityById(userId);
        var dailyLimit = user.getDailyCalorieNorm();
        
        var startOfDay = date.atStartOfDay();
        var endOfDay = date.plusDays(1).atStartOfDay();
        
        var consumedCalories = mealRepository.findByDateTimeBetweenAndUserId(startOfDay, endOfDay, userId)
                .stream()
                .mapToDouble(MealEntity::getTotalCalories)
                .sum();
                
        var remainingCalories = dailyLimit - consumedCalories;
        
        return new CalorieLimitResponse(
                dailyLimit,
                consumedCalories,
                remainingCalories,
                consumedCalories > dailyLimit
        );
    }

    public NutritionHistoryResponse getNutritionHistory(LocalDate startDate, LocalDate endDate, Long userId) {
        var startDateTime = startDate.atStartOfDay();
        var endDateTime = endDate.plusDays(1).atStartOfDay();
        
        var meals = mealRepository.findByDateTimeBetweenAndUserId(startDateTime, endDateTime, userId);
        
        var dailySummaries = meals.stream()
                .collect(Collectors.groupingBy(
                        meal -> meal.getDateTime().toLocalDate(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                dayMeals -> {
                                    var totalCalories = dayMeals.stream()
                                            .mapToDouble(MealEntity::getTotalCalories)
                                            .sum();
                                            
                                    var totalProteins = dayMeals.stream()
                                            .mapToDouble(MealEntity::getTotalProteins)
                                            .sum();
                                            
                                    var totalFats = dayMeals.stream()
                                            .mapToDouble(MealEntity::getTotalFats)
                                            .sum();
                                            
                                    var totalCarbohydrates = dayMeals.stream()
                                            .mapToDouble(MealEntity::getTotalCarbohydrates)
                                            .sum();
                                            
                                    return new NutritionHistoryResponse.DailySummary(
                                            dayMeals.get(0).getDateTime().toLocalDate(),
                                            totalCalories,
                                            totalProteins,
                                            totalFats,
                                            totalCarbohydrates,
                                            dayMeals.size()
                                    );
                                }
                        )
                ))
                .values()
                .stream()
                .sorted((a, b) -> b.date().compareTo(a.date()))
                .collect(Collectors.toList());
                
        return new NutritionHistoryResponse(dailySummaries);
    }
} 