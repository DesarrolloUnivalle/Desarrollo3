package com.tienda.productos.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
@Getter
@Setter
@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @OneToMany(mappedBy = "categoria")
    @JsonManagedReference // Controla la relación
    private List<Product> productos;

    // Constructor vacío
    public Categoria() {}

    // Constructor con parámetros
    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    
}
