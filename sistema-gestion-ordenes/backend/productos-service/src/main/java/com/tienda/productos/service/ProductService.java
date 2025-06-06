package com.tienda.productos.service;

import com.tienda.productos.dto.ProductDTO;
import com.tienda.productos.model.OrderItem;
import java.util.List;

public interface ProductService {
    
    ProductDTO createProduct(ProductDTO productDTO);
    
    ProductDTO updateProduct(Long id, ProductDTO productDTO);
    
    void deleteProduct(Long id);
    
    ProductDTO getProductById(Long id);
    
    List<ProductDTO> searchProducts(String palabra_clave);
    
    List<ProductDTO> getAllProducts();
    
    void actualizarStock(Long id, Integer cantidad);
    
    void validarStock(List<OrderItem> items);
}
