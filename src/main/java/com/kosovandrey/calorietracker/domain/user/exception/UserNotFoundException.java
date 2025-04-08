package com.kosovandrey.calorietracker.domain.user.exception;

import com.kosovandrey.calorietracker.domain.common.exception.ResourceNotFoundException;

/**
 * Исключение, возникающее при отсутствии пользователя
 */
public class UserNotFoundException extends ResourceNotFoundException {
    
    public UserNotFoundException(Long id) {
        super("Пользователь", id);
    }
    
    public UserNotFoundException(String username) {
        super(String.format("Пользователь с именем %s не найден", username));
    }
} 