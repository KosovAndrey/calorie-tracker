package com.kosovandrey.calorietracker.domain.common.exception;

/**
 * Базовый класс для исключений, связанных с отсутствием ресурсов
 */
public abstract class ResourceNotFoundException extends RuntimeException {
    
    protected ResourceNotFoundException(String message) {
        super(message);
    }
    
    protected ResourceNotFoundException(String resourceName, Object resourceId) {
        super(String.format("%s с ID %s не найден", resourceName, resourceId));
    }
} 