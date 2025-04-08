package com.kosovandrey.calorietracker.domain.dish.repository;

import com.kosovandrey.calorietracker.domain.dish.entity.DishEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с блюдами
 */
@Repository
public interface DishRepository extends JpaRepository<DishEntity, Long> {

    Optional<DishEntity> findByName(String name);
    
    /**
     * Поиск блюд по части названия
     */
    @Query("""
        SELECT d FROM DishEntity d 
        WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :query, '%'))
        ORDER BY d.name
    """)
    List<DishEntity> findByNameContainingIgnoreCase(@Param("query") String query);
} 