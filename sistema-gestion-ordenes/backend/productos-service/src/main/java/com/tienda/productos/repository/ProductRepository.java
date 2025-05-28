package com.tienda.productos.repository;

import com.tienda.productos.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByNombre(String nombre);
    Optional<List<Product>> findByNombreContainingIgnoreCase(String nombre);
}
