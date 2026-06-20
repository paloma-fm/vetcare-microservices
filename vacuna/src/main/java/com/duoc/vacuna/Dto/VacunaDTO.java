package com.duoc.vacuna.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class VacunaDTO {

    private Long idVacuna;

    @NotNull(message = "El ID de la mascota es obligatorio")
    private Long idMascota;

    @NotBlank(message = "El nombre de la vacuna es obligatorio")
    @Size(max = 100, message = "El nombre de la vacuna no puede superar los 100 caracteres")
    private String nombreVacuna;

    @NotNull(message = "La fecha de aplicación es obligatoria")
    private LocalDate fechaAplicacion;

    private LocalDate proximaDosis;

    @NotBlank(message = "El estado de la vacuna es obligatorio")
    @Size(max = 30, message = "El estado no puede superar los 30 caracteres")
    private String estado;

    @Size(max = 255, message = "Las observaciones no pueden superar los 255 caracteres")
    private String observaciones;

    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo;
}