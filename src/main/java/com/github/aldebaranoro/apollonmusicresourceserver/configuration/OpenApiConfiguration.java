package com.github.aldebaranoro.apollonmusicresourceserver.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {
    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI().info(new Info().title("Apollon music API")
                        .version("0.0.1")
                        .description("Backend music service spring boot app.")
                        .contact(new Contact().name("Anatoly Karas")
                                .email("anatol.karas@gmail.com")))
                .servers(List.of(
                        new Server().url("https://apollon-music-resource-server.herokuapp.com").description("Heroku server"),
                        new Server().url("http://localhost:8080").description("Development local server")));
    }
}
