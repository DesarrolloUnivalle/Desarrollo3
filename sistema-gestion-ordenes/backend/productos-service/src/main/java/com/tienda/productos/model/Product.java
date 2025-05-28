package com.tienda.productos.model;




import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
    
    
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    @JsonBackReference // Evita la recursión infinita
    private Categoria categoria;
}
