package com.kosovandrey.calorietracker.domain.user.service;

import com.kosovandrey.calorietracker.domain.user.entity.UserEntity;
import com.kosovandrey.calorietracker.domain.user.exception.UserNotFoundException;
import com.kosovandrey.calorietracker.domain.user.mapper.UserMapper;
import com.kosovandrey.calorietracker.domain.user.model.UserRequest;
import com.kosovandrey.calorietracker.domain.user.model.UserResponse;
import com.kosovandrey.calorietracker.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис для работы с пользователями
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse create(UserRequest request) {
        UserEntity entity = userMapper.toEntity(request);
        calculateDailyCalorieNorm(entity);
        return userMapper.toResponse(userRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        return userMapper.toResponse(userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("Пользователь не найден")));
    }

    @Transactional(readOnly = true)
    public UserEntity findEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
            .map(userMapper::toResponse)
            .toList();
    }

    @Transactional
    public UserResponse update(Long id, UserRequest request) {
        UserEntity entity = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        entity.setName(request.name());
        entity.setEmail(request.email());
        entity.setAge(request.age());
        entity.setWeight(request.weight());
        entity.setHeight(request.height());
        entity.setGoal(request.goal());
        calculateDailyCalorieNorm(entity);

        return userMapper.toResponse(userRepository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        userRepository.deleteById(id);
    }

    private void calculateDailyCalorieNorm(UserEntity user) {
        // Базальный метаболизм
        double bmr = 10 * user.getWeight() + 6.25 * user.getHeight() - 5 * user.getAge() + 5;

        // Коэффициент активности (средний)
        double activityFactor = 1.55;

        // Целевой коэффициент
        double goalFactor = switch (user.getGoal()) {
            case WEIGHT_LOSS -> 0.85;  // -15% калорий
            case MAINTENANCE -> 1.0;   // без изменений
            case WEIGHT_GAIN -> 1.15;  // +15% калорий
        };

        user.setDailyCalorieNorm(bmr * activityFactor * goalFactor);
    }
} 