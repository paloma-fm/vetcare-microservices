package com.duoc.atencion.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AtencionDTO {

    private Long idAtencion;

    @NotNull(message = "El ID de la mascota es obligatorio")
    private Long idMascota;

    @NotNull(message = "El ID del veterinario es obligatorio")
    private Long idVeterinario;

    @NotNull(message = "La fecha de atención es obligatoria")
    private LocalDate fechaAtencion;

    @NotBlank(message = "El motivo es obligatorio")
    @Size(max = 100, message = "El motivo no puede superar los 100 caracteres")
    private String motivo;

    @NotBlank(message = "El diagnóstico es obligatorio")
    @Size(max = 255, message = "El diagnóstico no puede superar los 255 caracteres")
    private String diagnostico;

    @NotBlank(message = "El tratamiento es obligatorio")
    @Size(max = 255, message = "El tratamiento no puede superar los 255 caracteres")
    private String tratamiento;

    @Size(max = 255, message = "Las observaciones no pueden superar los 255 caracteres")
    private String observaciones;

    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo;
}