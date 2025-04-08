package com.kosovandrey.calorietracker.infrastructure.web.controller;

import com.kosovandrey.calorietracker.domain.dish.entity.DishEntity;
import com.kosovandrey.calorietracker.domain.meal.entity.MealEntity;
import com.kosovandrey.calorietracker.domain.meal.model.MealRequest;
import com.kosovandrey.calorietracker.domain.meal.model.MealResponse;
import com.kosovandrey.calorietracker.domain.meal.model.MealType;
import com.kosovandrey.calorietracker.domain.meal.service.MealService;
import com.kosovandrey.calorietracker.domain.meal_item.entity.MealItemEntity;
import com.kosovandrey.calorietracker.domain.meal_item.model.MealItemRequest;
import com.kosovandrey.calorietracker.domain.meal_item.model.MealItemResponse;
import com.kosovandrey.calorietracker.domain.user.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MealControllerTest {

    @Mock
    private MealService mealService;

    @InjectMocks
    private MealController mealController;

    private MealEntity meal;
    private UserEntity user;
    private DishEntity dish;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(1L);

        dish = new DishEntity();
        dish.setId(1L);
        dish.setName("Пицца");
        dish.setCalories(300);
        dish.setProteins(10.0);
        dish.setFats(12.0);
        dish.setCarbohydrates(35.0);

        meal = new MealEntity();
        meal.setId(1L);
        meal.setDateTime(LocalDateTime.of(2025, 4, 1, 8, 0));
        meal.setMealType(MealType.BREAKFAST);
        meal.setTotalCalories(500);
        meal.setTotalProteins(20);
        meal.setTotalFats(10);
        meal.setTotalCarbohydrates(30);
        meal.setUser(user);

        MealItemEntity item = new MealItemEntity();
        item.setId(1L);
        item.setMeal(meal);
        item.setDish(dish);
        item.setPortion(200.0);
        item.setCalories(600.0);
        item.setProteins(20.0);
        item.setFats(24.0);
        item.setCarbohydrates(70.0);

        meal.setItems(List.of(item));
    }

    @Nested
    @DisplayName("Tests for getMeal method")
    class GetMealTests {

        @Test
        @DisplayName("Should return meal by ID")
        void shouldReturnMealById() {
            when(mealService.getMeal(1L)).thenReturn(new MealResponse(
                    meal.getId(),
                    meal.getUser().getId(),
                    meal.getDateTime(),
                    meal.getMealType().name(),
                    meal.getTotalCalories(),
                    meal.getTotalProteins(),
                    meal.getTotalFats(),
                    meal.getTotalCarbohydrates(),
                    List.of()
            ));

            MealResponse response = mealController.getMeal(1L);

            assertNotNull(response);
            assertEquals(1L, response.id());
            assertEquals(MealType.BREAKFAST.name(), response.mealType());
            verify(mealService, times(1)).getMeal(1L);
        }
    }

    @Nested
    @DisplayName("Tests for getMealsByDate method")
    class GetMealsByDateTests {

        @Test
        @DisplayName("Should return meals for the specified date")
        void shouldReturnMealsForTheSpecifiedDate() {
            LocalDate date = LocalDate.of(2023, 10, 1);
            when(mealService.getMealsByDate(date, 1L)).thenReturn(List.of(new MealResponse(
                    meal.getId(),
                    meal.getUser().getId(),
                    meal.getDateTime(),
                    meal.getMealType().name(),
                    meal.getTotalCalories(),
                    meal.getTotalProteins(),
                    meal.getTotalFats(),
                    meal.getTotalCarbohydrates(),
                    List.of()
            )));

            List<MealResponse> response = mealController.getMealsByDate(date, 1L);

            assertNotNull(response);
            assertEquals(1, response.size());
            assertEquals(1L, response.get(0).id());
            verify(mealService, times(1)).getMealsByDate(date, 1L);
        }
    }

    @Nested
    @DisplayName("Tests for createMeal method")
    class CreateMealTests {

        @Test
        @DisplayName("Should create a new meal")
        void shouldCreateANewMeal() {
            MealRequest request = new MealRequest(
                    1L,
                    LocalDateTime.of(2025, 4, 1, 8, 0),
                    List.of(new MealItemRequest(1L, 200.0)),
                    MealType.BREAKFAST.getValue()
            );

            when(mealService.createMeal(request)).thenReturn(new MealResponse(
                    meal.getId(),
                    meal.getUser().getId(),
                    meal.getDateTime(),
                    meal.getMealType().name(),
                    meal.getTotalCalories(),
                    meal.getTotalProteins(),
                    meal.getTotalFats(),
                    meal.getTotalCarbohydrates(),
                    List.of()
            ));

            MealResponse response = mealController.createMeal(request);

            assertNotNull(response);
            assertEquals(1L, response.id());
            assertEquals(MealType.BREAKFAST.name(), response.mealType());
            verify(mealService, times(1)).createMeal(request);
        }
    }

    @Nested
    @DisplayName("Tests for updateMeal method")
    class UpdateMealTests {

        @Test
        @DisplayName("Should update an existing meal")
        void shouldUpdateAnExistingMeal() {
            MealRequest request = new MealRequest(
                    1L,
                    LocalDateTime.of(2025, 4, 1, 8, 0),
                    List.of(new MealItemRequest(1L, 200.0)),
                    MealType.BREAKFAST.getValue()
            );

            when(mealService.updateMeal(1L, request)).thenReturn(new MealResponse(
                    meal.getId(),
                    meal.getUser().getId(),
                    meal.getDateTime(),
                    meal.getMealType().name(),
                    meal.getTotalCalories(),
                    meal.getTotalProteins(),
                    meal.getTotalFats(),
                    meal.getTotalCarbohydrates(),
                    List.of()
            ));

            MealResponse response = mealController.updateMeal(1L, request);

            assertNotNull(response);
            assertEquals(1L, response.id());
            verify(mealService, times(1)).updateMeal(1L, request);
        }
    }

    @Nested
    @DisplayName("Tests for deleteMeal method")
    class DeleteMealTests {

        @Test
        @DisplayName("Should delete a meal")
        void shouldDeleteAMeal() {
            doNothing().when(mealService).deleteMeal(1L);

            mealController.deleteMeal(1L);

            verify(mealService, times(1)).deleteMeal(1L);
        }
    }

    @Nested
    @DisplayName("Tests for createMealItem method")
    class CreateMealItemTests {

        @Test
        @DisplayName("Should create a meal item")
        void shouldCreateAMealItem() {
            MealItemRequest request = new MealItemRequest(1L, 200.0);

            when(mealService.createMealItem(1L, request)).thenReturn(new com.kosovandrey.calorietracker.domain.meal_item.model.MealItemResponse(
                    1L,
                    1L,
                    1L,
                    "Пицца",
                    200.0,
                    600.0,
                    20.0,
                    24.0,
                    70.0
            ));

            MealItemResponse response = mealController.createMealItem(1L, request);

            assertNotNull(response);
            assertEquals("Пицца", response.dishName());
            assertEquals(600.0, response.calories());
            assertEquals(20.0, response.proteins());
            assertEquals(24.0, response.fats());
            assertEquals(70.0, response.carbohydrates());
            verify(mealService, times(1)).createMealItem(1L, request);
        }
    }
}