package com.duoc.auth.Controller;

import com.duoc.auth.Dto.LoginRequest;
import com.duoc.auth.Model.UsuarioModel;
import com.duoc.auth.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Endpoint para registrar un nuevo usuario (Cliente, Veterinario, etc.)
    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody UsuarioModel usuario) {
        try {
            UsuarioModel nuevoUsuario = authService.registrar(usuario);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Usuario registrado con éxito",
                    "username", nuevoUsuario.getUsername()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "El nombre de usuario ya existe o los datos son inválidos."));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String username = request.getUsername();
            String password = request.getPassword();

            String token = authService.login(username, password);

            // Devolvemos el JWT envuelto en un objeto JSON limpio
            return ResponseEntity.ok(Map.of("token", token));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }
}