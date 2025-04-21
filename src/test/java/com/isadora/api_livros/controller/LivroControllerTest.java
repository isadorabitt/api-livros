package com.isadora.api_livros.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isadora.api_livros.dto.LivroDTO;
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
    private GoogleBooksService googleBooksService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void deveCadastrarLivroComSucesso() throws Exception {
        // Arrange
        LivroDTO livroRequest = new LivroDTO("Título Exemplo", "Autor Exemplo");
        Livro livroSalvo = new Livro(1L, "Título Exemplo", "Autor Exemplo");

        Mockito.when(livroService.salvarLivro(Mockito.any(Livro.class)))
                .thenReturn(livroSalvo);

        // Act & Assert
        mockMvc.perform(post("/api/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livroRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titulo").value("Título Exemplo"))
                .andExpect(jsonPath("$.autor").value("Autor Exemplo"));
    }

    @Test
    void naoDeveCadastrarLivroDuplicado() throws Exception {
        // Arrange
        LivroDTO livroRequest = new LivroDTO("Título Duplicado", "Autor Exemplo");
        String mensagemErro = "Já existe um livro com este título.";

        Mockito.when(livroService.salvarLivro(Mockito.any(Livro.class)))
                .thenThrow(new LivroDuplicadoException(mensagemErro));

        // Act & Assert
        mockMvc.perform(post("/api/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livroRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(mensagemErro));
    }
}