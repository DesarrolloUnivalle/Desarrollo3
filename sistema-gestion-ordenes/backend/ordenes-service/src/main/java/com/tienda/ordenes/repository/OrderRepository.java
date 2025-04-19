package com.tienda.ordenes.repository;

import com.tienda.ordenes.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUsuarioId(String usuarioId);
}
