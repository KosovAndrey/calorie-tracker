package com.kosovandrey.calorietracker.infrastructure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosovandrey.calorietracker.domain.dish.model.DishRequest;
import com.kosovandrey.calorietracker.domain.dish.model.DishResponse;
import com.kosovandrey.calorietracker.domain.dish.service.DishService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Dish Controller Tests")
@WebMvcTest(DishController.class)
class DishControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DishService dishService;

    @Autowired
    private ObjectMapper objectMapper;

    private final DishResponse sampleResponse = new DishResponse(
            1L, "Овсянка", 350, 12.5, 6.7, 60.0
    );

    private final DishRequest sampleRequest = new DishRequest(
            null, "Овсянка", 350, 12.5, 6.7, 60.0
    );

    @Nested
    @DisplayName("Get Dish by ID")
    class GetDishById {

        @Test
        @DisplayName("should return dish response when dish exists")
        void shouldReturnDishResponseWhenDishExists() throws Exception {
            Mockito.when(dishService.getDish(1L)).thenReturn(sampleResponse);

            mockMvc.perform(get("/api/v1/dishes/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Овсянка"));
        }
    }

    @Nested
    @DisplayName("Create Dish")
    class CreateDish {

        @Test
        @DisplayName("should create a new dish and return response")
        void shouldCreateNewDishAndReturnResponse() throws Exception {
            Mockito.when(dishService.createDish(any())).thenReturn(sampleResponse);

            mockMvc.perform(post("/api/v1/dishes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(sampleRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Овсянка"));
        }
    }

    @Nested
    @DisplayName("Update Dish")
    class UpdateDish {

        @Test
        @DisplayName("should update dish and return response")
        void shouldUpdateDishAndReturnResponse() throws Exception {
            Mockito.when(dishService.updateDish(eq(1L), any())).thenReturn(sampleResponse);

            mockMvc.perform(put("/api/v1/dishes/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(sampleRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Овсянка"));
        }
    }

    @Nested
    @DisplayName("Delete Dish")
    class DeleteDish {

        @Test
        @DisplayName("should delete dish and return 204 No Content")
        void shouldDeleteDishAndReturn204NoContent() throws Exception {
            mockMvc.perform(delete("/api/v1/dishes/1"))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("Search Dishes")
    class SearchDishes {

        @Test
        @DisplayName("should return list of dishes matching the query")
        void shouldReturnListOfDishesMatchingTheQuery() throws Exception {
            Mockito.when(dishService.searchDishes("Овсянка"))
                    .thenReturn(List.of(sampleResponse));

            mockMvc.perform(get("/api/v1/dishes/search")
                            .param("query", "Овсянка"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].name").value("Овсянка"));
        }
    }
}