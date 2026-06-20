package com.duoc.cliente.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
public class ErrorDTO {
    private LocalDateTime timestamp;
    private int codigo;
    private String error;
    private String mensaje;
    private Map<String, String> detalles; // Aquí guardaremos qué campos fallaron específicamente
}