package com.duoc.mascota.Config;

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
        // Nombre clave para el esquema de seguridad
        final String securitySchemeName = "bearerAuth";

        // Configuración de Servidores
        Server gatewayServer = new Server();
        gatewayServer.setUrl("http://localhost:8080");
        gatewayServer.setDescription("API Gateway");

        Server localServer = new Server();
        localServer.setUrl("http://localhost:8082");
        localServer.setDescription("Acceso Directo Mascota");

        return new OpenAPI()
                .servers(List.of(gatewayServer, localServer))
                // 1. Añadimos el requisito de seguridad global para los endpoints de este Swagger
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                // 2. Definimos el componente Bearer Token que Swagger renderizará
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Introduce tu token JWT obtenido del microservicio Auth para autorizar las peticiones.")));
    }
}