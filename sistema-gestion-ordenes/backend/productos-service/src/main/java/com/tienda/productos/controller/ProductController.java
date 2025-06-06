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
        try {
            logger.info("Creando nuevo producto: {}", productDTO);
            return ResponseEntity.ok(productService.createProduct(productDTO));
        } catch (Exception e) {
            logger.error("Error al crear producto: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) {
        try {
            logger.info("Actualizando producto con id {}: {}", id, productDTO);
            return ResponseEntity.ok(productService.updateProduct(id, productDTO));
        } catch (Exception e) {
            logger.error("Error al actualizar producto: {}", e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            logger.info("Eliminando producto con id: {}", id);
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error al eliminar producto: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        try {
            logger.info("Obteniendo producto con id: {}", id);
            return ResponseEntity.ok(productService.getProductById(id));
        } catch (Exception e) {
            logger.error("Error al obtener producto: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/buscar/{palabra_clave}")
    public ResponseEntity<List<ProductDTO>> searchProducts(@PathVariable String palabra_clave) {
        try {
            logger.info("Buscando productos con palabra clave: {}", palabra_clave);
            return ResponseEntity.ok(productService.searchProducts(palabra_clave));
        } catch (Exception e) {
            logger.error("Error al buscar productos: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        try {
            logger.info("Obteniendo todos los productos");
            return ResponseEntity.ok(productService.getAllProducts());
        } catch (Exception e) {
            logger.error("Error al obtener todos los productos: {}", e.getMessage());
            throw e;
        }
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
