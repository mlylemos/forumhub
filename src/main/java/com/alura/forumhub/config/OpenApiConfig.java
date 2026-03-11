package com.alura.forumhub.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FórumHub API")
                        .version("1.0.0")
                        .description("API REST do FórumHub — Alura Challenge Back End. " +
                                "Permite criar, listar, atualizar e excluir tópicos e respostas com autenticação JWT.")
                        .contact(new Contact()
                                .name("Alura")
                                .url("https://www.alura.com.br"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT"))
                )
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("BearerAuth",
                                new SecurityScheme()
                                        .name("BearerAuth")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Informe o token JWT obtido via POST /login")
                        )
                );
    }
}
