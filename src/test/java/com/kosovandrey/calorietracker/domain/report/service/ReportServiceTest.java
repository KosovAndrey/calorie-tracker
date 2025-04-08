package com.kosovandrey.calorietracker.domain.report.service;

import com.kosovandrey.calorietracker.domain.meal.entity.MealEntity;
import com.kosovandrey.calorietracker.domain.meal.model.MealType;
import com.kosovandrey.calorietracker.domain.meal.repository.MealRepository;
import com.kosovandrey.calorietracker.domain.report.model.*;
import com.kosovandrey.calorietracker.domain.user.entity.UserEntity;
import com.kosovandrey.calorietracker.domain.user.model.UserGoal;
import com.kosovandrey.calorietracker.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private MealRepository mealRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ReportService reportService;

    private UserEntity user;
    private MealEntity meal1, meal2;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(1L);
        user.setName("Иван Иванов");
        user.setEmail("ivan@example.com");
        user.setAge(30);
        user.setWeight(70.0);
        user.setHeight(175.0);
        user.setGoal(UserGoal.MAINTENANCE);
        user.setDailyCalorieNorm(2000.0);

        meal1 = new MealEntity();
        meal1.setId(1L);
        meal1.setDateTime(LocalDateTime.of(2025, 4, 1, 8, 0));
        meal1.setTotalCalories(500);
        meal1.setTotalProteins(20);
        meal1.setTotalFats(10);
        meal1.setTotalCarbohydrates(30);
        meal1.setMealType(MealType.BREAKFAST);
        meal1.setUser(user);

        meal2 = new MealEntity();
        meal2.setId(2L);
        meal2.setDateTime(LocalDateTime.of(2025, 4, 1, 12, 0));
        meal2.setTotalCalories(700);
        meal2.setTotalProteins(30);
        meal2.setTotalFats(15);
        meal2.setTotalCarbohydrates(40);
        meal2.setMealType(MealType.LUNCH);
        meal2.setUser(user);
    }

    @Nested
    @DisplayName("Tests for getDailyReport method")
    class GetDailyReportTests {

        @Test
        @DisplayName("should correctly calculate daily report")
        void shouldCorrectlyCalculateDailyReport() {
            LocalDate date = LocalDate.of(2025, 4, 1);
            when(mealRepository.findByDateTimeBetweenAndUserId(
                    LocalDateTime.of(2025, 4, 1, 0, 0),
                    LocalDateTime.of(2025, 4, 2, 0, 0),
                    1L))
                    .thenReturn(List.of(meal1, meal2));

            DailyReportResponse response = reportService.getDailyReport(date, 1L);

            assertNotNull(response);
            assertEquals(1200, response.totalCalories());
            assertEquals(50, response.totalProteins());
            assertEquals(25, response.totalFats());
            assertEquals(70, response.totalCarbohydrates());
            assertEquals(2, response.meals().size());
            verify(mealRepository, times(1)).findByDateTimeBetweenAndUserId(any(), any(), eq(1L));
        }
    }

    @Nested
    @DisplayName("Tests for checkCalorieLimit method")
    class CheckCalorieLimitTests {

        @Test
        @DisplayName("should return correct response when calorie limit is not exceeded")
        void shouldReturnCorrectResponseWhenCalorieLimitIsNotExceeded() {
            LocalDate date = LocalDate.of(2025, 4, 1);
            when(userService.findEntityById(1L)).thenReturn(user);
            when(mealRepository.findByDateTimeBetweenAndUserId(
                    LocalDateTime.of(2025, 4, 1, 0, 0),
                    LocalDateTime.of(2025, 4, 2, 0, 0),
                    1L))
                    .thenReturn(List.of(meal1));

            CalorieLimitResponse response = reportService.checkCalorieLimit(date, 1L);

            assertNotNull(response);
            assertEquals(2000, response.dailyLimit());
            assertEquals(500, response.consumedCalories());
            assertEquals(1500, response.remainingCalories());
            assertFalse(response.isLimitExceeded());
        }

        @Test
        @DisplayName("should return correct response when calorie limit is exceeded")
        void shouldReturnCorrectResponseWhenCalorieLimitIsExceeded() {
            LocalDate date = LocalDate.of(2025, 4, 1);
            when(userService.findEntityById(1L)).thenReturn(user);
            when(mealRepository.findByDateTimeBetweenAndUserId(
                    LocalDateTime.of(2025, 4, 1, 0, 0),
                    LocalDateTime.of(2025, 4, 2, 0, 0),
                    1L))
                    .thenReturn(List.of(meal1, meal2));

            CalorieLimitResponse response = reportService.checkCalorieLimit(date, 1L);

            assertNotNull(response);
            assertEquals(2000, response.dailyLimit());
            assertEquals(1200, response.consumedCalories());
            assertEquals(800, response.remainingCalories());
            assertFalse(response.isLimitExceeded());
        }
    }

    @Nested
    @DisplayName("Tests for getNutritionHistory method")
    class GetNutritionHistoryTests {

        @Test
        @DisplayName("should correctly calculate nutrition history")
        void shouldCorrectlyCalculateNutritionHistory() {
            LocalDate startDate = LocalDate.of(2025, 4, 1);
            LocalDate endDate = LocalDate.of(2025, 4, 2);
            when(mealRepository.findByDateTimeBetweenAndUserId(
                    LocalDateTime.of(2025, 4, 1, 0, 0),
                    LocalDateTime.of(2025, 4, 3, 0, 0),
                    1L))
                    .thenReturn(List.of(meal1, meal2));

            NutritionHistoryResponse response = reportService.getNutritionHistory(startDate, endDate, 1L);

            assertNotNull(response);
            assertEquals(1, response.dailySummaries().size());
            var dailySummary = response.dailySummaries().get(0);
            assertEquals(LocalDate.of(2025, 4, 1), dailySummary.date());
            assertEquals(1200, dailySummary.totalCalories());
            assertEquals(50, dailySummary.totalProteins());
            assertEquals(25, dailySummary.totalFats());
            assertEquals(70, dailySummary.totalCarbohydrates());
            assertEquals(2, dailySummary.mealsCount());
        }
    }
}