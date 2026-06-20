package com.vetcare.orquestador.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO {



    private Long id;
    private String nombre;
    private String correo;
    private String telefono;
}
