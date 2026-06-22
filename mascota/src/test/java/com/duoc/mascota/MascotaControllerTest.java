package com.duoc.mascota;

import com.duoc.mascota.Controller.MascotaController;
import com.duoc.mascota.Dto.MascotaDTO;
import com.duoc.mascota.Service.MascotaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MascotaControllerTest {

    @Mock
    private MascotaService mascotaService;

    @InjectMocks
    private MascotaController mascotaController;

    @Test
    @DisplayName("POST guardar debe retornar 201 CREATED con la mascota guardada")
    void guardar_deberiaRetornar201ConMascotaCreada() {
        MascotaDTO dto = new MascotaDTO(null, "Max", "Perro", "Labrador", 3, 1L);
        MascotaDTO guardada = new MascotaDTO(10L, "Max", "Perro", "Labrador", 3, 1L);

        when(mascotaService.guardar(dto)).thenReturn(guardada);

        ResponseEntity<MascotaDTO> respuesta = mascotaController.guardar(dto);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertEquals(10L, respuesta.getBody().getId());
        verify(mascotaService, times(1)).guardar(dto);
    }

    @Test
    @DisplayName("GET por ID debe retornar 200 OK con la mascota encontrada")
    void obtenerMascota_deberiaRetornar200ConMascota() {
        Long id = 5L;
        MascotaDTO dto = new MascotaDTO(id, "Luna", "Gato", "Siames", 2, 3L);

        when(mascotaService.buscarPorId(id)).thenReturn(dto);

        ResponseEntity<MascotaDTO> respuesta = mascotaController.obtenerMascota(id);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals("Luna", respuesta.getBody().getNombre());
        verify(mascotaService, times(1)).buscarPorId(id);
    }

    @Test
    @DisplayName("GET listar todas debe retornar 200 OK con la lista de mascotas activas")
    void listarTodas_deberiaRetornar200ConListaDeMascotas() {
        List<MascotaDTO> lista = List.of(new MascotaDTO(1L, "Max", "Perro", "Labrador", 3, 1L));

        when(mascotaService.listarMascotas()).thenReturn(lista);

        ResponseEntity<List<MascotaDTO>> respuesta = mascotaController.listarTodas();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        verify(mascotaService, times(1)).listarMascotas();
    }

    @Test
    @DisplayName("GET por cliente debe retornar 200 OK con las mascotas de ese cliente")
    void listarPorCliente_deberiaRetornar200ConMascotasDelCliente() {
        Long clienteId = 1L;
        List<MascotaDTO> lista = List.of(new MascotaDTO(1L, "Max", "Perro", "Labrador", 3, clienteId));

        when(mascotaService.listarPorCliente(clienteId)).thenReturn(lista);

        ResponseEntity<List<MascotaDTO>> respuesta = mascotaController.listarPorCliente(clienteId);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(clienteId, respuesta.getBody().get(0).getClienteId());
        verify(mascotaService, times(1)).listarPorCliente(clienteId);
    }

    @Test
    @DisplayName("PUT actualizar debe retornar 200 OK con la mascota actualizada")
    void actualizar_deberiaRetornar200ConMascotaActualizada() {
        Long id = 1L;
        MascotaDTO dto = new MascotaDTO(null, "Max", "Perro", "Golden", 4, 1L);
        MascotaDTO actualizada = new MascotaDTO(id, "Max", "Perro", "Golden", 4, 1L);

        when(mascotaService.actualizar(id, dto)).thenReturn(actualizada);

        ResponseEntity<MascotaDTO> respuesta = mascotaController.actualizar(id, dto);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals("Golden", respuesta.getBody().getRaza());
        verify(mascotaService, times(1)).actualizar(id, dto);
    }

    @Test
    @DisplayName("DELETE eliminar debe retornar 204 NO_CONTENT")
    void eliminar_deberiaRetornar204SinContenido() {
        Long id = 1L;

        ResponseEntity<Void> respuesta = mascotaController.eliminar(id);

        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(mascotaService, times(1)).eliminarLogico(id);
    }
}
