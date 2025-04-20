package com.isadora.api_livros.exception;

public class LivroDuplicadoException extends RuntimeException {
    public LivroDuplicadoException(String mensagem) {
        super(mensagem);
    }
}

