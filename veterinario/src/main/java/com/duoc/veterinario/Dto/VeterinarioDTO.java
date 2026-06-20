package com.duoc.veterinario.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VeterinarioDTO {

    private Long idVeterinario;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede superar los 50 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 50, message = "El apellido no puede superar los 50 caracteres")
    private String apellido;

    @NotBlank(message = "La especialidad es obligatoria")
    @Size(max = 80, message = "La especialidad no puede superar los 80 caracteres")
    private String especialidad;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener un formato válido")
    @Size(max = 100, message = "El correo no puede superar los 100 caracteres")
    private String correo;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 15, message = "El teléfono no puede superar los 15 caracteres")
    private String telefono;

    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo;
}