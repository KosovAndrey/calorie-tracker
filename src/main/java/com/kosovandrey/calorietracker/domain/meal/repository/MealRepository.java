package com.kosovandrey.calorietracker.domain.meal.repository;

import com.kosovandrey.calorietracker.domain.meal.entity.MealEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Репозиторий для работы с приемами пищи
 */
@Repository
public interface MealRepository extends JpaRepository<MealEntity, Long> {

    List<MealEntity> findByDateTimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<MealEntity> findByDateTimeBetweenAndUserId(
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            Long userId
    );

    /**
     * Находит приемы пищи за указанную дату для пользователя
     */
    @Query("SELECT m FROM MealEntity m WHERE DATE(m.dateTime) = :date AND m.user.id = :userId")
    List<MealEntity> findByDateAndUserId(
            @Param("date") LocalDateTime date,
            @Param("userId") Long userId
    );

    /**
     * Удаляет элементы приема пищи
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM MealItemEntity mi WHERE mi.meal.id = :mealId")
    void deleteItemsByMealId(Long mealId);
} 