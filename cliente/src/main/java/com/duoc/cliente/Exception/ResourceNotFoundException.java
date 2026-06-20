package com.duoc.cliente.Exception;

// Hereda de RuntimeException para que Spring la pueda capturar sin obligarte a usar bloques try-catch ruidosos
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }
}