package com.duoc.auth.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Credenciales para el inicio de sesión")
public class LoginRequest {

    @Schema(example = "juan.perez@correo.com", description = "Nombre de usuario o correo electrónico")
    private String username;

    @Schema(example = "123456", description = "Contraseña del usuario")
    private String password;
}