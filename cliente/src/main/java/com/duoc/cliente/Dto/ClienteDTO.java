package com.duoc.cliente.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO {

    @Schema(hidden = true)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio y no puede estar vacío.")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres.")
    private String nombre;

    @NotBlank(message = "El correo es obligatorio.")
    @Email(message = "El formato del correo electrónico no es válido.")
    private String correo;

    @NotBlank(message = "El teléfono es obligatorio.")
    @Size(min = 9, max = 12, message = "El teléfono debe tener entre 9 y 12 dígitos.")
    private String telefono;
}