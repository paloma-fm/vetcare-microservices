package com.vetcare.orquestador.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FichaMedicaIntegralDTO {

    private ClienteDTO cliente;
    private List<MascotaDTO> mascotas;
}
