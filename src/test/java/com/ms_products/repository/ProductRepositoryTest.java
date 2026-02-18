package com.ms_products.repository;

import com.ms_products.entity.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @Mock
    private ProductRepository productRepository;

    private List<ProductEntity> productEntities = new ArrayList<>();

    // simulating database data
    @BeforeEach
    void setUp() {
        productEntities.add(ProductEntity.builder()
                .name("XBOX")
                .code("XBOX")
                .id(1L)
                .price(55.33)
                .stock(5)
                .build());
        productEntities.add(ProductEntity.builder()
                .name("PLAY")
                .code("PLAY")
                .stock(5)
                .price(44.33)
                .id(2L)
                .build());
    }

    @ParameterizedTest(name = "Value to test {0}")
    @ValueSource(strings = {"XBOX", "PLAY", "NIENTIENDO"})
    void findByCode(String value){
//        Mockito.when(productRepository.findByCode(value)).thenReturn(Optional.of(opproductEntities));
        Optional<ProductEntity> byCode = this.productRepository.findByCode(value);
        assertTrue(byCode.isPresent());
    }

}