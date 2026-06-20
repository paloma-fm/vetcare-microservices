package com.duoc.auth.Util; // Con 'U' mayúscula como tu carpeta

import com.duoc.auth.Model.UsuarioModel; // Con 'M' mayúscula como tu carpeta
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    // Genera la llave criptográfica asegurando que el string de la clave sea válido
    private Key getSigningKey() {
        byte[] keyBytes = this.secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Método principal para construir el JWT
    public String generateToken(UsuarioModel usuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", usuario.getRole()); // Guardamos el rol dentro del token
        claims.put("id", usuario.getId());     // Guardamos el ID del usuario

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(usuario.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Firma digital
                .compact();
    }
}