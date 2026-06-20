package com.duoc.api_gateway.Filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Value("${jwt.secret}")
    private String secretKey;

    public static class Config {
        // Esta clase puede quedar vacía si no requieres parámetros dinámicos por ruta
    }

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // 1. Verificar si viene el header de Authorization
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "Falta el encabezado de Autorización", HttpStatus.UNAUTHORIZED);
            }

            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            // 2. Verificar que empiece con la convención 'Bearer '
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return onError(exchange, "Estructura del token de autorización inválida", HttpStatus.UNAUTHORIZED);
            }

            // 3. Extraer el string puro del JWT
            String token = authHeader.substring(7);

            try {
                // 4. Validar la firma con la clave secreta compartida
                Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                // 5. (Opcional) Mutar la petición para inyectar el Username o ID al microservicio de destino
                ServerHttpRequest mutatedRequest = request.mutate()
                        .header("X-User-Username", claims.getSubject())
                        .header("X-User-Role", claims.get("role", String.class))
                        .build();

                return chain.filter(exchange.mutate().request(mutatedRequest).build());

            } catch (Exception e) {
                // Si la firma expiró o es falsa, rebota la petición de inmediato
                return onError(exchange, "Token inválido o expirado: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
            }
        };
    }

    // Método auxiliar para responder con un código HTTP limpio en caso de error
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().add("Content-Type", "application/json");
        return response.setComplete();
    }
}