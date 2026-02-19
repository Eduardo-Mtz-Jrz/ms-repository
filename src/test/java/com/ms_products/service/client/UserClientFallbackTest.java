package com.ms_products.service.client;

import com.ms_products.client.UserClientFallback;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class UserClientFallbackTest {

    @InjectMocks
    private UserClientFallback userClientFallback;

    @Test
    @DisplayName("Debe cubrir la lógica de isAdmin y retornar false por defecto")
    void isAdmin_ShouldReturnFalse_WhenCalled() {
        Long userId = 1L;

        Boolean result = assertDoesNotThrow(() -> userClientFallback.isAdmin(userId));

        assertThat(result).isFalse();
    }
}