package com.isadora.api_livros.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiLivrosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("📚 API de Livros - Portfolio Isadora Bittencourt")
                        .version("1.0")
                        .description("""
                                ## API REST para gerenciamento de livros com integração ao Google Books
                                **Projeto desenvolvido para portfólio Java Junior**
                                
                                ### Funcionalidades:
                                - **Livros Locais**: CRUD completo de livros armazenados localmente
                                - **Google Books**: Integração com API externa para busca e importação de livros
                                
                                ### Tecnologias:
                                - Validações customizadas
                                - Tratamento de erros personalizado
                                - Documentação Swagger/OpenAPI
                                """)
                        .contact(new Contact()
                                .name("Isadora Bittencourt")
                                .email("isadorabittencourt2@gmail.com")
                                .url("https://github.com/isadorabitt")
                        )
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")
                        ))
                .tags(List.of(
                        new Tag().name("1. Livros Locais").description("Operações CRUD para livros armazenados localmente"),
                        new Tag().name("2. Google Books").description("Operações de integração com a API do Google Books")
                ))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Ambiente de Desenvolvimento Local"),
                        new Server()
                                .url("https://${RAILWAY_PUBLIC_DOMAIN:api-livros-demo.up.railway.app}")
                                .description("Ambiente de Produção")
                ));
    }

    @Bean
    public OpenApiCustomizer globalResponsesCustomizer() {
        return openApi -> {
            openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
                // Respostas globais para erros
                ApiResponse badRequest = new ApiResponse()
                        .description("Requisição inválida")
                        .content(new Content().addMediaType(
                                org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                                new MediaType().example("""
                                        {
                                            "timestamp": "2023-08-20T14:30:00",
                                            "status": 400,
                                            "error": "Bad Request",
                                            "message": "Parâmetros inválidos",
                                            "path": "/api/livros"
                                        }""")));

                ApiResponse notFound = new ApiResponse()
                        .description("Recurso não encontrado")
                        .content(new Content().addMediaType(
                                org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                                new MediaType().example("""
                                        {
                                            "timestamp": "2023-08-20T14:32:00",
                                            "status": 404,
                                            "error": "Not Found",
                                            "message": "Livro com ID 123 não encontrado",
                                            "path": "/api/livros/123"
                                        }""")));

                operation.getResponses()
                        .addApiResponse("400", badRequest)
                        .addApiResponse("404", notFound);
            }));
        };
    }
}