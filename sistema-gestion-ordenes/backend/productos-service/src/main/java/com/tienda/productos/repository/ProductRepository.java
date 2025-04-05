package com.tienda.productos.repository;

import com.tienda.productos.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByNombre(String nombre);
}
