package com.ms_products.ms_products.service;

import com.ms_products.ms_products.entity.ProductEntity;

import java.util.List;

public interface ProductService {

    ProductEntity agregar(ProductEntity product);

    ProductEntity modificar(Long id, ProductEntity product);

    void eliminar(Long id);

    ProductEntity obtenerPorId(Long id);

    List<ProductEntity> listar();
}
