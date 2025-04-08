package com.kosovandrey.calorietracker.domain.meal.entity;

import com.kosovandrey.calorietracker.domain.meal.model.MealType;
import com.kosovandrey.calorietracker.domain.meal_item.entity.MealItemEntity;
import com.kosovandrey.calorietracker.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность приема пищи
 */
@Entity
@Table(name = "meals", indexes = {
    @Index(name = "idx_meal_date_user", columnList = "date_time,user_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL)
    @Builder.Default
    private List<MealItemEntity> items = new ArrayList<>();

    @Column(name = "total_calories", nullable = false)
    private double totalCalories;

    @Column(name = "total_proteins", nullable = false)
    private double totalProteins;

    @Column(name = "total_fats", nullable = false)
    private double totalFats;

    @Column(name = "total_carbohydrates", nullable = false)
    private double totalCarbohydrates;

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type", nullable = false)
    private MealType mealType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Version
    private Long version;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
} 