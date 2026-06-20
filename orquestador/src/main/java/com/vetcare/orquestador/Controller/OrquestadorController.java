package com.vetcare.orquestador.Controller;

import com.vetcare.orquestador.Dto.FichaMedicaIntegralDTO;
import com.vetcare.orquestador.Service.OrquestadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/consultas")
@RequiredArgsConstructor
@Tag(name = "Orquestador Clínico", description = "Consolida información de múltiples microservicios en una sola respuesta")
public class OrquestadorController {

    private final OrquestadorService orquestadorService;

    @GetMapping("/ficha-completa/{clienteId}")
    @Operation(
            summary = "Obtener Ficha Médica Integral",
            description = "Llama a los microservicios de Cliente, Mascota, Atencion, Vacuna y Veterinario para retornar un JSON unificado con el historial clínico completo del dueño."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ficha médica generada exitosamente"),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido o ausente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado o inactivo")
    })
    public ResponseEntity<FichaMedicaIntegralDTO> obtenerFichaCompleta(
            @Parameter(description = "ID del cliente dueño de las mascotas", example = "1")
            @PathVariable Long clienteId) {

        FichaMedicaIntegralDTO ficha = orquestadorService.obtenerFichaCompleta(clienteId);
        return new ResponseEntity<>(ficha, HttpStatus.OK);
    }
}
