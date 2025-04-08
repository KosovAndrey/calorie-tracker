package com.kosovandrey.calorietracker.domain.dish.service;

import com.kosovandrey.calorietracker.domain.dish.entity.DishEntity;
import com.kosovandrey.calorietracker.domain.dish.exception.DishAlreadyExistsException;
import com.kosovandrey.calorietracker.domain.dish.exception.DishNotFoundException;
import com.kosovandrey.calorietracker.domain.dish.mapper.DishMapper;
import com.kosovandrey.calorietracker.domain.dish.model.DishRequest;
import com.kosovandrey.calorietracker.domain.dish.model.DishResponse;
import com.kosovandrey.calorietracker.domain.dish.repository.DishRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Dish Service Tests")
class DishServiceTest {

    @Mock
    private DishRepository dishRepository;

    @Mock
    private DishMapper dishMapper;

    private DishService dishService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dishService = new DishService(dishRepository, dishMapper);
    }

    @Nested
    @DisplayName("Get Dish Tests")
    class GetDishTests {

        @Test
        @DisplayName("should return dish when valid id provided")
        void shouldReturnDish() {
            Long dishId = 1L;
            DishEntity dishEntity = new DishEntity(dishId, "Пицца", 250, 12.0, 10.0, 30.0, null);
            DishResponse dishResponse = new DishResponse(dishId, "Пицца", 250, 12.0, 10.0, 30.0);

            when(dishRepository.findById(dishId)).thenReturn(Optional.of(dishEntity));
            when(dishMapper.toDishResponse(dishEntity)).thenReturn(dishResponse);

            DishResponse result = dishService.getDish(dishId);

            assertEquals(dishResponse, result);
            verify(dishRepository).findById(dishId);
        }

        @Test
        @DisplayName("should throw DishNotFoundException when dish not found")
        void shouldThrowExceptionWhenDishNotFound() {
            Long dishId = 1L;

            when(dishRepository.findById(dishId)).thenReturn(Optional.empty());

            assertThrows(DishNotFoundException.class, () -> dishService.getDish(dishId));
        }
    }

    @Nested
    @DisplayName("Create Dish Tests")
    class CreateDishTests {

        @Test
        @DisplayName("should create dish when valid request provided")
        void shouldCreateDish() {
            DishRequest dishRequest = new DishRequest(1L, "Пицца", 250, 12.0, 10.0, 30.0);
            DishEntity dishEntity = new DishEntity(1L, "Пицца", 250, 12.0, 10.0, 30.0, null);
            DishResponse dishResponse = new DishResponse(1L, "Пицца", 250, 12.0, 10.0, 30.0);

            when(dishRepository.findByName(dishRequest.name())).thenReturn(Optional.empty());
            when(dishMapper.toDish(dishRequest)).thenReturn(dishEntity);
            when(dishRepository.save(dishEntity)).thenReturn(dishEntity);
            when(dishMapper.toDishResponse(dishEntity)).thenReturn(dishResponse);

            DishResponse result = dishService.createDish(dishRequest);

            assertEquals(dishResponse, result);
            verify(dishRepository).findByName(dishRequest.name());
            verify(dishRepository).save(dishEntity);
        }

        @Test
        @DisplayName("should throw DishAlreadyExistsException when dish already exists")
        void shouldThrowExceptionWhenDishAlreadyExists() {
            DishRequest dishRequest = new DishRequest(1L, "Пицца", 250, 12.0, 10.0, 30.0);

            when(dishRepository.findByName(dishRequest.name())).thenReturn(Optional.of(new DishEntity()));

            assertThrows(DishAlreadyExistsException.class, () -> dishService.createDish(dishRequest));
        }
    }

    @Nested
    @DisplayName("Update Dish Tests")
    class UpdateDishTests {

        @Test
        @DisplayName("should update dish when valid id and request provided")
        void shouldUpdateDish() {
            Long dishId = 1L;
            DishRequest dishRequest = new DishRequest(dishId, "Пицца с сыром", 300, 15.0, 12.0, 35.0);
            DishEntity existingDishEntity = new DishEntity(dishId, "Пицца", 250, 12.0, 10.0, 30.0, 1L);
            DishEntity updatedDishEntity = new DishEntity(dishId, "Пицца с сыром", 300, 15.0, 12.0, 35.0, 1L);
            DishResponse dishResponse = new DishResponse(dishId, "Пицца с сыром", 300, 15.0, 12.0, 35.0);

            when(dishRepository.findById(dishId)).thenReturn(Optional.of(existingDishEntity));
            when(dishMapper.toDish(dishRequest)).thenReturn(updatedDishEntity);
            when(dishRepository.save(updatedDishEntity)).thenReturn(updatedDishEntity);
            when(dishMapper.toDishResponse(updatedDishEntity)).thenReturn(dishResponse);

            DishResponse result = dishService.updateDish(dishId, dishRequest);

            assertEquals(dishResponse, result);
            verify(dishRepository).findById(dishId);
            verify(dishRepository).save(updatedDishEntity);
        }

        @Test
        @DisplayName("should throw DishNotFoundException when updating non-existent dish")
        void shouldThrowExceptionWhenUpdatingNonExistentDish() {
            Long dishId = 1L;
            DishRequest dishRequest = new DishRequest(dishId, "Пицца с сыром", 300, 15.0, 12.0, 35.0);

            when(dishRepository.findById(dishId)).thenReturn(Optional.empty());

            assertThrows(DishNotFoundException.class, () -> dishService.updateDish(dishId, dishRequest));
        }
    }

    @Nested
    @DisplayName("Delete Dish Tests")
    class DeleteDishTests {

        @Test
        @DisplayName("should delete dish when valid id provided")
        void shouldDeleteDish() {
            Long dishId = 1L;

            when(dishRepository.existsById(dishId)).thenReturn(true);

            dishService.deleteDish(dishId);

            verify(dishRepository).deleteById(dishId);
        }

        @Test
        @DisplayName("should throw DishNotFoundException when deleting non-existent dish")
        void shouldThrowExceptionWhenDeletingNonExistentDish() {
            Long dishId = 1L;

            when(dishRepository.existsById(dishId)).thenReturn(false);

            assertThrows(DishNotFoundException.class, () -> dishService.deleteDish(dishId));
        }
    }

    @Test
    void testSearchDishes() {
        String query = "Пицца";
        DishEntity dishEntity = new DishEntity(1L, "Пицца", 250, 12.0, 10.0, 30.0, null);
        DishResponse dishResponse = new DishResponse(1L, "Пицца", 250, 12.0, 10.0, 30.0);

        when(dishRepository.findByNameContainingIgnoreCase(query)).thenReturn(List.of(dishEntity));
        when(dishMapper.toDishResponse(dishEntity)).thenReturn(dishResponse);

        List<DishResponse> result = dishService.searchDishes(query);

        assertEquals(1, result.size());
        assertEquals(dishResponse, result.get(0));
    }
}