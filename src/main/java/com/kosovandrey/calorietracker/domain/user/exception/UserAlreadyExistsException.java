package com.kosovandrey.calorietracker.domain.user.exception;

import com.kosovandrey.calorietracker.domain.common.exception.ResourceAlreadyExistsException;

/**
 * Исключение, возникающее при попытке создать пользователя с существующими данными
 */
public class UserAlreadyExistsException extends ResourceAlreadyExistsException {
    
    public UserAlreadyExistsException(String field, String value) {
        super(field, value);
    }
} 