package com.tienda.productos.controller;

import com.tienda.productos.model.Product;
import com.tienda.productos.service.ProductService;
import com.tienda.productos.dto.ProductDTO;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productos")
public class ProductController {

    private final ProductService productService;
    
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

     @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        Product product = productService.createProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) {
        Product updatedProduct = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(updatedProduct);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }


}
