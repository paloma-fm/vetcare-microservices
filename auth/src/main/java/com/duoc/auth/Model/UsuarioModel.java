package com.duoc.auth.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Esto genera automáticamente: Getters, Setters, toString, equals y hashCode
@NoArgsConstructor // Genera el constructor vacío obligatorio para JPA
@AllArgsConstructor // Genera el constructor con todos los atributos (id, username, password, role)
@Entity
@Table(name = "usuarios")
public class UsuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;
}