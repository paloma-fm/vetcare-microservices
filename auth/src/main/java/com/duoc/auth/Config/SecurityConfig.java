package com.duoc.auth.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. Definimos el encriptor de contraseñas oficial
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Configuramos los accesos permitidos para este microservicio
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Deshabilitamos CSRF porque usaremos JWT
                .authorizeHttpRequests(auth -> auth
                        // ========================================================
                        // AGREGADOS: Endpoints de Swagger y OpenAPI públicos
                        // ========================================================
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // Permitimos que CUALQUIERA entre al login y al registro sin estar autenticado
                        .requestMatchers("/api/v1/auth/**", "/h2-console/**").permitAll()

                        // Cualquier otra ruta hipotética requerirá autenticación
                        .anyRequest().authenticated()
                )
                // Esto es necesario para poder abrir la consola de H2 en el navegador si lo necesitas
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}