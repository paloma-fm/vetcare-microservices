package com.vetcare.orquestador.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VacunaDTO {

    private Long idVacuna;
    private Long idMascota;
    private String nombreVacuna;
    private LocalDate fechaAplicacion;
    private LocalDate proximaDosis;
    private String estado;
    private String observaciones;
    private Boolean activo;
}