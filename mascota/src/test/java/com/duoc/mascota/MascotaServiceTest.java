package com.duoc.mascota;

import com.duoc.mascota.Dto.MascotaDTO;
import com.duoc.mascota.Exception.ResourceNotFoundException;
import com.duoc.mascota.Model.MascotaModel;
import com.duoc.mascota.Repository.MascotaRepository;
import com.duoc.mascota.Service.MascotaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MascotaServiceTest {

    @Mock
    private MascotaRepository mascotaRepository;

    @InjectMocks
    private MascotaService mascotaService;

    // ================================================================
    // TEST 1: Guardar una nueva mascota
    // Regla de negocio: toda mascota nueva debe nacer con activo = true
    // ================================================================
    @Test
    @DisplayName("Guardar mascota nueva debe retornar DTO con datos correctos y activo=true")
    void guardarMascota_deberiaRetornarDTOConDatosCorrectos() {
        // GIVEN: DTO de entrada con datos válidos
        MascotaDTO dto = new MascotaDTO(null, "Max", "Perro", "Labrador", 3, 1L);

        MascotaModel modeloGuardado = new MascotaModel(10L, "Max", "Perro", "Labrador", 3, 1L, true);

        when(mascotaRepository.save(any(MascotaModel.class))).thenReturn(modeloGuardado);

        // WHEN: Se invoca guardar en el servicio
        MascotaDTO resultado = mascotaService.guardar(dto);

        // THEN: El DTO retornado debe tener ID asignado y los datos correctos
        assertNotNull(resultado, "El resultado no debe ser nulo");
        assertEquals(10L, resultado.getId(), "El ID debe corresponder al guardado en BD");
        assertEquals("Max", resultado.getNombre(), "El nombre debe coincidir");
        assertEquals("Perro", resultado.getEspecie(), "La especie debe coincidir");
        assertEquals(1L, resultado.getClienteId(), "El clienteId debe coincidir");

        verify(mascotaRepository, times(1)).save(any(MascotaModel.class));
    }

    // ================================================================
    // TEST 2: Buscar mascota activa por ID
    // Regla de negocio: solo se retornan mascotas con activo=true
    // ================================================================
    @Test
    @DisplayName("Buscar mascota activa por ID debe retornar el DTO correspondiente")
    void buscarPorId_conMascotaActiva_deberiaRetornarDTO() {
        // GIVEN: Una mascota activa existente en el repositorio
        Long idBuscado = 5L;
        MascotaModel mascotaActiva = new MascotaModel(5L, "Luna", "Gato", "Siames", 2, 3L, true);

        when(mascotaRepository.findByIdAndActivoTrue(idBuscado)).thenReturn(Optional.of(mascotaActiva));

        // WHEN: Se busca la mascota por su ID
        MascotaDTO resultado = mascotaService.buscarPorId(idBuscado);

        // THEN: Se retorna el DTO con los datos correctos
        assertNotNull(resultado);
        assertEquals(5L, resultado.getId());
        assertEquals("Luna", resultado.getNombre());
        assertEquals("Gato", resultado.getEspecie());
        assertEquals(3L, resultado.getClienteId());

        verify(mascotaRepository, times(1)).findByIdAndActivoTrue(idBuscado);
    }

    // ================================================================
    // TEST 3: Eliminar lógicamente una mascota
    // Regla de negocio: el borrado no es físico, solo cambia activo a false
    // ================================================================
    @Test
    @DisplayName("Eliminar lógico debe lanzar excepción si la mascota no existe o ya está inactiva")
    void eliminarLogico_conMascotaInexistente_deberiaLanzarExcepcion() {
        // GIVEN: Un ID que no existe en el repositorio como activo
        Long idInexistente = 99L;

        when(mascotaRepository.findByIdAndActivoTrue(idInexistente)).thenReturn(Optional.empty());

        // WHEN / THEN: Se espera ResourceNotFoundException al intentar eliminar
        ResourceNotFoundException excepcion = assertThrows(
                ResourceNotFoundException.class,
                () -> mascotaService.eliminarLogico(idInexistente),
                "Debe lanzar ResourceNotFoundException para IDs no activos"
        );

        assertTrue(excepcion.getMessage().contains("99"),
                "El mensaje de error debe contener el ID buscado");

        verify(mascotaRepository, never()).save(any(MascotaModel.class));
    }
}
