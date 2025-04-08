package com.kosovandrey.calorietracker.domain.dish.exception;

import com.kosovandrey.calorietracker.domain.common.exception.ResourceNotFoundException;

/**
 * Исключение, возникающее при отсутствии блюда
 */
public class DishNotFoundException extends ResourceNotFoundException {
    
    public DishNotFoundException(final Long id) {
        super("Блюдо", id);
    }
} 