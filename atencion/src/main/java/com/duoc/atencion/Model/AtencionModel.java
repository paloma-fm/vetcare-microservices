package com.duoc.atencion.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "atenciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtencionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAtencion;

    @Column(nullable = false)
    private Long idMascota;

    @Column(nullable = false)
    private Long idVeterinario;

    @Column(nullable = false)
    private LocalDate fechaAtencion;

    @Column(nullable = false, length = 100)
    private String motivo;

    @Column(nullable = false, length = 255)
    private String diagnostico;

    @Column(nullable = false, length = 255)
    private String tratamiento;

    @Column(length = 255)
    private String observaciones;

    @Column(nullable = false)
    private Boolean activo;
}