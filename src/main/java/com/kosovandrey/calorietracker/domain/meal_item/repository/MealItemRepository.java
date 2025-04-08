package com.kosovandrey.calorietracker.domain.meal_item.repository;

import com.kosovandrey.calorietracker.domain.meal_item.entity.MealItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с элементами приемов пищи
 */
@Repository
public interface MealItemRepository extends JpaRepository<MealItemEntity, Long> {

    List<MealItemEntity> findByMealId(Long mealId);

    void deleteByMealId(Long mealId);
} 