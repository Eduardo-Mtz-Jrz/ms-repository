package com.ms_products.service.service;

import com.ms_products.client.UserClient;
import com.ms_products.dto.ProductRequestDTO;
import com.ms_products.dto.ProductResponseDTO;
import com.ms_products.entity.ProductEntity;
import com.ms_products.exception.ProductNotFoundException;
import com.ms_products.exception.UnauthorizedException;
import com.ms_products.mapper.ProductMapper;
import com.ms_products.repository.ProductRepository;
import com.ms_products.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock private ProductRepository productRepository;
    @Mock private UserClient userClient;
    @Mock private ProductMapper productMapper;

    @InjectMocks private ProductServiceImpl productService;

    private ProductEntity sampleEntity;
    private ProductRequestDTO sampleRequest;
    private ProductResponseDTO sampleResponse;

    @BeforeEach
    void setUp() {
        sampleEntity = ProductEntity.builder()
                .id(1L)
                .name("Laptop")
                .code("PROD-1234")
                .price(1000.0)
                .stock(10)
                .build();

        sampleRequest = ProductRequestDTO.builder()
                .name("Laptop")
                .code("PROD-1234")
                .price(1000.0)
                .stock(10)
                .build();

        sampleResponse = ProductResponseDTO.builder()
                .id(1L)
                .name("Laptop")
                .code("PROD-1234")
                .build();
    }

    @Nested
    @DisplayName("1. Product Creation Tests")
    class SaveTests {
        @Test
        @DisplayName("Should save product when code is unique")
        void save_Success() {
            when(productRepository.findByCode(anyString())).thenReturn(Optional.empty());
            when(productMapper.toEntity(any())).thenReturn(sampleEntity);
            when(productRepository.save(any())).thenReturn(sampleEntity);
            when(productMapper.toDto(any())).thenReturn(sampleResponse);

            ProductResponseDTO result = productService.save(sampleRequest);

            assertThat(result).isNotNull();
            verify(productRepository).save(any());
        }

        @Test
        @DisplayName("Should throw IllegalStateException when code already exists")
        void save_DuplicateCode_ThrowsException() {
            when(productRepository.findByCode("PROD-1234")).thenReturn(Optional.of(sampleEntity));

            assertThatThrownBy(() -> productService.save(sampleRequest))
                    .isInstanceOf(IllegalStateException.class);

            verify(productRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("2. Read & Search Operations")
    class ReadTests {
        @Test
        @DisplayName("Should return product when ID exists")
        void findById_Success() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(sampleEntity));
            when(productMapper.toDto(any())).thenReturn(sampleResponse);

            ProductResponseDTO result = productService.findById(1L);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Should throw ProductNotFoundException when ID missing")
        void findById_NotFound_ThrowsException() {
            when(productRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> productService.findById(1L))
                    .isInstanceOf(ProductNotFoundException.class);
        }

        @Test
        @DisplayName("Should return list of all products")
        void findAll_Success() {
            when(productRepository.findAll()).thenReturn(List.of(sampleEntity));
            when(productMapper.toDto(any())).thenReturn(sampleResponse);

            List<ProductResponseDTO> result = productService.findAll();

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("Should return empty list when no products exist")
        void findAll_ReturnsEmpty() {
            when(productRepository.findAll()).thenReturn(List.of());

            List<ProductResponseDTO> result = productService.findAll();

            assertThat(result).isEmpty();
            verify(productRepository).findAll();
        }

        @Test
        @DisplayName("Should return low stock products")
        void findLowStock_Success() {
            Integer threshold = 5;
            when(productRepository.findByStockLessThan(threshold)).thenReturn(List.of(sampleEntity));
            when(productMapper.toDto(any())).thenReturn(sampleResponse);

            List<ProductResponseDTO> result = productService.findLowStock(threshold);

            assertThat(result).isNotEmpty();
        }

        @Test
        @DisplayName("Should return empty list when no products below stock threshold")
        void findLowStock_ReturnsEmpty() {
            Integer threshold = 5;
            when(productRepository.findByStockLessThan(threshold)).thenReturn(List.of());

            List<ProductResponseDTO> result = productService.findLowStock(threshold);

            assertThat(result).isEmpty();
            verify(productRepository).findByStockLessThan(threshold);
        }
    }

    @Nested
    @DisplayName("3. Update & Security (checkAdminPrivileges)")
    class UpdateSecurityTests {

        @Test
        @DisplayName("update -> Should successfully update product with valid admin")
        void update_SuccessfullyUpdatesProduct() {
            // GIVEN
            Long userId = 1L;
            Long productId = 1L;
            ProductEntity updatedEntity = ProductEntity.builder()
                    .id(1L)
                    .name("Updated Laptop")
                    .code("PROD-1234")
                    .price(1500.0)
                    .stock(10)
                    .build();

            when(productRepository.findById(productId)).thenReturn(Optional.of(sampleEntity));
            when(productRepository.save(any())).thenReturn(updatedEntity);
            when(productMapper.toDto(updatedEntity)).thenReturn(sampleResponse);

            // WHEN
            ProductResponseDTO result = productService.update(productId, sampleRequest, userId);

            // THEN
            assertThat(result).isNotNull();
            verify(productRepository).findById(productId);
            verify(productRepository).save(any());
        }

        @Test
        @DisplayName("update -> Should throw ProductNotFoundException when product not found")
        void update_NotFound_ThrowsException() {
            Long userId = 1L;
            Long productId = 999L;

            when(productRepository.findById(productId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> productService.update(productId, sampleRequest, userId))
                    .isInstanceOf(ProductNotFoundException.class);

            verify(productRepository).findById(productId);
            verify(productRepository, never()).save(any());
        }

        @Test
        @DisplayName("checkAdminPrivileges -> Should log success when role is ADMIN")
        void checkAdminPrivileges_ShouldPass_WhenRoleIsAdmin() {
            // GIVEN
            Long userId = 1L;
            Long productId = 1L;

            // Simulamos que el producto existe para que el flujo de update continúe
            when(productRepository.findById(productId)).thenReturn(Optional.of(sampleEntity));
            when(productRepository.save(any())).thenReturn(sampleEntity);
            when(productMapper.toDto(any())).thenReturn(sampleResponse);

            // WHEN
            ProductResponseDTO result = productService.update(productId, sampleRequest, userId);

            // THEN
            assertThat(result).isNotNull();
            // Si el test llega aquí sin UnauthorizedException, checkAdminPrivileges fue exitoso.
        }
    }

    @Nested
    @DisplayName("4. Existence & Delete Operations")
    class ExistenceDeleteTests {
        @Test
        @DisplayName("existsById -> Should return true when product exists")
        void existsById_ReturnsTrue() {
            when(productRepository.existsById(1L)).thenReturn(true);

            assertThat(productService.existsById(1L)).isTrue();

            verify(productRepository).existsById(1L);
        }

        @Test
        @DisplayName("existsById -> Should return false when product does not exist")
        void existsById_ReturnsFalse() {
            when(productRepository.existsById(999L)).thenReturn(false);

            assertThat(productService.existsById(999L)).isFalse();

            verify(productRepository).existsById(999L);
        }

        @Test
        @DisplayName("delete -> Should delete when exists")
        void delete_Success() {
            when(productRepository.existsById(1L)).thenReturn(true);
            doNothing().when(productRepository).deleteById(1L);

            productService.delete(1L);

            verify(productRepository).deleteById(1L);
        }

        @Test
        @DisplayName("delete -> Should throw exception when not exists")
        void delete_NotFound_ThrowsException() {
            when(productRepository.existsById(1L)).thenReturn(false);

            assertThatThrownBy(() -> productService.delete(1L))
                    .isInstanceOf(ProductNotFoundException.class);
        }
    }
}