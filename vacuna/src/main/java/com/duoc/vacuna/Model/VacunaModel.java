package com.duoc.vacuna.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "vacunas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacunaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVacuna;

    @Column(nullable = false)
    private Long idMascota;

    @Column(nullable = false, length = 100)
    private String nombreVacuna;

    @Column(nullable = false)
    private LocalDate fechaAplicacion;

    @Column
    private LocalDate proximaDosis;

    @Column(nullable = false, length = 30)
    private String estado;

    @Column(length = 255)
    private String observaciones;

    @Column(nullable = false)
    private Boolean activo;
}