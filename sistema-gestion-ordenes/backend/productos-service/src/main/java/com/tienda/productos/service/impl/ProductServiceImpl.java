package com.tienda.productos.service.impl;

import com.tienda.productos.dto.ProductDTO;
import com.tienda.productos.model.Categoria;
import com.tienda.productos.model.Product;
import com.tienda.productos.repository.CategoriaRepository;
import com.tienda.productos.repository.ProductRepository;
import com.tienda.productos.service.ProductService;

import jakarta.transaction.Transactional;


import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoriaRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoriaRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }
    
    @Transactional
    @Override
    public Product createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setNombre(productDTO.getNombre());
        product.setDescripcion(productDTO.getDescripcion());
        product.setPrecio(productDTO.getPrecio());
        product.setStock(productDTO.getStock());

        // Buscar la categoría en la BD y asignarla
        Categoria category = categoryRepository.findById(productDTO.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        product.setCategoria(category);

        return productRepository.save(product);
    }
}
