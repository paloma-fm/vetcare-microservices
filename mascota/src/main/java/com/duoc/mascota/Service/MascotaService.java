package com.duoc.mascota.Service;

import com.duoc.mascota.Dto.MascotaDTO;
import com.duoc.mascota.Exception.ResourceNotFoundException;
import com.duoc.mascota.Model.MascotaModel;
import com.duoc.mascota.Repository.MascotaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MascotaService {

    private static final Logger log = LoggerFactory.getLogger(MascotaService.class);
    private final MascotaRepository mascotaRepository;

    // Inyección por constructor
    public MascotaService(MascotaRepository mascotaRepository) {
        this.mascotaRepository = mascotaRepository;
    }

    public MascotaDTO guardar(MascotaDTO dto) {
        log.info("Registrando nueva mascota: {} para el cliente ID: {}", dto.getNombre(), dto.getClienteId());

        // Mapeo de DTO a Modelo - Agregamos 'true' al final para el campo 'activo'
        MascotaModel mascota = new MascotaModel(
                null,
                dto.getNombre(),
                dto.getEspecie(),
                dto.getRaza(),
                dto.getEdad(),
                dto.getClienteId(),
                true // <-- Toda mascota nueva nace activa por defecto
        );

        MascotaModel guardada = mascotaRepository.save(mascota);
        log.info("Mascota guardada exitosamente con ID: {}", guardada.getId());

        return new MascotaDTO(
                guardada.getId(),
                guardada.getNombre(),
                guardada.getEspecie(),
                guardada.getRaza(),
                guardada.getEdad(),
                guardada.getClienteId()
        );
    }

    public MascotaDTO buscarPorId(Long id) {
        log.info("Buscando mascota activa con ID: {}", id);
        // Modificado para usar el método que verifica si sigue activa
        MascotaModel mascota = mascotaRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada o inactiva con el ID: " + id));

        return new MascotaDTO(
                mascota.getId(),
                mascota.getNombre(),
                mascota.getEspecie(),
                mascota.getRaza(),
                mascota.getEdad(),
                mascota.getClienteId()
        );
    }

    public List<MascotaDTO> listarMascotas() {
        log.info("Listando todas las mascotas registradas (Vigentes)");
        // Opcional: Si en el repositorio agregaste findByActivoTrue() puedes usarlo aquí.
        // Si usas findAll(), te traerá de baja también, por lo que usaremos una filtración rápida en el stream.
        List<MascotaModel> mascotas = mascotaRepository.findAll();

        return mascotas.stream()
                .filter(MascotaModel::isActivo) // Solo deja pasar las que tengan activo == true
                .map(m -> new MascotaDTO(m.getId(), m.getNombre(), m.getEspecie(), m.getRaza(), m.getEdad(), m.getClienteId()))
                .collect(Collectors.toList());
    }

    public List<MascotaDTO> listarPorCliente(Long clienteId) {
        log.info("Buscando mascotas activas asociadas al cliente ID: {}", clienteId);
        // Modificado para usar tu nuevo método del repositorio con borrado lógico
        List<MascotaModel> mascotas = mascotaRepository.findByClienteIdAndActivoTrue(clienteId);

        return mascotas.stream()
                .map(m -> new MascotaDTO(m.getId(), m.getNombre(), m.getEspecie(), m.getRaza(), m.getEdad(), m.getClienteId()))
                .collect(Collectors.toList());
    }

    // ==========================================
    // 1. NUEVO MÉTODO: ACTUALIZAR MASCOTA (PUT)
    // ==========================================
    public MascotaDTO actualizar(Long id, MascotaDTO dto) {
        log.info("Actualizando datos de la mascota con ID: {}", id);

        // Verificamos que exista y esté activa
        MascotaModel mascotaExistente = mascotaRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se puede actualizar. Mascota no encontrada o dada de baja con ID: " + id));

        // Pisamos los datos viejos con los nuevos recibidos en el DTO
        mascotaExistente.setNombre(dto.getNombre());
        mascotaExistente.setEspecie(dto.getEspecie());
        mascotaExistente.setRaza(dto.getRaza());
        mascotaExistente.setEdad(dto.getEdad());
        mascotaExistente.setClienteId(dto.getClienteId());
        // El campo 'activo' se mantiene intacto (true)

        MascotaModel actualizada = mascotaRepository.save(mascotaExistente);
        log.info("Mascota ID: {} actualizada con éxito", actualizada.getId());

        return new MascotaDTO(
                actualizada.getId(),
                actualizada.getNombre(),
                actualizada.getEspecie(),
                actualizada.getRaza(),
                actualizada.getEdad(),
                actualizada.getClienteId()
        );
    }

    // ==========================================
    // 2. NUEVO MÉTODO: BORRADO LÓGICO (DELETE)
    // ==========================================
    public void eliminarLogico(Long id) {
        log.info("Aplicando borrado lógico a la mascota con ID: {}", id);

        // Buscamos si la mascota existe y está activa
        MascotaModel mascota = mascotaRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se puede eliminar. Mascota no encontrada o ya inactiva con ID: " + id));

        // Cambiamos el estado en lugar de remover de la BD físicamente
        mascota.setActivo(false);

        // Guardamos el cambio de estado
        mascotaRepository.save(mascota);
        log.info("Mascota ID: {} desactivada (borrada lógicamente) correctamente", id);
    }
}