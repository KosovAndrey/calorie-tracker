package com.kosovandrey.calorietracker.domain.meal.exception;

import com.kosovandrey.calorietracker.domain.common.exception.ResourceNotFoundException;

/**
 * Исключение, возникающее при отсутствии приема пищи
 */
public class MealNotFoundException extends ResourceNotFoundException {
    
    public MealNotFoundException(Long id) {
        super("Прием пищи", id);
    }
} 