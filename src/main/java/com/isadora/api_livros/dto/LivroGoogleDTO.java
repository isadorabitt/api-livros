package com.isadora.api_livros.dto;

public class LivroGoogleDTO {
    private String titulo;
    private String autores;
    private String descricao;

    public LivroGoogleDTO(String titulo, String autores, String descricao) {
        this.titulo = titulo;
        this.autores = autores;
        this.descricao = descricao;
    }

    // Getters e Setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutores() {
        return autores;
    }

    public void setAutores(String autores) {
        this.autores = autores;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
