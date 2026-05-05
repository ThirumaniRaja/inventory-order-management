package com.guvi.inventory.services;

import com.guvi.inventory.DTO.LoginRequest;
import com.guvi.inventory.DTO.LoginResponse;
import com.guvi.inventory.DTO.RegisterRequest;
import com.guvi.inventory.config.JwtUtil;
import com.guvi.inventory.model.Role;
import com.guvi.inventory.model.User;
import com.guvi.inventory.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtUtil jwtUtil;

    @InjectMocks AuthService authService;

    private User activeUser;

    @BeforeEach
    void setUp() {
        activeUser = new User();
        activeUser.setId(1L);
        activeUser.setUsername("john");
        activeUser.setEmail("john@example.com");
        activeUser.setPasswordHash("hashed-password");
        activeUser.setRole(Role.USER);
        activeUser.setActive(true);
    }

    // ──────────────────── LOGIN ────────────────────

    @Nested
    @DisplayName("login()")
    class Login {

        @Test
        @DisplayName("should return token when credentials are valid")
        void shouldReturnTokenForValidCredentials() {
            when(userRepository.findByUsername("john")).thenReturn(Optional.of(activeUser));
            when(passwordEncoder.matches("secret", "hashed-password")).thenReturn(true);
            when(jwtUtil.generateToken(1L, Role.USER)).thenReturn("jwt-token");

            LoginResponse response = authService.login(new LoginRequest("john", "secret"));

            assertNotNull(response);
            assertEquals("jwt-token", response.getToken());
            assertEquals(1L, response.getUserId());
            assertEquals("john", response.getUsername());
            assertEquals(Role.USER, response.getRole());
            verify(jwtUtil).generateToken(1L, Role.USER);
        }

        @Test
        @DisplayName("should throw when username is not found")
        void shouldThrowForUnknownUsername() {
            when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> authService.login(new LoginRequest("unknown", "pass")));

            assertEquals("Invalid username or password", ex.getMessage());
            verifyNoInteractions(jwtUtil);
        }

        @Test
        @DisplayName("should throw when password does not match")
        void shouldThrowForWrongPassword() {
            when(userRepository.findByUsername("john")).thenReturn(Optional.of(activeUser));
            when(passwordEncoder.matches("wrong", "hashed-password")).thenReturn(false);

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> authService.login(new LoginRequest("john", "wrong")));

            assertEquals("Invalid username or password", ex.getMessage());
            verifyNoInteractions(jwtUtil);
        }

        @Test
        @DisplayName("should throw when account is inactive")
        void shouldThrowForInactiveUser() {
            activeUser.setActive(false);
            when(userRepository.findByUsername("john")).thenReturn(Optional.of(activeUser));
            when(passwordEncoder.matches("secret", "hashed-password")).thenReturn(true);

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> authService.login(new LoginRequest("john", "secret")));

            assertEquals("User account is inactive", ex.getMessage());
        }

        @Test
        @DisplayName("should fall back to password field when passwordHash is null")
        void shouldUsePlainPasswordFieldWhenHashIsNull() {
            activeUser.setPasswordHash(null);
            activeUser.setPassword("plain-hash");
            when(userRepository.findByUsername("john")).thenReturn(Optional.of(activeUser));
            when(passwordEncoder.matches("secret", "plain-hash")).thenReturn(true);
            when(jwtUtil.generateToken(1L, Role.USER)).thenReturn("token");

            LoginResponse response = authService.login(new LoginRequest("john", "secret"));

            assertNotNull(response);
        }
    }

    // ──────────────────── REGISTER ────────────────────

    @Nested
    @DisplayName("register()")
    class Register {

        @Test
        @DisplayName("should create user and return token for valid request")
        void shouldRegisterUserSuccessfully() {
            when(userRepository.existsByUsername("newuser")).thenReturn(false);
            when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
            when(passwordEncoder.encode("pass")).thenReturn("encoded");
            when(userRepository.save(any(User.class))).thenAnswer(inv -> {
                User u = inv.getArgument(0);
                u.setId(99L);
                return u;
            });
            when(jwtUtil.generateToken(99L, Role.USER)).thenReturn("new-token");

            RegisterRequest request = new RegisterRequest("newuser", "pass", "new@example.com", Role.USER);
            LoginResponse response = authService.register(request);

            assertEquals("new-token", response.getToken());
            assertEquals(99L, response.getUserId());
            assertEquals("newuser", response.getUsername());
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("should throw when username already exists")
        void shouldThrowForDuplicateUsername() {
            when(userRepository.existsByUsername("john")).thenReturn(true);

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> authService.register(new RegisterRequest("john", "pass", "other@x.com", Role.USER)));

            assertEquals("Username already exists", ex.getMessage());
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("should throw when email already exists")
        void shouldThrowForDuplicateEmail() {
            when(userRepository.existsByUsername("newuser")).thenReturn(false);
            when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> authService.register(new RegisterRequest("newuser", "pass", "john@example.com", Role.USER)));

            assertEquals("Email already exists", ex.getMessage());
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("should encode password before saving")
        void shouldEncodePasswordBeforeSave() {
            when(userRepository.existsByUsername(anyString())).thenReturn(false);
            when(userRepository.existsByEmail(anyString())).thenReturn(false);
            when(passwordEncoder.encode("raw")).thenReturn("encoded-hash");
            when(userRepository.save(any(User.class))).thenAnswer(inv -> {
                User u = inv.getArgument(0);
                u.setId(5L);
                return u;
            });
            when(jwtUtil.generateToken(anyLong(), any())).thenReturn("token");

            authService.register(new RegisterRequest("u", "raw", "u@e.com", Role.ADMIN));

            verify(passwordEncoder).encode("raw");
        }
    }
}

