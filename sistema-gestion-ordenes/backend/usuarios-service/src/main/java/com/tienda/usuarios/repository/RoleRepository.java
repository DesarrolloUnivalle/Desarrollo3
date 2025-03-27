package com.tienda.usuarios.repository;

import com.tienda.usuarios.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query("SELECT r FROM Role r WHERE LOWER(TRIM(r.nombre)) = LOWER(TRIM(:nombre))")
    Optional<Role> findByNombre(@Param("nombre") String nombre);

    @Query("SELECT r FROM Role r")  // 🔥 Asegurar que siempre devuelva datos
    List<Role> findAllRoles();
}
