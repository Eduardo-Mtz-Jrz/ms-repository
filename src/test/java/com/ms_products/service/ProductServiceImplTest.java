package com.ms_products.service;

import com.ms_products.dto.ProductRequestDTO;
import com.ms_products.dto.ProductResponseDTO;
import com.ms_products.entity.ProductEntity;
import com.ms_products.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    private List<ProductEntity> productEntities = new ArrayList<>();

    private ProductRequestDTO productRequestDTO;

    // simulating database data
    @BeforeEach
    void setUp() {
        productEntities.add(ProductEntity.builder()
                .name("XBOX")
                .code("XBOX")
                .id(1L)
                .price(55.33)
                        .category("Ent")
                .stock(5)
                .build());
        productEntities.add(ProductEntity.builder()
                .name("PLAY")
                .code("PLAY")
                        .category("Ent")
                .stock(5)
                .price(44.33)
                .id(2L)
                .build());

        this.productRequestDTO = ProductRequestDTO.builder()
                .name("PLAY")
                .code("PLAY")
                .category("Ent")
                .stock(5)
                .price(44.33)
                .build();
    }

    @Test
    void saveProductTest() {
//        Mockito.when(productRepository.findByCode(Mockito.any(String.class)))
//                .then(Optional.of(productEntities.get(1)));
//
//        ProductResponseDTO save = this.productServiceImpl.save(this.productRequestDTO);
    }

    @Test
    void findAllTest(){
        Mockito.when(productRepository.findAll()).thenReturn(productEntities);
        List<ProductResponseDTO> list = this.productServiceImpl.findAll();
        assertNotNull(list);
        assertFalse(list.isEmpty());

    }
}