package com.kosovandrey.calorietracker.domain.common.exception;

/**
 * Базовый класс для исключений, связанных с дублированием ресурсов
 */
public abstract class ResourceAlreadyExistsException extends RuntimeException {
    
    protected ResourceAlreadyExistsException(String message) {
        super(message);
    }
    
    protected ResourceAlreadyExistsException(String field, String value) {
        super(String.format("%s со значением %s уже существует", field, value));
    }
} 