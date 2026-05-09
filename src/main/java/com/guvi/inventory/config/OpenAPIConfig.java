package com.guvi.inventory.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${server.port:9002}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        List<Server> servers = new ArrayList<>();
        servers.add(new Server()
                .url("https://inventory-order-management-x6cl.onrender.com")
                .description("Production Server (Render)"));
        servers.add(new Server()
                .url("http://localhost:" + serverPort)
                .description("Local Development Server"));

        return new OpenAPI()
                .info(new Info()
                        .title("Inventory Order Management System")
                        .version("1.0.0")
                        .description("A comprehensive API for managing inventory, products, categories, and customer orders with role-based authentication")
                        .contact(new Contact()
                                .name("Development Team")
                                .email("support@example.com")))
                .servers(servers)
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT token for API authentication")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"));
    }
}
