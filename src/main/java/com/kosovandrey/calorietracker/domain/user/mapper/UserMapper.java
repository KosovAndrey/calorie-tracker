package com.kosovandrey.calorietracker.domain.user.mapper;

import com.kosovandrey.calorietracker.domain.user.entity.UserEntity;
import com.kosovandrey.calorietracker.domain.user.model.UserRequest;
import com.kosovandrey.calorietracker.domain.user.model.UserResponse;
import org.springframework.stereotype.Component;

/**
 * Маппер для преобразования между сущностями и моделями пользователей
 */
@Component
public class UserMapper {

    public UserEntity toEntity(UserRequest request) {
        return UserEntity.builder()
            .name(request.name())
            .email(request.email())
            .age(request.age())
            .weight(request.weight())
            .height(request.height())
            .goal(request.goal())
            .dailyCalorieNorm(null) // будет рассчитан в сервисе
            .build();
    }

    public UserResponse toResponse(UserEntity entity) {
        return new UserResponse(
            entity.getId(),
            entity.getName(),
            entity.getEmail(),
            entity.getAge(),
            entity.getWeight(),
            entity.getHeight(),
            entity.getGoal(),
            entity.getDailyCalorieNorm()
        );
    }
} 