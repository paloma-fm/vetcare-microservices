package com.duoc.auth.Service;

import com.duoc.auth.Model.UsuarioModel;
import com.duoc.auth.Repository.UsuarioRepository;
import com.duoc.auth.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // 1. REGISTRO: Toma al usuario, le encripta la clave y lo guarda
    public UsuarioModel registrar(UsuarioModel usuario) {
        // Encriptamos la contraseña usando BCrypt antes de guardar en la BD
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    // 2. LOGIN: Verifica credenciales y si son correctas, fabrica el JWT
    public String login(String username, String password) {
        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByUsername(username);

        if (usuarioOpt.isPresent()) {
            UsuarioModel usuario = usuarioOpt.get();
            // Comparamos la contraseña de Postman con el hash guardado en la BD
            if (passwordEncoder.matches(password, usuario.getPassword())) {
                // Si coinciden, devolvemos su token firmado
                return jwtUtil.generateToken(usuario);
            }
        }

        // Si no existe o la contraseña está mala, lanzamos un error explícito
        throw new RuntimeException("Credenciales inválidas. Inténtalo de nuevo.");
    }
}