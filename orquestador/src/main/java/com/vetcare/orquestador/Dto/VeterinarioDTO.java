package com.vetcare.orquestador.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VeterinarioDTO {

    private Long idVeterinario;
    private String nombre;
    private String apellido;
    private String especialidad;
    private String correo;
    private String telefono;
    private Boolean activo;
}