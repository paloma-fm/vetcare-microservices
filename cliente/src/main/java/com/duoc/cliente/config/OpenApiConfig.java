package com.duoc.cliente.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        // Servidores para Cliente
        Server gatewayServer = new Server();
        gatewayServer.setUrl("http://localhost:8080");
        gatewayServer.setDescription("API Gateway");

        Server localServer = new Server();
        localServer.setUrl("http://localhost:8081"); // ◄ Puerto 8081 para Cliente
        localServer.setDescription("Acceso Directo Cliente");

        return new OpenAPI()
                .servers(List.of(gatewayServer, localServer))
                // 1. Esto activa el candado global en este microservicio
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                // 2. Esto le dice a Swagger que use formato JWT
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Introduce el token JWT obtenido de Auth")));
    }
}