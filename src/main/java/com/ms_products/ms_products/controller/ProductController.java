package com.ms_products.ms_products.controller;

import com.ms_products.ms_products.entity.ProductEntity;
import com.ms_products.ms_products.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // AGREGAR
    @PostMapping
    public ProductEntity agregar(@Valid @RequestBody ProductEntity product) {
        return productService.agregar(product);
    }

    // MODIFICAR
    @PutMapping("/{id}")
    public ProductEntity modificar(@PathVariable Long id,
                                   @Valid @RequestBody ProductEntity product) {
        return productService.modificar(id, product);
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        productService.eliminar(id);
    }

    // CONSULTAR POR ID
    @GetMapping("/{id}")
    public ProductEntity obtenerPorId(@PathVariable Long id) {
        return productService.obtenerPorId(id);
    }

    // LISTAR TODOS
    @GetMapping
    public List<ProductEntity> listar() {
        return productService.listar();
    }
}
