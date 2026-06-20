package com.duoc.vacuna.Service;

import com.duoc.vacuna.Dto.VacunaDTO;
import com.duoc.vacuna.Exception.BadRequestException;
import com.duoc.vacuna.Exception.ResourceNotFoundException;
import com.duoc.vacuna.Model.VacunaModel;
import com.duoc.vacuna.Repository.VacunaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VacunaService {

    private final VacunaRepository vacunaRepository;

    public List<VacunaModel> listarVacunas() {
        log.info("Listando todas las vacunas");
        return vacunaRepository.findAll();
    }

    public VacunaModel buscarPorId(Long id) {
        log.info("Buscando vacuna con ID: {}", id);

        return vacunaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontró vacuna con ID: {}", id);
                    return new ResourceNotFoundException("No existe una vacuna con ID: " + id);
                });
    }

    public List<VacunaModel> listarPorMascota(Long idMascota) {
        log.info("Listando vacunas de la mascota con ID: {}", idMascota);
        return vacunaRepository.findByIdMascota(idMascota);
    }

    public List<VacunaModel> listarPorEstado(String estado) {
        log.info("Listando vacunas con estado: {}", estado);
        return vacunaRepository.findByEstado(estado);
    }

    public VacunaModel guardarVacuna(VacunaDTO vacunaDTO) {
        log.info("Intentando registrar vacuna para mascota ID: {}", vacunaDTO.getIdMascota());

        validarFechas(vacunaDTO.getFechaAplicacion(), vacunaDTO.getProximaDosis());

        VacunaModel vacuna = new VacunaModel();

        vacuna.setIdMascota(vacunaDTO.getIdMascota());
        vacuna.setNombreVacuna(vacunaDTO.getNombreVacuna());
        vacuna.setFechaAplicacion(vacunaDTO.getFechaAplicacion());
        vacuna.setProximaDosis(vacunaDTO.getProximaDosis());
        vacuna.setEstado(vacunaDTO.getEstado());
        vacuna.setObservaciones(vacunaDTO.getObservaciones());
        vacuna.setActivo(vacunaDTO.getActivo());

        VacunaModel vacunaGuardada = vacunaRepository.save(vacuna);

        log.info("Vacuna registrada correctamente con ID: {}", vacunaGuardada.getIdVacuna());

        return vacunaGuardada;
    }

    public VacunaModel actualizarVacuna(Long id, VacunaDTO vacunaDTO) {
        log.info("Intentando actualizar vacuna con ID: {}", id);

        validarFechas(vacunaDTO.getFechaAplicacion(), vacunaDTO.getProximaDosis());

        VacunaModel vacuna = buscarPorId(id);

        vacuna.setIdMascota(vacunaDTO.getIdMascota());
        vacuna.setNombreVacuna(vacunaDTO.getNombreVacuna());
        vacuna.setFechaAplicacion(vacunaDTO.getFechaAplicacion());
        vacuna.setProximaDosis(vacunaDTO.getProximaDosis());
        vacuna.setEstado(vacunaDTO.getEstado());
        vacuna.setObservaciones(vacunaDTO.getObservaciones());
        vacuna.setActivo(vacunaDTO.getActivo());

        VacunaModel vacunaActualizada = vacunaRepository.save(vacuna);

        log.info("Vacuna actualizada correctamente con ID: {}", vacunaActualizada.getIdVacuna());

        return vacunaActualizada;
    }

    public void eliminarVacuna(Long id) {
        log.info("Intentando eliminar vacuna con ID: {}", id);

        VacunaModel vacuna = buscarPorId(id);

        vacunaRepository.delete(vacuna);

        log.info("Vacuna eliminada correctamente con ID: {}", id);
    }

    private void validarFechas(LocalDate fechaAplicacion, LocalDate proximaDosis) {

        if (fechaAplicacion.isAfter(LocalDate.now())) {
            log.warn("Intento de registrar vacuna con fecha de aplicación futura: {}", fechaAplicacion);
            throw new BadRequestException("La fecha de aplicación no puede ser futura");
        }

        if (proximaDosis != null && proximaDosis.isBefore(fechaAplicacion)) {
            log.warn("Intento de registrar próxima dosis anterior a la fecha de aplicación");
            throw new BadRequestException("La próxima dosis no puede ser anterior a la fecha de aplicación");
        }
    }
}