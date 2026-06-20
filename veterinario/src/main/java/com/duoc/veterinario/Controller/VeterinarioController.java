package com.duoc.veterinario.Controller;

import com.duoc.veterinario.Dto.VeterinarioDTO;
import com.duoc.veterinario.Model.VeterinarioModel;
import com.duoc.veterinario.Service.VeterinarioService;
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
@RequestMapping("/api/v1/veterinarios")
@RequiredArgsConstructor
@Tag(name = "Gestión de Veterinarios", description = "Operaciones CRUD para el personal médico de la clínica")
public class VeterinarioController {

    private final VeterinarioService veterinarioService;

    @GetMapping
    @Operation(summary = "Listar todos los veterinarios", description = "Retorna la lista completa de veterinarios registrados.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    public ResponseEntity<?> listarVeterinarios() {
        return ResponseEntity.ok(veterinarioService.listarVeterinarios());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener veterinario por ID", description = "Busca un veterinario específico por su identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veterinario encontrado"),
            @ApiResponse(responseCode = "404", description = "Veterinario no encontrado")
    })
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        VeterinarioModel veterinario = veterinarioService.buscarPorId(id);
        return ResponseEntity.ok(veterinario);
    }

    @PostMapping
    @Operation(summary = "Registrar un veterinario", description = "Crea un nuevo veterinario. El correo debe ser único en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Veterinario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o correo duplicado")
    })
    public ResponseEntity<?> guardarVeterinario(@Valid @RequestBody VeterinarioDTO veterinarioDTO) {
        VeterinarioModel veterinarioGuardado = veterinarioService.guardarVeterinario(veterinarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(veterinarioGuardado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos de un veterinario", description = "Modifica la información de un veterinario existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veterinario actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Veterinario no encontrado")
    })
    public ResponseEntity<?> actualizarVeterinario(@PathVariable Long id, @Valid @RequestBody VeterinarioDTO veterinarioDTO) {
        VeterinarioModel veterinarioActualizado = veterinarioService.actualizarVeterinario(id, veterinarioDTO);
        return ResponseEntity.ok(veterinarioActualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un veterinario", description = "Elimina físicamente un veterinario de la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Veterinario eliminado con éxito"),
            @ApiResponse(responseCode = "404", description = "Veterinario no encontrado")
    })
    public ResponseEntity<?> eliminarVeterinario(@PathVariable Long id) {
        veterinarioService.eliminarVeterinario(id);
        return ResponseEntity.noContent().build();
    }
}
