package com.ms_products.service.client;

import com.ms_products.client.UserClientFallback;
import com.ms_products.dto.UserResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserClientFallbackTest {

    private UserClientFallback userClientFallback;

    @BeforeEach
    void setUp() {
        userClientFallback = new UserClientFallback();
    }

    @Test
    @DisplayName("Should return a safe UserResponseDTO with UNKNOWN role when called")
    void getUserById_ShouldReturnSafeResponse() {
        // GIVEN
        Long userId = 123L;

        // WHEN
        UserResponseDTO result = userClientFallback.getUserById(userId);

        // THEN
        assertNotNull(result, "The fallback should never return null");
        assertEquals(userId, result.getId(), "The returned userId should match the input");
        assertEquals("UNKNOWN", result.getRole(), "The role must be UNKNOWN for security reasons");
    }
}
