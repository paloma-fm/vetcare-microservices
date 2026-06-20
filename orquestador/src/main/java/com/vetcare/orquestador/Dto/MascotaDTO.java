package com.vetcare.orquestador.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MascotaDTO {

    private Long id;
    private String nombre;
    private String especie;
    private String raza;
    private Integer edad;
    private Long clienteId;
    private List<AtencionDTO> atenciones;
    private List<VacunaDTO> vacunas;
}
