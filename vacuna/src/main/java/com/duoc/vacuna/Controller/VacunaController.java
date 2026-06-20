package com.duoc.vacuna.Controller;

import com.duoc.vacuna.Dto.VacunaDTO;
import com.duoc.vacuna.Model.VacunaModel;
import com.duoc.vacuna.Service.VacunaService;
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
@RequestMapping("/api/v1/vacunas")
@RequiredArgsConstructor
@Tag(name = "Gestión de Vacunas", description = "Registro y seguimiento del historial de vacunación de las mascotas")
public class VacunaController {

    private final VacunaService vacunaService;

    @GetMapping
    @Operation(summary = "Listar todas las vacunas", description = "Retorna la lista completa de vacunas registradas.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    public ResponseEntity<?> listarVacunas() {
        return ResponseEntity.ok(vacunaService.listarVacunas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener vacuna por ID", description = "Busca una vacuna específica por su identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vacuna encontrada"),
            @ApiResponse(responseCode = "404", description = "Vacuna no encontrada")
    })
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        VacunaModel vacuna = vacunaService.buscarPorId(id);
        return ResponseEntity.ok(vacuna);
    }

    @GetMapping("/mascota/{idMascota}")
    @Operation(summary = "Listar vacunas por mascota", description = "Retorna todas las vacunas asociadas a una mascota específica.")
    @ApiResponse(responseCode = "200", description = "Lista de vacunas de la mascota obtenida")
    public ResponseEntity<?> listarPorMascota(@PathVariable Long idMascota) {
        return ResponseEntity.ok(vacunaService.listarPorMascota(idMascota));
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Listar vacunas por estado", description = "Filtra vacunas por estado (ej: APLICADA, PENDIENTE).")
    @ApiResponse(responseCode = "200", description = "Lista filtrada por estado obtenida")
    public ResponseEntity<?> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(vacunaService.listarPorEstado(estado));
    }

    @PostMapping
    @Operation(summary = "Registrar una vacuna", description = "Crea un nuevo registro de vacunación. La fecha no puede ser futura.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vacuna registrada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o fecha futura")
    })
    public ResponseEntity<?> guardarVacuna(@Valid @RequestBody VacunaDTO vacunaDTO) {
        VacunaModel vacunaGuardada = vacunaService.guardarVacuna(vacunaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(vacunaGuardada);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una vacuna", description = "Modifica los datos de una vacuna existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vacuna actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Vacuna no encontrada")
    })
    public ResponseEntity<?> actualizarVacuna(@PathVariable Long id, @Valid @RequestBody VacunaDTO vacunaDTO) {
        VacunaModel vacunaActualizada = vacunaService.actualizarVacuna(id, vacunaDTO);
        return ResponseEntity.ok(vacunaActualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una vacuna", description = "Elimina físicamente un registro de vacuna.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Vacuna eliminada con éxito"),
            @ApiResponse(responseCode = "404", description = "Vacuna no encontrada")
    })
    public ResponseEntity<?> eliminarVacuna(@PathVariable Long id) {
        vacunaService.eliminarVacuna(id);
        return ResponseEntity.noContent().build();
    }
}
