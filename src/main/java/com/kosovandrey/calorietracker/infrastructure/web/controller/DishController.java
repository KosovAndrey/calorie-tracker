package com.kosovandrey.calorietracker.infrastructure.web.controller;

import com.kosovandrey.calorietracker.domain.dish.model.DishRequest;
import com.kosovandrey.calorietracker.domain.dish.model.DishResponse;
import com.kosovandrey.calorietracker.domain.dish.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для работы с блюдами
 */
@RestController
@RequestMapping("/api/v1/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    @GetMapping("/{id}")
    public ResponseEntity<DishResponse> getDish(
            final @PathVariable Long id) {
        return ResponseEntity.ok(dishService.getDish(id));
    }

    @PostMapping
    public ResponseEntity<DishResponse> createDish(
            final @RequestBody DishRequest dish) {
        return ResponseEntity.ok(dishService.createDish(dish));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DishResponse> updateDish(
            final @PathVariable Long id,
            final @RequestBody DishRequest dish) {
        return ResponseEntity.ok(dishService.updateDish(id, dish));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(
            final @PathVariable Long id) {
        dishService.deleteDish(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<DishResponse>> searchDishes(
            final @RequestParam String query) {
        return ResponseEntity.ok(dishService.searchDishes(query));
    }
}