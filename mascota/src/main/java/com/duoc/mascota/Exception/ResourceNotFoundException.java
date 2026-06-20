package com.duoc.mascota.Exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException (String mensaje){
        super(mensaje);
    }
}
