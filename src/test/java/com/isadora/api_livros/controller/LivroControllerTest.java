package com.isadora.api_livros.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isadora.api_livros.exception.LivroDuplicadoException;
import com.isadora.api_livros.model.Livro;
import com.isadora.api_livros.service.GoogleBooksService;
import com.isadora.api_livros.service.LivroService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LivroController.class)
class LivroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LivroService livroService;

    @MockBean
    private GoogleBooksService googleBooksService; // Adicionado o mock para GoogleBooksService

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void deveCadastrarLivroComSucesso() throws Exception {
        // Arrange
        Livro livroRequest = new Livro(null, "Título Exemplo", "Autor Exemplo");
        Livro livroResponse = new Livro(1L, "Título Exemplo", "Autor Exemplo");

        // Configura o mock
        Mockito.when(livroService.salvarLivro(Mockito.any(Livro.class)))
                .thenReturn(livroResponse);

        // Act & Assert
        mockMvc.perform(post("/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livroRequest)))
                .andExpect(status().isOk()) // Mantido como 200 para alinhar com o controller
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titulo").value("Título Exemplo"))
                .andExpect(jsonPath("$.autor").value("Autor Exemplo"));
    }

    @Test
    void naoDeveCadastrarLivroDuplicado() throws Exception {
        // Arrange
        Livro livro = new Livro(null, "Título Duplicado", "Autor Exemplo");
        String mensagemErro = "Já existe um livro com este título.";

        // Configura o mock para lançar exceção
        Mockito.when(livroService.salvarLivro(Mockito.any(Livro.class)))
                .thenThrow(new LivroDuplicadoException(mensagemErro));

        // Act & Assert
        mockMvc.perform(post("/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livro)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(mensagemErro));
    }
}