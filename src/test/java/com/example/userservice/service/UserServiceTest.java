package com.example.userservice.service;

import com.example.userservice.dao.UserDAO;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.dto.UserMapper;
import com.example.userservice.entity.User;
import com.example.userservice.exception.DatabaseException;
import com.example.userservice.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/* Юнит‑тесты для UserService с использованием Mockito
 * Мокируем зависимости (UserDAO) для изоляции бизнес‑логики*/
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserService userService;

    private UserDTO testUserDTO;
    private User testUserEntity;

    @BeforeEach
    void setUp() {
        // Инициализация тестовых данных
        testUserDTO = new UserDTO();
        testUserDTO.setId(1L);
        testUserDTO.setName("Test User");
        testUserDTO.setEmail("test@example.com");
        testUserDTO.setAge(30);

        testUserEntity = UserMapper.toEntity(testUserDTO);
        testUserEntity.setId(1L);
    }

    @Test
    void testCreateUser_Success() {
        // Arrange
        when(userDAO.save(any(User.class), any())).thenReturn(1L);

        // Act
        UserDTO result = userService.createUser(testUserDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(userDAO, times(1)).save(any(User.class), any());
    }

    @Test
    void testCreateUser_ValidationError() {
        // Arrange
        UserDTO invalidUser = new UserDTO();
        invalidUser.setName(""); // Пустое имя — невалидно
        invalidUser.setEmail("invalid-email");

        // Act & Assert
        assertThatThrownBy(() -> userService.createUser(invalidUser))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Имя пользователя не может быть пустым");

        verify(userDAO, never()).save(any(User.class), any());
    }

    @Test
    void testGetUserById_Found() {
        // Arrange
        when(userDAO.findById(1L, any())).thenReturn(testUserEntity);

        // Act
        Optional<UserDTO> result = userService.getUserById(1L);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Test User");
        verify(userDAO, times(1)).findById(1L, any());
    }

    @Test
    void testGetUserById_NotFound() {
        // Arrange
        when(userDAO.findById(999L, any())).thenReturn(null);

        // Act
        Optional<UserDTO> result = userService.getUserById(999L);

        // Assert
        assertThat(result).isEmpty();
        verify(userDAO, times(1)).findById(999L, any());
    }

    @Test
    void testGetAllUsers() {
        // Arrange
        List<User> users = Arrays.asList(testUserEntity, testUserEntity);
        when(userDAO.findAll(any())).thenReturn(users);

        // Act
        List<UserDTO> result = userService.getAllUsers();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting("name")
                .containsExactly("Test User", "Test User");
        verify(userDAO, times(1)).findAll(any());
    }

    @Test
    void testUpdateUser_Success() {
        // Arrange
        UserDTO updatedDTO = new UserDTO();
        updatedDTO.setId(1L);
        updatedDTO.setName("Updated Name");
        updatedDTO.setEmail("updated@example.com");
        updatedDTO.setAge(35);

        // Мок для findById — возвращаем существующую сущность
        when(userDAO.findById(1L, any())).thenReturn(testUserEntity);

        // Act
        UserDTO result = userService.updateUser(updatedDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Updated Name");
        assertThat(result.getAge()).isEqualTo(35);
        verify(userDAO, times(1)).update(any(User.class), any());
    }

    @Test
    void testUpdateUser_ValidationError() {
        // Arrange
        UserDTO invalidUser = new UserDTO();
        invalidUser.setId(1L);
        invalidUser.setName(""); // Пустое имя

        // Act & Assert
        assertThatThrownBy(() -> userService.updateUser(invalidUser))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Имя пользователя не может быть пустым");

        verify(userDAO, never()).update(any(User.class), any());
    }

    @Test
    void testDeleteUser_Success() {
        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userDAO, times(1)).delete(1L, any());
    }

    @Test
    void testCreateUser_DatabaseError() {
        // Arrange
        when(userDAO.save(any(User.class), any()))
                .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThatThrownBy(() -> userService.createUser(testUserDTO))
                .isInstanceOf(DatabaseException.class)
                .hasMessageContaining("Ошибка при создании пользователя");
    }
}