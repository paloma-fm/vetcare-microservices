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

import java.util.List;
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

    // ================================================================
    // TEST 4: Listar mascotas
    // Regla de negocio: del total de mascotas, solo se devuelven las activas
    // ================================================================
    @Test
    @DisplayName("Listar mascotas debe filtrar y retornar solo las que están activas")
    void listarMascotas_deberiaRetornarSoloLasActivas() {
        // GIVEN: el repositorio tiene una mascota activa y una inactiva
        MascotaModel activa = new MascotaModel(1L, "Max", "Perro", "Labrador", 3, 1L, true);
        MascotaModel inactiva = new MascotaModel(2L, "Rocky", "Perro", "Bulldog", 5, 1L, false);

        when(mascotaRepository.findAll()).thenReturn(List.of(activa, inactiva));

        // WHEN: se listan todas las mascotas
        List<MascotaDTO> resultado = mascotaService.listarMascotas();

        // THEN: solo la mascota activa debe estar en el resultado
        assertEquals(1, resultado.size(), "Solo debe retornar la mascota activa");
        assertEquals("Max", resultado.get(0).getNombre());

        verify(mascotaRepository, times(1)).findAll();
    }

    // ================================================================
    // TEST 5: Listar mascotas por cliente
    // Regla de negocio: retorna solo las mascotas activas asociadas a ese cliente
    // ================================================================
    @Test
    @DisplayName("Listar por cliente debe retornar las mascotas activas de ese cliente")
    void listarPorCliente_deberiaRetornarMascotasDelCliente() {
        // GIVEN: el cliente 1 tiene una mascota activa registrada
        Long clienteId = 1L;
        MascotaModel mascota = new MascotaModel(1L, "Max", "Perro", "Labrador", 3, clienteId, true);

        when(mascotaRepository.findByClienteIdAndActivoTrue(clienteId)).thenReturn(List.of(mascota));

        // WHEN: se buscan las mascotas de ese cliente
        List<MascotaDTO> resultado = mascotaService.listarPorCliente(clienteId);

        // THEN: debe retornar la mascota encontrada con el clienteId correcto
        assertEquals(1, resultado.size());
        assertEquals(clienteId, resultado.get(0).getClienteId());

        verify(mascotaRepository, times(1)).findByClienteIdAndActivoTrue(clienteId);
    }

    // ================================================================
    // TEST 6: Actualizar mascota existente
    // Regla de negocio: solo se puede actualizar una mascota activa, y el
    // campo 'activo' no se modifica en este proceso
    // ================================================================
    @Test
    @DisplayName("Actualizar una mascota activa debe guardar los nuevos datos y retornar el DTO actualizado")
    void actualizar_conMascotaActiva_deberiaRetornarDTOActualizado() {
        // GIVEN: una mascota activa existente y los nuevos datos a aplicar
        Long id = 1L;
        MascotaModel mascotaExistente = new MascotaModel(id, "Max", "Perro", "Labrador", 3, 1L, true);
        MascotaDTO nuevosDatos = new MascotaDTO(null, "Max", "Perro", "Golden", 4, 1L);
        MascotaModel mascotaActualizada = new MascotaModel(id, "Max", "Perro", "Golden", 4, 1L, true);

        when(mascotaRepository.findByIdAndActivoTrue(id)).thenReturn(Optional.of(mascotaExistente));
        when(mascotaRepository.save(any(MascotaModel.class))).thenReturn(mascotaActualizada);

        // WHEN: se actualiza la mascota
        MascotaDTO resultado = mascotaService.actualizar(id, nuevosDatos);

        // THEN: el DTO retornado refleja los datos nuevos
        assertEquals("Golden", resultado.getRaza(), "La raza debe haberse actualizado");
        assertEquals(4, resultado.getEdad(), "La edad debe haberse actualizado");

        verify(mascotaRepository, times(1)).save(any(MascotaModel.class));
    }
}
