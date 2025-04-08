package com.kosovandrey.calorietracker.domain.meal_item.exception;

import com.kosovandrey.calorietracker.domain.common.exception.ResourceNotFoundException;

/**
 * Исключение, возникающее при отсутствии приема пищи
 */
public class MealItemNotFoundException extends ResourceNotFoundException {
    
    public MealItemNotFoundException(Long id) {
        super("Прием пищи", id);
    }
} 