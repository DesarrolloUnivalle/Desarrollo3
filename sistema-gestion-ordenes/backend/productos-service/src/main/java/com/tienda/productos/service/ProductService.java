package com.tienda.productos.service;

import com.tienda.productos.dto.ProductDTO;
import com.tienda.productos.model.Product;

public interface ProductService {
    Product createProduct(ProductDTO productDTO);
        
    
}
