package com.duoc.mascota;

import com.duoc.mascota.Dto.ErrorDTO;
import com.duoc.mascota.Exception.GlobalExceptionHandler;
import com.duoc.mascota.Exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("handleNotFoundException debe retornar 404 con el mensaje de la excepción")
    void handleNotFoundException_deberiaRetornar404ConMensaje() {
        // GIVEN: una excepción de recurso no encontrado
        ResourceNotFoundException excepcion = new ResourceNotFoundException("Mascota no encontrada con el ID: 99");

        // WHEN: el handler procesa la excepción
        ResponseEntity<ErrorDTO> respuesta = handler.handleNotFoundException(excepcion);

        // THEN: retorna 404 con el mensaje correcto en el cuerpo
        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        assertEquals(404, respuesta.getBody().getCodigo());
        assertEquals("Mascota no encontrada con el ID: 99", respuesta.getBody().getMensaje());
    }
}
