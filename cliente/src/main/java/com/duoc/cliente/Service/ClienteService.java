package com.duoc.cliente.Service;

import com.duoc.cliente.Dto.ClienteDTO;
import com.duoc.cliente.Exception.ResourceNotFoundException;
import com.duoc.cliente.Model.ClienteModel;
import com.duoc.cliente.Repository.ClienteRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private static final Logger log = LoggerFactory.getLogger(ClienteService.class);
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository){
        this.clienteRepository = clienteRepository;
    }

    public ClienteDTO guardar(ClienteDTO dto){
        log.info("Registrando nuevo cliente: {}", dto.getNombre());

        // Se agrega 'true' al final para que el cliente nazca activo
        ClienteModel cliente = new ClienteModel(null, dto.getNombre(), dto.getCorreo(), dto.getTelefono(), true);
        ClienteModel guardado = clienteRepository.save(cliente);
        log.info("Cliente guardado con ID: {}", guardado.getId());

        return new ClienteDTO(guardado.getId(), guardado.getNombre(), guardado.getCorreo(), guardado.getTelefono());
    }

    public ClienteDTO buscarPorId(Long id) {
        log.info("Buscando cliente activo con ID: {}", id);

        // Cambiado para usar el método del repositorio que verifica si sigue activo
        ClienteModel cliente = clienteRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado o inactivo con el ID: " + id));

        return new ClienteDTO(cliente.getId(), cliente.getNombre(), cliente.getCorreo(), cliente.getTelefono());
    }

    public List<ClienteDTO> listarClientes(){
        log.info("Listando todos los clientes vigentes");

        List<ClienteModel> clientes = clienteRepository.findAll();
        return clientes.stream()
                .filter(ClienteModel::isActivo) // Filtramos solo los clientes que tengan activo == true
                .map(cliente -> new ClienteDTO(cliente.getId(), cliente.getNombre(), cliente.getCorreo(), cliente.getTelefono()))
                .collect(Collectors.toList());
    }

    public ClienteDTO actualizar(Long id, ClienteDTO dto) {
        log.info("Actualizando datos del cliente con ID: {}", id);

        // Verificamos que exista y esté activo
        ClienteModel clienteExistente = clienteRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se puede actualizar. Cliente no encontrado o dado de baja con ID: " + id));

        // Pisamos los datos antiguos con los nuevos recibidos desde el DTO
        clienteExistente.setNombre(dto.getNombre());
        clienteExistente.setCorreo(dto.getCorreo());
        clienteExistente.setTelefono(dto.getTelefono());
        // El campo 'activo' se mantiene en true

        ClienteModel actualizado = clienteRepository.save(clienteExistente);
        log.info("Cliente ID: {} actualizado con éxito", actualizado.getId());

        return new ClienteDTO(actualizado.getId(), actualizado.getNombre(), actualizado.getCorreo(), actualizado.getTelefono());
    }


    public void eliminarLogico(Long id) {
        log.info("Aplicando borrado lógico al cliente con ID: {}", id);

        // Verificamos que exista y no haya sido borrado antes
        ClienteModel cliente = clienteRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se puede eliminar. Cliente no encontrado o ya inactivo con ID: " + id));

        // Desactivamos el cliente cambiando el switch
        cliente.setActivo(false);
        clienteRepository.save(cliente);

        log.info("Cliente ID: {} desactivado correctamente", id);
    }
}