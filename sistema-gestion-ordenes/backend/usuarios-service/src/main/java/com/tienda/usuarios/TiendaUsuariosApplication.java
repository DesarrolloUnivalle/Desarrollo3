package com.tienda.usuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;

@EnableDiscoveryClient
@SpringBootApplication
public class TiendaUsuariosApplication {
    public static void main(String[] args) {
        SpringApplication.run(TiendaUsuariosApplication.class, args);
    }
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Usuarios API")
                        .version("1.0")
                        .description("API para la gestión de Usuarios"));
    }
}
