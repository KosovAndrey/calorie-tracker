package com.kosovandrey.calorietracker.infrastructure.web.controller;

import com.kosovandrey.calorietracker.domain.user.model.UserGoal;
import com.kosovandrey.calorietracker.domain.user.model.UserRequest;
import com.kosovandrey.calorietracker.domain.user.model.UserResponse;
import com.kosovandrey.calorietracker.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController Tests")
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Nested
    @DisplayName("Get User by ID")
    class GetUserById {

        @Test
        @DisplayName("should return user response when user exists")
        void shouldReturnUserResponseWhenUserExists() {
            Long userId = 1L;
            UserResponse mockResponse = new UserResponse(1L, "Иван Иванов", "ivan@example.com", 30, 70.0, 175.0, UserGoal.WEIGHT_LOSS, 2000.0);

            when(userService.findById(userId)).thenReturn(mockResponse);

            ResponseEntity<UserResponse> response = userController.getUser(userId);

            assertEquals(200, response.getStatusCodeValue());
            assertEquals(mockResponse, response.getBody());
        }

    }

    @Nested
    @DisplayName("Create User")
    class CreateUser {

        @Test
        @DisplayName("should create a new user and return response")
        void shouldCreateNewUserAndReturnResponse() {
            UserRequest request = new UserRequest("Иван Иванов", "ivan@example.com", 30, 70.0, 175.0, UserGoal.WEIGHT_LOSS);
            UserResponse mockResponse = new UserResponse(1L, "Иван Иванов", "ivan@example.com", 30, 70.0, 175.0, UserGoal.WEIGHT_LOSS, 2000.0);

            when(userService.create(request)).thenReturn(mockResponse);

            ResponseEntity<UserResponse> response = userController.createUser(request);

            assertEquals(200, response.getStatusCodeValue());
            assertEquals(mockResponse, response.getBody());
        }
    }

    @Nested
    @DisplayName("Update User")
    class UpdateUser {

        @Test
        @DisplayName("should update user and return response")
        void shouldUpdateUserAndReturnResponse() {
            Long userId = 1L;
            UserRequest request = new UserRequest("Ваня Иванов", "vanya@example.com", 35, 75.0, 180.0, UserGoal.MAINTENANCE);
            UserResponse mockResponse = new UserResponse(1L, "Ваня Иванов", "vanya@example.com", 35, 75.0, 180.0, UserGoal.MAINTENANCE, 2500.0);

            when(userService.update(userId, request)).thenReturn(mockResponse);

            ResponseEntity<UserResponse> response = userController.updateUser(userId, request);

            assertEquals(200, response.getStatusCodeValue());
            assertEquals(mockResponse, response.getBody());
        }

    }

    @Nested
    @DisplayName("Delete User")
    class DeleteUser {

        @Test
        @DisplayName("should delete user and return 204 No Content")
        void shouldDeleteUserAndReturn204NoContent() {
            Long userId = 1L;

            ResponseEntity<Void> response = userController.deleteUser(userId);

            assertEquals(204, response.getStatusCodeValue());
            verify(userService, times(1)).delete(userId);
        }

    }

}