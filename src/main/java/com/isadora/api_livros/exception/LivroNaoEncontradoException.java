package com.isadora.api_livros.exception;

public class LivroNaoEncontradoException extends RuntimeException {
    public LivroNaoEncontradoException(Long id) {
        super("Livro com ID " + id + " não encontrado.");
    }
}
