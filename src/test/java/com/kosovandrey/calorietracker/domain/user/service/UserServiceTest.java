package com.kosovandrey.calorietracker.domain.user.service;

import com.kosovandrey.calorietracker.domain.user.entity.UserEntity;
import com.kosovandrey.calorietracker.domain.user.exception.UserNotFoundException;
import com.kosovandrey.calorietracker.domain.user.mapper.UserMapper;
import com.kosovandrey.calorietracker.domain.user.model.UserGoal;
import com.kosovandrey.calorietracker.domain.user.model.UserRequest;
import com.kosovandrey.calorietracker.domain.user.model.UserResponse;
import com.kosovandrey.calorietracker.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Nested
    @DisplayName("Create User")
    class CreateUser {

        @Test
        @DisplayName("should create a new user and calculate daily calorie norm")
        void shouldCreateNewUserAndCalculateCalories() {
            UserRequest request = new UserRequest("Иван Иванов", "ivan@example.com", 30, 70.0, 175.0, UserGoal.WEIGHT_LOSS);
            UserEntity entity = new UserEntity();
            entity.setId(1L);
            entity.setName(request.name());
            entity.setEmail(request.email());
            entity.setAge(request.age());
            entity.setWeight(request.weight());
            entity.setHeight(request.height());
            entity.setGoal(request.goal());

            when(userMapper.toEntity(request)).thenReturn(entity);
            when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(userMapper.toResponse(any(UserEntity.class))).thenCallRealMethod();

            UserResponse response = userService.create(request);

            assertNotNull(response);
            assertEquals(1L, response.id());
            verify(userRepository, times(1)).save(any(UserEntity.class));
        }
    }

    @Nested
    @DisplayName("Find User by ID")
    class FindUserById {

        @Test
        @DisplayName("should return user when found by ID")
        void shouldReturnUserWhenFoundById() {
            Long userId = 1L;
            UserEntity entity = new UserEntity();
            entity.setId(userId);
            entity.setName("Иван Иванов");
            entity.setEmail("ivan@example.com");

            when(userRepository.findById(userId)).thenReturn(Optional.of(entity));
            when(userMapper.toResponse(entity)).thenCallRealMethod();

            UserResponse response = userService.findById(userId);

            assertNotNull(response);
            assertEquals(userId, response.id());
        }

        @Test
        @DisplayName("should throw UserNotFoundException when user not found by ID")
        void shouldThrowExceptionWhenUserNotFoundById() {
            Long userId = 1L;
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> userService.findById(userId));
        }
    }

    @Nested
    @DisplayName("Find All Users")
    class FindAllUsers {

        @Test
        @DisplayName("should return list of all users")
        void shouldReturnListOfAllUsers() {
            UserEntity entity1 = new UserEntity();
            entity1.setId(1L);
            entity1.setName("Иван Иванов");

            UserEntity entity2 = new UserEntity();
            entity2.setId(2L);
            entity2.setName("Петр Петров");

            when(userRepository.findAll()).thenReturn(List.of(entity1, entity2));
            when(userMapper.toResponse(any(UserEntity.class))).thenCallRealMethod();

            List<UserResponse> responses = userService.findAll();

            assertNotNull(responses);
            assertEquals(2, responses.size());
            assertEquals("Иван Иванов", responses.get(0).name());
            assertEquals("Петр Петров", responses.get(1).name());
        }
    }

    @Nested
    @DisplayName("Update User")
    class UpdateUser {

        @Test
        @DisplayName("should update user details successfully")
        void shouldUpdateUserDetailsSuccessfully() {
            Long userId = 1L;
            UserRequest request = new UserRequest("Ваня Иванов", "vanya@example.com", 35, 75.0, 180.0, UserGoal.MAINTENANCE);
            UserEntity entity = new UserEntity();
            entity.setId(userId);
            entity.setName("Иван Иванов");
            entity.setEmail("ivan@example.com");

            when(userRepository.findById(userId)).thenReturn(Optional.of(entity));
            when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(userMapper.toResponse(any(UserEntity.class))).thenCallRealMethod();

            UserResponse response = userService.update(userId, request);

            assertNotNull(response);
            assertEquals("Ваня Иванов", response.name());
            assertEquals("vanya@example.com", response.email());
            verify(userRepository, times(1)).save(any(UserEntity.class));
        }

        @Test
        @DisplayName("should throw UserNotFoundException when updating non-existent user")
        void shouldThrowExceptionWhenUpdatingNonExistentUser() {
            Long userId = 1L;
            UserRequest request = new UserRequest("Ваня Иванов", "vanya@example.com", 35, 75.0, 180.0, UserGoal.MAINTENANCE);

            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> userService.update(userId, request));
        }
    }

    @Nested
    @DisplayName("Delete User")
    class DeleteUser {

        @Test
        @DisplayName("should delete user successfully")
        void shouldDeleteUserSuccessfully() {
            Long userId = 1L;
            when(userRepository.existsById(userId)).thenReturn(true);

            userService.delete(userId);

            verify(userRepository, times(1)).deleteById(userId);
        }

        @Test
        @DisplayName("should throw UserNotFoundException when deleting non-existent user")
        void shouldThrowExceptionWhenDeletingNonExistentUser() {
            Long userId = 1L;
            when(userRepository.existsById(userId)).thenReturn(false);

            assertThrows(UserNotFoundException.class, () -> userService.delete(userId));
        }
    }
}