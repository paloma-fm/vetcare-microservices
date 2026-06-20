package com.duoc.veterinario.Service;

import com.duoc.veterinario.Dto.VeterinarioDTO;
import com.duoc.veterinario.Exception.BadRequestException;
import com.duoc.veterinario.Exception.ResourceNotFoundException;
import com.duoc.veterinario.Model.VeterinarioModel;
import com.duoc.veterinario.Repository.VeterinarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VeterinarioService {

    private final VeterinarioRepository veterinarioRepository;

    public List<VeterinarioModel> listarVeterinarios() {
        log.info("Listando todos los veterinarios");
        return veterinarioRepository.findAll();
    }

    public VeterinarioModel buscarPorId(Long id) {
        log.info("Buscando veterinario con ID: {}", id);

        return veterinarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontró veterinario con ID: {}", id);
                    return new ResourceNotFoundException("No existe un veterinario con ID: " + id);
                });
    }

    public VeterinarioModel guardarVeterinario(VeterinarioDTO veterinarioDTO) {
        log.info("Intentando registrar veterinario con correo: {}", veterinarioDTO.getCorreo());

        if (veterinarioRepository.findByCorreo(veterinarioDTO.getCorreo()).isPresent()) {
            log.warn("Intento de registro con correo duplicado: {}", veterinarioDTO.getCorreo());
            throw new BadRequestException("Ya existe un veterinario registrado con ese correo");
        }

        VeterinarioModel veterinario = new VeterinarioModel();

        veterinario.setNombre(veterinarioDTO.getNombre());
        veterinario.setApellido(veterinarioDTO.getApellido());
        veterinario.setEspecialidad(veterinarioDTO.getEspecialidad());
        veterinario.setCorreo(veterinarioDTO.getCorreo());
        veterinario.setTelefono(veterinarioDTO.getTelefono());
        veterinario.setActivo(veterinarioDTO.getActivo());

        VeterinarioModel veterinarioGuardado = veterinarioRepository.save(veterinario);

        log.info("Veterinario registrado correctamente con ID: {}", veterinarioGuardado.getIdVeterinario());

        return veterinarioGuardado;
    }

    public VeterinarioModel actualizarVeterinario(Long id, VeterinarioDTO veterinarioDTO) {
        log.info("Intentando actualizar veterinario con ID: {}", id);

        VeterinarioModel veterinario = buscarPorId(id);

        veterinario.setNombre(veterinarioDTO.getNombre());
        veterinario.setApellido(veterinarioDTO.getApellido());
        veterinario.setEspecialidad(veterinarioDTO.getEspecialidad());
        veterinario.setCorreo(veterinarioDTO.getCorreo());
        veterinario.setTelefono(veterinarioDTO.getTelefono());
        veterinario.setActivo(veterinarioDTO.getActivo());

        VeterinarioModel veterinarioActualizado = veterinarioRepository.save(veterinario);

        log.info("Veterinario actualizado correctamente con ID: {}", veterinarioActualizado.getIdVeterinario());

        return veterinarioActualizado;
    }

    public void eliminarVeterinario(Long id) {
        log.info("Intentando eliminar veterinario con ID: {}", id);

        VeterinarioModel veterinario = buscarPorId(id);

        veterinarioRepository.delete(veterinario);

        log.info("Veterinario eliminado correctamente con ID: {}", id);
    }
}