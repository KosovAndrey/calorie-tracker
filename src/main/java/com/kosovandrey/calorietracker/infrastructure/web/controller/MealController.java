package com.kosovandrey.calorietracker.infrastructure.web.controller;

import com.kosovandrey.calorietracker.domain.meal.model.MealRequest;
import com.kosovandrey.calorietracker.domain.meal.model.MealResponse;
import com.kosovandrey.calorietracker.domain.meal.service.MealService;
import com.kosovandrey.calorietracker.domain.meal_item.model.MealItemRequest;
import com.kosovandrey.calorietracker.domain.meal_item.model.MealItemResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Контроллер для работы с приемами пищи
 */
@RestController
@RequestMapping("/api/v1/meals")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;

    @GetMapping("/{id}")
    public MealResponse getMeal(@PathVariable Long id) {
        return mealService.getMeal(id);
    }

    @GetMapping("/by-date")
    public List<MealResponse> getMealsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam Long userId) {
        return mealService.getMealsByDate(date, userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MealResponse createMeal(@RequestBody @Valid MealRequest request) {
        return mealService.createMeal(request);
    }

    @PutMapping("/{id}")
    public MealResponse updateMeal(
            @PathVariable Long id,
            @RequestBody @Valid MealRequest request) {
        return mealService.updateMeal(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMeal(@PathVariable Long id) {
        mealService.deleteMeal(id);
    }

    @PostMapping("/{mealId}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public MealItemResponse createMealItem(
            @PathVariable Long mealId,
            @RequestBody @Valid MealItemRequest request) {
        return mealService.createMealItem(mealId, request);
    }
} 