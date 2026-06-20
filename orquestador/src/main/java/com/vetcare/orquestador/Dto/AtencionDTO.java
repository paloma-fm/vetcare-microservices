package com.vetcare.orquestador.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtencionDTO {

    private Long idAtencion;
    private Long idMascota;
    private Long idVeterinario;
    private LocalDate fechaAtencion;
    private String motivo;
    private String diagnostico;
    private String tratamiento;
    private String observaciones;
    private Boolean activo;

    private VeterinarioDTO veterinario;
}