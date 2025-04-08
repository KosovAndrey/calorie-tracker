package com.kosovandrey.calorietracker.domain.meal.service;

import com.kosovandrey.calorietracker.domain.dish.entity.DishEntity;
import com.kosovandrey.calorietracker.domain.dish.model.DishResponse;
import com.kosovandrey.calorietracker.domain.dish.service.DishService;
import com.kosovandrey.calorietracker.domain.meal.entity.MealEntity;
import com.kosovandrey.calorietracker.domain.meal.exception.MealNotFoundException;
import com.kosovandrey.calorietracker.domain.meal.mapper.MealMapper;
import com.kosovandrey.calorietracker.domain.meal.model.MealRequest;
import com.kosovandrey.calorietracker.domain.meal.model.MealResponse;
import com.kosovandrey.calorietracker.domain.meal.model.MealType;
import com.kosovandrey.calorietracker.domain.meal_item.entity.MealItemEntity;
import com.kosovandrey.calorietracker.domain.meal_item.mapper.MealItemMapper;
import com.kosovandrey.calorietracker.domain.meal_item.model.MealItemRequest;
import com.kosovandrey.calorietracker.domain.meal_item.model.MealItemResponse;
import com.kosovandrey.calorietracker.domain.meal_item.repository.MealItemRepository;
import com.kosovandrey.calorietracker.domain.meal.repository.MealRepository;
import com.kosovandrey.calorietracker.domain.user.entity.UserEntity;
import com.kosovandrey.calorietracker.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MealService Tests")
class MealServiceTest {

    @Mock
    private MealRepository mealRepository;

    @Mock
    private MealItemRepository mealItemRepository;

    @Mock
    private MealMapper mealMapper;

    @InjectMocks
    private MealService mealService;

    @Nested
    @DisplayName("Get Meal by ID")
    class GetMealById {

        @Test
        @DisplayName("should return meal when found by ID")
        void shouldReturnMealWhenFoundById() {
            Long mealId = 1L;
            MealEntity entity = new MealEntity();
            entity.setId(mealId);
            entity.setDateTime(LocalDateTime.now());
            entity.setMealType(MealType.BREAKFAST);

            List<MealItemEntity> mealItems = List.of(
                    new MealItemEntity(1L, null, null, 100.0, 400.0, 20.0, 10.0, 30.0, 1L)
            );

            MealResponse expectedResponse = new MealResponse(
                    mealId, 1L, LocalDateTime.now(), "BREAKFAST", 250.0, 12.0, 10.0, 30.0,
                    List.of(new MealItemResponse(1L, mealId, 1L, "Пицца", 100.0, 250.0, 12.0, 10.0, 30.0))
            );

            when(mealRepository.findById(mealId)).thenReturn(Optional.of(entity));
            when(mealItemRepository.findByMealId(mealId)).thenReturn(mealItems);
            when(mealMapper.toResponse(entity, mealItems)).thenReturn(expectedResponse);

            MealResponse response = mealService.getMeal(mealId);

            assertNotNull(response);
            assertEquals(mealId, response.id());
            assertEquals("BREAKFAST", response.mealType());
            assertEquals(250.0, response.totalCalories());
            verify(mealRepository, times(1)).findById(mealId);
            verify(mealItemRepository, times(1)).findByMealId(mealId);
        }

        @Test
        @DisplayName("should throw MealNotFoundException when meal not found by ID")
        void shouldThrowExceptionWhenMealNotFoundById() {
            Long mealId = 1L;
            when(mealRepository.findById(mealId)).thenReturn(Optional.empty());

            assertThrows(MealNotFoundException.class, () -> mealService.getMeal(mealId));
        }
    }

    @Nested
    @DisplayName("Delete Meal")
    class DeleteMeal {

        @Test
        @DisplayName("should delete meal successfully")
        void shouldDeleteMealSuccessfully() {
            Long mealId = 1L;
            when(mealRepository.existsById(mealId)).thenReturn(true);

            mealService.deleteMeal(mealId);

            verify(mealItemRepository, times(1)).deleteByMealId(mealId);
            verify(mealRepository, times(1)).deleteById(mealId);
        }

        @Test
        @DisplayName("should throw MealNotFoundException when deleting non-existent meal")
        void shouldThrowExceptionWhenDeletingNonExistentMeal() {
            Long mealId = 1L;
            when(mealRepository.existsById(mealId)).thenReturn(false);

            assertThrows(MealNotFoundException.class, () -> mealService.deleteMeal(mealId));
        }
    }
}