package com.kosovandrey.calorietracker.domain.dish.exception;

import com.kosovandrey.calorietracker.domain.common.exception.ResourceAlreadyExistsException;

/**
 * Исключение, возникающее при попытке создать блюдо с существующим названием
 */
public class DishAlreadyExistsException extends ResourceAlreadyExistsException {
    
    public DishAlreadyExistsException(final String name) {
        super("Блюдо", name);
    }
} 