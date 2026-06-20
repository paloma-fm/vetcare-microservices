package com.duoc.atencion.Controller;

import com.duoc.atencion.Dto.AtencionDTO;
import com.duoc.atencion.Model.AtencionModel;
import com.duoc.atencion.Service.AtencionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/atenciones")
@RequiredArgsConstructor
@Tag(name = "Gestión de Atenciones", description = "Registro de las consultas y tratamientos clínicos realizados a las mascotas")
public class AtencionController {

    private final AtencionService atencionService;

    @GetMapping
    @Operation(summary = "Listar todas las atenciones", description = "Retorna la lista completa de atenciones clínicas registradas.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    public ResponseEntity<?> listarAtenciones() {
        return ResponseEntity.ok(atencionService.listarAtenciones());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener atención por ID", description = "Busca una atención clínica específica por su identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atención encontrada"),
            @ApiResponse(responseCode = "404", description = "Atención no encontrada")
    })
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        AtencionModel atencion = atencionService.buscarPorId(id);
        return ResponseEntity.ok(atencion);
    }

    @GetMapping("/mascota/{idMascota}")
    @Operation(summary = "Listar atenciones por mascota", description = "Retorna todas las atenciones registradas para una mascota específica.")
    @ApiResponse(responseCode = "200", description = "Lista de atenciones de la mascota obtenida")
    public ResponseEntity<?> listarPorMascota(@PathVariable Long idMascota) {
        return ResponseEntity.ok(atencionService.listarPorMascota(idMascota));
    }

    @GetMapping("/veterinario/{idVeterinario}")
    @Operation(summary = "Listar atenciones por veterinario", description = "Retorna todas las atenciones realizadas por un veterinario específico.")
    @ApiResponse(responseCode = "200", description = "Lista de atenciones del veterinario obtenida")
    public ResponseEntity<?> listarPorVeterinario(@PathVariable Long idVeterinario) {
        return ResponseEntity.ok(atencionService.listarPorVeterinario(idVeterinario));
    }

    @PostMapping
    @Operation(summary = "Registrar una atención", description = "Crea un nuevo registro de atención clínica. La fecha no puede ser futura.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Atención registrada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o fecha futura")
    })
    public ResponseEntity<?> guardarAtencion(@Valid @RequestBody AtencionDTO atencionDTO) {
        AtencionModel atencionGuardada = atencionService.guardarAtencion(atencionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(atencionGuardada);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una atención", description = "Modifica los datos de una atención clínica existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atención actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Atención no encontrada")
    })
    public ResponseEntity<?> actualizarAtencion(@PathVariable Long id, @Valid @RequestBody AtencionDTO atencionDTO) {
        AtencionModel atencionActualizada = atencionService.actualizarAtencion(id, atencionDTO);
        return ResponseEntity.ok(atencionActualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una atención", description = "Elimina físicamente un registro de atención.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Atención eliminada con éxito"),
            @ApiResponse(responseCode = "404", description = "Atención no encontrada")
    })
    public ResponseEntity<?> eliminarAtencion(@PathVariable Long id) {
        atencionService.eliminarAtencion(id);
        return ResponseEntity.noContent().build();
    }
}
