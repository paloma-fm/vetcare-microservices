package com.duoc.mascota.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name= "mascota")
public class MascotaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (nullable = false,length = 30)
    private String nombre;
    @Column (nullable = false,length = 30)
    private String especie;
    @Column (length = 50)
    private String raza;
    @Column (nullable = false)
    private Integer edad;

    @Column(name="cliente_id",nullable = false)
    private Long clienteId;
    @Column(nullable = false)
    private boolean activo = true;
}
