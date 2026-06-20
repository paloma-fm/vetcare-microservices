package com.vetcare.orquestador.Service;

import com.vetcare.orquestador.Dto.AtencionDTO;
import com.vetcare.orquestador.Dto.ClienteDTO;
import com.vetcare.orquestador.Dto.FichaMedicaIntegralDTO;
import com.vetcare.orquestador.Dto.MascotaDTO;
import com.vetcare.orquestador.Dto.VacunaDTO;
import com.vetcare.orquestador.Dto.VeterinarioDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class OrquestadorService {

    private final WebClient webClient;

    @Value("${microservices.cliente-url}")
    private String clienteUrl;

    @Value("${microservices.mascota-url}")
    private String mascotaUrl;

    @Value("${microservices.veterinario-url}")
    private String veterinarioUrl;

    @Value("${microservices.atencion-url}")
    private String atencionUrl;

    @Value("${microservices.vacuna-url}")
    private String vacunaUrl;

    public OrquestadorService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public FichaMedicaIntegralDTO obtenerFichaCompleta(Long clienteId) {

        log.info("[ORQUESTADOR] Iniciando recopilación de datos para Cliente ID: {}", clienteId);

        ClienteDTO cliente = webClient.get()
                .uri(clienteUrl + "/" + clienteId)
                .retrieve()
                .bodyToMono(ClienteDTO.class)
                .block();

        log.info("[ORQUESTADOR] Cliente recuperado: {}", cliente != null ? cliente.getNombre() : "No encontrado");

        List<MascotaDTO> mascotas = webClient.get()
                .uri(mascotaUrl + "/cliente/" + clienteId)
                .retrieve()
                .bodyToFlux(MascotaDTO.class)
                .collectList()
                .onErrorReturn(Collections.emptyList())
                .block();

        log.info("[ORQUESTADOR] Mascotas encontradas: {}", mascotas != null ? mascotas.size() : 0);

        if (mascotas != null) {
            for (MascotaDTO mascota : mascotas) {

                Long idMascota = mascota.getId();

                log.info("[ORQUESTADOR] Buscando atenciones para mascota ID: {}", idMascota);

                List<AtencionDTO> atenciones = webClient.get()
                        .uri(atencionUrl + "/mascota/" + idMascota)
                        .retrieve()
                        .bodyToFlux(AtencionDTO.class)
                        .collectList()
                        .onErrorReturn(Collections.emptyList())
                        .block();

                if (atenciones != null) {
                    for (AtencionDTO atencion : atenciones) {

                        Long idVeterinario = atencion.getIdVeterinario();

                        if (idVeterinario != null) {
                            log.info("[ORQUESTADOR] Buscando veterinario ID: {}", idVeterinario);

                            VeterinarioDTO veterinario = webClient.get()
                                    .uri(veterinarioUrl + "/" + idVeterinario)
                                    .retrieve()
                                    .bodyToMono(VeterinarioDTO.class)
                                    .onErrorReturn(new VeterinarioDTO())
                                    .block();

                            atencion.setVeterinario(veterinario);
                        }
                    }
                }

                log.info("[ORQUESTADOR] Buscando vacunas para mascota ID: {}", idMascota);

                List<VacunaDTO> vacunas = webClient.get()
                        .uri(vacunaUrl + "/mascota/" + idMascota)
                        .retrieve()
                        .bodyToFlux(VacunaDTO.class)
                        .collectList()
                        .onErrorReturn(Collections.emptyList())
                        .block();

                mascota.setAtenciones(atenciones);
                mascota.setVacunas(vacunas);
            }
        }

        FichaMedicaIntegralDTO ficha = new FichaMedicaIntegralDTO();
        ficha.setCliente(cliente);
        ficha.setMascotas(mascotas);

        log.info("[ORQUESTADOR] Ficha médica integral generada correctamente para Cliente ID: {}", clienteId);

        return ficha;
    }
}