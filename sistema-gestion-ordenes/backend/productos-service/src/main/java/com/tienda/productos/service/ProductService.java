package com.tienda.productos.service;

import java.util.List;

import com.tienda.productos.dto.ProductDTO;
import com.tienda.productos.model.Product;

public interface ProductService {
    Product createProduct(ProductDTO productDTO);
    Product updateProduct(Long id, ProductDTO productDTO);
    void deleteProduct(Long id);
    Product getProductById(Long id);
    List<Product> searchProducts(String keyword);
    List<Product> getAllProducts();

        
    
    
}
