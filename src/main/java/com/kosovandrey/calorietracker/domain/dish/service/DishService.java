package com.kosovandrey.calorietracker.domain.dish.service;

import com.kosovandrey.calorietracker.domain.dish.entity.DishEntity;
import com.kosovandrey.calorietracker.domain.dish.exception.DishAlreadyExistsException;
import com.kosovandrey.calorietracker.domain.dish.exception.DishNotFoundException;
import com.kosovandrey.calorietracker.domain.dish.mapper.DishMapper;
import com.kosovandrey.calorietracker.domain.dish.model.DishRequest;
import com.kosovandrey.calorietracker.domain.dish.model.DishResponse;
import com.kosovandrey.calorietracker.domain.dish.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с блюдами
 * Предоставляет методы для создания, чтения, обновления и удаления блюд,
 * а также поиска блюд по названию
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DishService {

    private final DishRepository dishRepository;
    private final DishMapper dishMapper;

    public DishResponse getDish(final Long id) {
        return dishRepository.findById(id)
                .map(dishMapper::toDishResponse)
                .orElseThrow(() -> new DishNotFoundException(id));
    }

    public List<DishResponse> getAllDishes() {
        return dishRepository.findAll()
                .stream()
                .map(dishMapper::toDishResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public DishResponse createDish(final DishRequest dish) {
        if (dishRepository.findByName(dish.name()).isPresent()) {
            throw new DishAlreadyExistsException(dish.name());
        }
        return dishMapper.toDishResponse(dishRepository.save(dishMapper.toDish(dish)));
    }

    @Transactional
    public DishResponse updateDish(final Long id, final DishRequest request) {
        var existingEntity = dishRepository.findById(id)
            .orElseThrow(() -> new DishNotFoundException(id));
        
        var updatedEntity = dishMapper.toDish(request);
        updatedEntity.setId(existingEntity.getId());
        updatedEntity.setVersion(existingEntity.getVersion());
        return dishMapper.toDishResponse(dishRepository.save(updatedEntity));
    }

    @Transactional
    public void deleteDish(final Long id) {
        if (!dishRepository.existsById(id)) {
            throw new DishNotFoundException(id);
        }
        dishRepository.deleteById(id);
    }

    public List<DishResponse> searchDishes(final String query) {
        return dishRepository.findByNameContainingIgnoreCase(query)
                .stream()
                .map(dishMapper::toDishResponse)
                .collect(Collectors.toList());
    }

    public DishEntity getDishEntity(final Long id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new DishNotFoundException(id));
    }
} 