package com.isadora.api_livros.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Livros")
                        .description("API REST para cadastro e consulta de livros")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Isadora")
                                .email("contato@exemplo.com")
                        )
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")
                        )
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Documentação completa no GitHub")
                        .url("https://github.com/seu-usuario/api-livros")
                );
    }
}
