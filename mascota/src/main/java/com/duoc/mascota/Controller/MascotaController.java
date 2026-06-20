package com.duoc.mascota.Controller;

import com.duoc.mascota.Dto.MascotaDTO;
import com.duoc.mascota.Service.MascotaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mascotas")
@Tag(name = "Gestión de Mascotas", description = "Operaciones CRUD para los pacientes de la veterinaria")
public class MascotaController {

    private final MascotaService mascotaService;

    public MascotaController(MascotaService mascotaService) {
        this.mascotaService = mascotaService;
    }

    @PostMapping
    @Operation(summary = "Registrar una nueva mascota", description = "Crea una mascota asociada a un cliente. Nace con activo = true.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Mascota creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados")
    })
    public ResponseEntity<MascotaDTO> guardar(@Valid @RequestBody MascotaDTO mascotaDTO) {
        MascotaDTO nuevaMascota = mascotaService.guardar(mascotaDTO);
        return new ResponseEntity<>(nuevaMascota, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una mascota por ID", description = "Busca una mascota activa. Retorna 404 si fue dada de baja.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mascota encontrada"),
            @ApiResponse(responseCode = "404", description = "Mascota no encontrada o dada de baja")
    })
    public ResponseEntity<MascotaDTO> obtenerMascota(@PathVariable Long id) {
        MascotaDTO mascotaDTO = mascotaService.buscarPorId(id);
        return new ResponseEntity<>(mascotaDTO, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Listar todas las mascotas activas", description = "Retorna la lista de mascotas con activo = true.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida con éxito")
    public ResponseEntity<List<MascotaDTO>> listarTodas() {
        List<MascotaDTO> listaMascotas = mascotaService.listarMascotas();
        return new ResponseEntity<>(listaMascotas, HttpStatus.OK);
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar mascotas de un cliente", description = "Retorna todas las mascotas activas asociadas al clienteId indicado.")
    @ApiResponse(responseCode = "200", description = "Lista de mascotas del cliente obtenida")
    public ResponseEntity<List<MascotaDTO>> listarPorCliente(@PathVariable Long clienteId) {
        List<MascotaDTO> listaMascotas = mascotaService.listarPorCliente(clienteId);
        return new ResponseEntity<>(listaMascotas, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos de una mascota", description = "Modifica la información de una mascota activa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mascota actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Mascota no encontrada")
    })
    public ResponseEntity<MascotaDTO> actualizar(@PathVariable Long id, @Valid @RequestBody MascotaDTO mascotaDTO) {
        MascotaDTO mascotaActualizada = mascotaService.actualizar(id, mascotaDTO);
        return new ResponseEntity<>(mascotaActualizada, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Dar de baja una mascota", description = "Borrado lógico: cambia activo = false sin eliminar el registro.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Mascota dada de baja con éxito"),
            @ApiResponse(responseCode = "404", description = "Mascota no encontrada o ya inactiva")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        mascotaService.eliminarLogico(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
