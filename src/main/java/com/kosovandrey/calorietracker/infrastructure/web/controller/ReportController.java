package com.kosovandrey.calorietracker.infrastructure.web.controller;

import com.kosovandrey.calorietracker.domain.report.model.CalorieLimitResponse;
import com.kosovandrey.calorietracker.domain.report.model.DailyReportResponse;
import com.kosovandrey.calorietracker.domain.report.model.NutritionHistoryResponse;
import com.kosovandrey.calorietracker.domain.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * Контроллер для работы с отчетами о питании
 */
@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/daily")
    public ResponseEntity<DailyReportResponse> getDailyReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam Long userId) {
        return ResponseEntity.ok(reportService.getDailyReport(date, userId));
    }

    @GetMapping("/calorie-limit")
    public ResponseEntity<CalorieLimitResponse> checkCalorieLimit(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam Long userId) {
        return ResponseEntity.ok(reportService.checkCalorieLimit(date, userId));
    }

    @GetMapping("/history")
    public ResponseEntity<NutritionHistoryResponse> getNutritionHistory(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam Long userId) {
        return ResponseEntity.ok(reportService.getNutritionHistory(startDate, endDate, userId));
    }
} 