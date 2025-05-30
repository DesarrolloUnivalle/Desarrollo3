package com.tienda.productos.controller;

import com.tienda.productos.dto.ProductDTO;
import com.tienda.productos.model.OrderItem;
import com.tienda.productos.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.createProduct(productDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.updateProduct(id, productDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam(required = false) String query) {
        return ResponseEntity.ok(productService.searchProducts(query));
    }
    @GetMapping("/all")
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping("/validar-stock")
    public ResponseEntity<Void> validarStock(@RequestBody List<OrderItem> items) {
        logger.info("Recibida petición para validar stock: {}", items);
        try {
            productService.validarStock(items);
            logger.info("Validación de stock exitosa");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error al validar stock: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/{productoId}/stock")
    public ResponseEntity<Void> actualizarStock(@PathVariable Long productoId, @RequestParam Integer cantidad) {
        logger.info("Recibida petición para actualizar stock. Producto: {}, Cantidad: {}", productoId, cantidad);
        try {
            productService.actualizarStock(productoId, cantidad);
            logger.info("Actualización de stock exitosa");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error al actualizar stock: {}", e.getMessage());
            throw e;
        }
    }
}
