package com.kosovandrey.calorietracker.domain.dish.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Сущность для хранения блюда
 */
@Entity
@Table(name = "dishes", indexes = {
        @Index(name = "idx_dish_name", columnList = "name")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    int calories;

    @Column(nullable = false)
    double proteins;

    @Column(nullable = false)
    double fats;

    @Column(nullable = false)
    double carbohydrates;

    @Version
    private Long version;
}