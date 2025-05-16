package com.tienda.entregas.repository;

import com.tienda.entregas.model.entity.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Long> {

    List<Entrega> findByRepartidorId(Long repartidorId);

    List<Entrega> findByOrdenId(Long ordenId);
}