package com.tienda.productos.controller;

import com.tienda.productos.model.Product;
import com.tienda.productos.service.ProductService;
import com.tienda.productos.dto.ProductDTO;
import com.tienda.ordenes.model.OrderItem;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos")
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

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/buscar/{palabra_clave}")
    public ResponseEntity<List<Product>> searchProducts(@PathVariable String palabra_clave) {
        List<Product> products = productService.searchProducts(palabra_clave);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Product> actualizarStock(@PathVariable Long id, @RequestParam Integer cantidad) {
        Product updatedProduct = productService.actualizarStock(id, cantidad);
        return ResponseEntity.ok(updatedProduct);
    }

    @PostMapping("/validar-stock")
    public ResponseEntity<Void> validarStock(@RequestBody List<OrderItem> items) {
        for (OrderItem item : items) {
            Product product = productService.getProductById(item.getProductoId());
            if (product.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + product.getNombre());
            }
        }
        return ResponseEntity.ok().build();
    }
}
