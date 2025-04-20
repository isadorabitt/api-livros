package com.isadora.api_livros.dto;

import jakarta.validation.constraints.NotBlank;

public record LivroDTO(
        @NotBlank(message = "O título é obrigatório") String titulo,
        @NotBlank(message = "O autor é obrigatório") String autor
) {}

