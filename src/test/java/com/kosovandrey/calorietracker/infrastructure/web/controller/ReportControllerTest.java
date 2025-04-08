package com.kosovandrey.calorietracker.infrastructure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kosovandrey.calorietracker.domain.report.model.CalorieLimitResponse;
import com.kosovandrey.calorietracker.domain.report.model.DailyReportResponse;
import com.kosovandrey.calorietracker.domain.report.model.NutritionHistoryResponse;
import com.kosovandrey.calorietracker.domain.report.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Report Controller Tests")
class ReportControllerTest {

    @Mock
    private ReportService reportService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReportController controller = new ReportController(reportService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Nested
    @DisplayName("Daily Report Tests")
    class DailyReportTests {

        @Test
        @DisplayName("should return daily report when valid date and userId provided")
        void shouldReturnDailyReport() throws Exception {
            LocalDate date = LocalDate.now();
            Long userId = 1L;
            DailyReportResponse response = new DailyReportResponse(
                date,
                2000.0,
                150.0,
                50.0,
                200.0,
                Collections.emptyList()
            );

            when(reportService.getDailyReport(date, userId)).thenReturn(response);

            mockMvc.perform(get("/api/v1/reports/daily")
                    .param("date", date.toString())
                    .param("userId", userId.toString())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.date").value(date.toString()))
                    .andExpect(jsonPath("$.totalCalories").value(2000.0))
                    .andExpect(jsonPath("$.totalProteins").value(150.0))
                    .andExpect(jsonPath("$.totalFats").value(50.0))
                    .andExpect(jsonPath("$.totalCarbohydrates").value(200.0));
        }
    }

    @Nested
    @DisplayName("Calorie Limit Tests")
    class CalorieLimitTests {

        @Test
        @DisplayName("should return calorie limit information when valid date and userId provided")
        void shouldReturnCalorieLimit() throws Exception {
            LocalDate date = LocalDate.now();
            Long userId = 1L;
            CalorieLimitResponse response = new CalorieLimitResponse(
                2000.0,
                1800.0,
                200.0,
                false
            );

            when(reportService.checkCalorieLimit(date, userId)).thenReturn(response);

            mockMvc.perform(get("/api/v1/reports/calorie-limit")
                    .param("date", date.toString())
                    .param("userId", userId.toString())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.dailyLimit").value(2000.0))
                    .andExpect(jsonPath("$.consumedCalories").value(1800.0))
                    .andExpect(jsonPath("$.remainingCalories").value(200.0))
                    .andExpect(jsonPath("$.isLimitExceeded").value(false));
        }
    }

    @Nested
    @DisplayName("Nutrition History Tests")
    class NutritionHistoryTests {

        @Test
        @DisplayName("should return nutrition history when valid date range and userId provided")
        void shouldReturnNutritionHistory() throws Exception {
            LocalDate startDate = LocalDate.now().minusDays(7);
            LocalDate endDate = LocalDate.now();
            Long userId = 1L;
            NutritionHistoryResponse response = new NutritionHistoryResponse(
                List.of(
                    new NutritionHistoryResponse.DailySummary(
                        endDate,
                        2000.0,
                        150.0,
                        50.0,
                        200.0,
                        3
                    )
                )
            );

            when(reportService.getNutritionHistory(startDate, endDate, userId)).thenReturn(response);

            mockMvc.perform(get("/api/v1/reports/history")
                    .param("startDate", startDate.toString())
                    .param("endDate", endDate.toString())
                    .param("userId", userId.toString())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.dailySummaries[0].date").value(endDate.toString()))
                    .andExpect(jsonPath("$.dailySummaries[0].totalCalories").value(2000.0))
                    .andExpect(jsonPath("$.dailySummaries[0].totalProteins").value(150.0))
                    .andExpect(jsonPath("$.dailySummaries[0].totalFats").value(50.0))
                    .andExpect(jsonPath("$.dailySummaries[0].totalCarbohydrates").value(200.0))
                    .andExpect(jsonPath("$.dailySummaries[0].mealsCount").value(3));
        }
    }
} 