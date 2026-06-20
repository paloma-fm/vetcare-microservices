package com.duoc.atencion.Service;

import com.duoc.atencion.Dto.AtencionDTO;
import com.duoc.atencion.Exception.BadRequestException;
import com.duoc.atencion.Exception.ResourceNotFoundException;
import com.duoc.atencion.Model.AtencionModel;
import com.duoc.atencion.Repository.AtencionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AtencionService {

    private final AtencionRepository atencionRepository;

    public List<AtencionModel> listarAtenciones() {
        log.info("Listando todas las atenciones clínicas");
        return atencionRepository.findAll();
    }

    public AtencionModel buscarPorId(Long id) {
        log.info("Buscando atención con ID: {}", id);

        return atencionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontró atención con ID: {}", id);
                    return new ResourceNotFoundException("No existe una atención con ID: " + id);
                });
    }

    public List<AtencionModel> listarPorMascota(Long idMascota) {
        log.info("Listando atenciones de la mascota con ID: {}", idMascota);
        return atencionRepository.findByIdMascota(idMascota);
    }

    public List<AtencionModel> listarPorVeterinario(Long idVeterinario) {
        log.info("Listando atenciones del veterinario con ID: {}", idVeterinario);
        return atencionRepository.findByIdVeterinario(idVeterinario);
    }

    public AtencionModel guardarAtencion(AtencionDTO atencionDTO) {
        log.info("Intentando registrar atención para mascota ID: {} y veterinario ID: {}",
                atencionDTO.getIdMascota(), atencionDTO.getIdVeterinario());

        validarFechaAtencion(atencionDTO.getFechaAtencion());

        AtencionModel atencion = new AtencionModel();

        atencion.setIdMascota(atencionDTO.getIdMascota());
        atencion.setIdVeterinario(atencionDTO.getIdVeterinario());
        atencion.setFechaAtencion(atencionDTO.getFechaAtencion());
        atencion.setMotivo(atencionDTO.getMotivo());
        atencion.setDiagnostico(atencionDTO.getDiagnostico());
        atencion.setTratamiento(atencionDTO.getTratamiento());
        atencion.setObservaciones(atencionDTO.getObservaciones());
        atencion.setActivo(atencionDTO.getActivo());

        AtencionModel atencionGuardada = atencionRepository.save(atencion);

        log.info("Atención registrada correctamente con ID: {}", atencionGuardada.getIdAtencion());

        return atencionGuardada;
    }

    public AtencionModel actualizarAtencion(Long id, AtencionDTO atencionDTO) {
        log.info("Intentando actualizar atención con ID: {}", id);

        validarFechaAtencion(atencionDTO.getFechaAtencion());

        AtencionModel atencion = buscarPorId(id);

        atencion.setIdMascota(atencionDTO.getIdMascota());
        atencion.setIdVeterinario(atencionDTO.getIdVeterinario());
        atencion.setFechaAtencion(atencionDTO.getFechaAtencion());
        atencion.setMotivo(atencionDTO.getMotivo());
        atencion.setDiagnostico(atencionDTO.getDiagnostico());
        atencion.setTratamiento(atencionDTO.getTratamiento());
        atencion.setObservaciones(atencionDTO.getObservaciones());
        atencion.setActivo(atencionDTO.getActivo());

        AtencionModel atencionActualizada = atencionRepository.save(atencion);

        log.info("Atención actualizada correctamente con ID: {}", atencionActualizada.getIdAtencion());

        return atencionActualizada;
    }

    public void eliminarAtencion(Long id) {
        log.info("Intentando eliminar atención con ID: {}", id);

        AtencionModel atencion = buscarPorId(id);

        atencionRepository.delete(atencion);

        log.info("Atención eliminada correctamente con ID: {}", id);
    }

    private void validarFechaAtencion(LocalDate fechaAtencion) {
        if (fechaAtencion.isAfter(LocalDate.now())) {
            log.warn("Intento de registrar atención con fecha futura: {}", fechaAtencion);
            throw new BadRequestException("La fecha de atención no puede ser futura");
        }
    }
}