package com.guvi.inventory.services;

import com.guvi.inventory.DTO.LoginRequest;
import com.guvi.inventory.DTO.LoginResponse;
import com.guvi.inventory.DTO.RegisterRequest;
import com.guvi.inventory.DTO.RegisterResponse;
import com.guvi.inventory.config.JwtUtil;
import com.guvi.inventory.model.Role;
import com.guvi.inventory.model.User;
import com.guvi.inventory.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
        activeUser.setPasswordHash("hashed");
        activeUser.setRole(Role.USER);
        activeUser.setActive(true);
    }

    // ── LOGIN ────────────────────────────────────────────────────────────────

    @Test
    void login_shouldReturnTokenForValidCredentials() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(activeUser));
        when(passwordEncoder.matches("secret", "hashed")).thenReturn(true);
        when(jwtUtil.generateToken(1L, Role.USER)).thenReturn("jwt-token");
        LoginResponse r = authService.login(new LoginRequest("john", "secret"));
        assertEquals("jwt-token", r.getToken());
        assertEquals(Role.USER, r.getRole());
    }

    @Test
    void login_shouldThrowForUnknownUsername() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> authService.login(new LoginRequest("unknown", "pass")));
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void login_shouldThrowForWrongPassword() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(activeUser));
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);
        assertThrows(IllegalArgumentException.class,
                () -> authService.login(new LoginRequest("john", "wrong")));
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void login_shouldThrowForInactiveUser() {
        activeUser.setActive(false);
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(activeUser));
        when(passwordEncoder.matches("secret", "hashed")).thenReturn(true);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> authService.login(new LoginRequest("john", "secret")));
        assertEquals("User account is inactive", ex.getMessage());
    }

    @Test
    void login_shouldFallBackToPasswordField() {
        activeUser.setPasswordHash(null);
        activeUser.setPassword("plain");
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(activeUser));
        when(passwordEncoder.matches("secret", "plain")).thenReturn(true);
        when(jwtUtil.generateToken(anyLong(), any())).thenReturn("t");
        assertNotNull(authService.login(new LoginRequest("john", "secret")));
    }

    // ── REGISTER ─────────────────────────────────────────────────────────────

    @Test
    void register_shouldCreateUserAndReturnSuccessMessage() {
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("new@e.com")).thenReturn(false);
        when(passwordEncoder.encode("pass")).thenReturn("encoded");
        when(userRepository.save(any())).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(99L);
            return u;
        });

        RegisterResponse r = authService.register(
                new RegisterRequest("newuser", "pass", "new@e.com", Role.USER));

        assertEquals(99L, r.getUserId());
        assertEquals("newuser", r.getUsername());
        assertEquals("new@e.com", r.getEmail());
        assertEquals(Role.USER, r.getRole());
        assertNotNull(r.getMessage());
        verify(userRepository).save(any());
        // Token must NOT be generated on register
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void register_shouldThrowForDuplicateUsername() {
        when(userRepository.existsByUsername("john")).thenReturn(true);
        assertThrows(IllegalArgumentException.class,
                () -> authService.register(
                        new RegisterRequest("john", "p", "x@e.com", Role.USER)));
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_shouldThrowForDuplicateEmail() {
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);
        assertThrows(IllegalArgumentException.class,
                () -> authService.register(
                        new RegisterRequest("newuser", "p", "john@example.com", Role.USER)));
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_shouldEncodePasswordAndNotCallJwt() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode("raw")).thenReturn("hashed-raw");
        when(userRepository.save(any())).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(5L);
            return u;
        });

        authService.register(new RegisterRequest("u", "raw", "u@e.com", Role.ADMIN));

        verify(passwordEncoder).encode("raw");
        // Confirm no token is issued during registration
        verifyNoInteractions(jwtUtil);
    }
}
