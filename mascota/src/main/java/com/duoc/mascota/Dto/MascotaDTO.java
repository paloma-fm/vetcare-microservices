package com.duoc.mascota.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MascotaDTO {

    @Schema(hidden = true)
    private Long id;

    @NotBlank(message = "El nombre de la mascota es obligatorio y no puede estar vacío.")
    @Size(min = 2, max = 30, message = "El nombre debe tener entre 2 y 30 caracteres.")
    private String nombre;

    @NotBlank(message = "La especie es obligatoria (ejemplo: Perro, Gato).")
    @Size(min = 3, max = 20, message = "La especie debe tener entre 3 y 20 caracteres.")
    private String especie;

    @Size(max = 30, message = "La raza no puede superar los 30 caracteres.")
    private String raza;

    @Min(value = 0, message = "La edad no puede ser un número negativo.")
    private int edad;

    @NotNull(message = "El ID del cliente asociado es obligatorio.")
    private Long clienteId;
}
