package com.ms_products.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProductRequestDTOTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @NullSource
    @ParameterizedTest(name = "value To Test '{0}'")
    @ValueSource(strings = {"", " ", "David"})
    void nameNotBlank(String value){
        ProductRequestDTO productdto = ProductRequestDTO.builder()
                .name(value)
                .code("afd")
                .category("fdf")
                .price(45.33)
                .stock(5)
                .build();

        Set<ConstraintViolation<ProductRequestDTO>> validate = validator.validate(productdto);
        // General validation of the name with @NotBlank
        assertTrue(validate.isEmpty());

        // error message validation
        assertFalse(validate.stream().anyMatch(v -> v.getMessage().equals("Product name must not be empty"))
                , "Should have found the specific error message for input: " + value);
    }

    @NullSource
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "XBOX-ONE"})
    void codeNotNullTest(String value){
        ProductRequestDTO productdto = ProductRequestDTO.builder()
                .name("fasdfad")
                .category("fasdf")
                .category("sdfsdfsd")
                .price(45.33)
                .stock(5)
                .code(value)
                .build();

        Set<ConstraintViolation<ProductRequestDTO>> validate = validator.validate(productdto);
        // General validation of the code with @NotBlank
        assertTrue(validate.isEmpty());

        // error message validation
        assertFalse(validate.stream().anyMatch(v -> v.getMessage().equals("KPI value is required"))
                , "Should have found the specific error message for input: " + value);
    }

    @NullSource
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "entertainment"})
    void categoryNotNullTest(String value){
        ProductRequestDTO productdto = ProductRequestDTO.builder()
                .category(value)
                .name("dsfsdf")
                .code("sdafasdf")
                .stock(5)
                .price(45.33)
                .build();

        Set<ConstraintViolation<ProductRequestDTO>> validate = validator.validate(productdto);
        // General validation of the category with @NotBlank
        assertTrue(validate.isEmpty());

        // error message validation
        assertFalse(validate.stream().anyMatch(v -> v.getMessage().equals("Category cannot be null")));
    }

    @NullSource
    @ParameterizedTest
    @ValueSource(doubles = {-45.54D,54.54})
    void pricePositiveTest(Double value){
        ProductRequestDTO productdto = ProductRequestDTO.builder()
                .price(value)
                .name("dsfsdf")
                .code("sdafasdf")
                .stock(5)
                .category("fasdf")
                .build();

        Set<ConstraintViolation<ProductRequestDTO>> validate = validator.validate(productdto);
        // General validation of the name with @NotBlank
        assertTrue(validate.isEmpty());

        // error message validation
        assertFalse(validate.stream().anyMatch(v -> v.getMessage().equals("Price must be greater than zero")));
    }

    @NullSource
    @ParameterizedTest
    @ValueSource(ints = {1,5,45,-84})
    void stockMinZerotest(Integer value){
        ProductRequestDTO productdto = ProductRequestDTO.builder()
                .stock(value)
                .category("fasdf")
                .name("dsfsdf")
                .price(45.33)
                .code("asdasd")
                .build();

        Set<ConstraintViolation<ProductRequestDTO>> validate = validator.validate(productdto);
        // General validation of the name with @NotBlank
        assertTrue(validate.isEmpty());

        // error message validation
        assertFalse(validate.stream().anyMatch(v -> v.getMessage().equals("Price must be greater than zero")));
    }

}