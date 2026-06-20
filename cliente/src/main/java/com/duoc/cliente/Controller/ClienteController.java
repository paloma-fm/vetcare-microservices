package com.duoc.cliente.Controller;

import com.duoc.cliente.Dto.ClienteDTO;
import com.duoc.cliente.Service.ClienteService;
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
@RequestMapping("/api/v1/clientes")
@Tag(name = "Gestión de Clientes", description = "Operaciones CRUD para los dueños de mascotas")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo cliente", description = "Crea un cliente en el sistema, iniciando en estado activo por defecto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos de entrada")
    })
    public ResponseEntity<ClienteDTO> guardar(@Valid @RequestBody ClienteDTO clienteDTO) {
        ClienteDTO nuevoCliente = clienteService.guardar(clienteDTO);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un cliente por ID", description = "Busca un cliente activo. Retorna 404 si fue dado de baja lógicamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado o dado de baja")
    })
    public ResponseEntity<ClienteDTO> obtenerClientes(@PathVariable Long id) {
        ClienteDTO clienteDTO = clienteService.buscarPorId(id);
        return new ResponseEntity<>(clienteDTO, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Listar todos los clientes activos", description = "Retorna la lista de clientes con activo = true.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida con éxito")
    public ResponseEntity<List<ClienteDTO>> listarTodos() {
        List<ClienteDTO> listaClientes = clienteService.listarClientes();
        return new ResponseEntity<>(listaClientes, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos de un cliente", description = "Modifica la información de un cliente existente y activo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado o inactivo")
    })
    public ResponseEntity<ClienteDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ClienteDTO clienteDTO) {
        ClienteDTO clienteActualizado = clienteService.actualizar(id, clienteDTO);
        return new ResponseEntity<>(clienteActualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Dar de baja un cliente", description = "Borrado lógico: cambia activo = false, sin eliminar el registro de la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente desactivado con éxito"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado o ya inactivo")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteService.eliminarLogico(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
