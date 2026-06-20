package com.duoc.cliente.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name = "clientes")
public class ClienteModel {


    @Id
    @GeneratedValue (strategy =  GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String correo;
    private String telefono;
    @Column(nullable = false)
    private boolean activo = true;
}
