package com.isadora.api_livros.service;

import com.isadora.api_livros.dto.LivroGoogleDTO;
import com.isadora.api_livros.model.Livro;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class GoogleBooksService {

    private final WebClient webClient;

    public GoogleBooksService() {
        this.webClient = WebClient.create("https://www.googleapis.com/books/v1/volumes");
    }

    public Livro buscarLivroPorTitulo(String titulo) {
        String query = "?q=intitle:" + URLEncoder.encode(titulo, StandardCharsets.UTF_8);

        return webClient.get()
                .uri(query)
                .retrieve()
                .bodyToMono(GoogleBooksResponse.class)
                .map(response -> {
                    if (response != null && response.items != null && !response.items.isEmpty()) {
                        var volumeInfo = response.items.get(0).volumeInfo;
                        Livro livro = new Livro();
                        livro.setTitulo(volumeInfo.title);
                        if (volumeInfo.authors != null && !volumeInfo.authors.isEmpty()) {
                            livro.setAutor(volumeInfo.authors.get(0));
                        }
                        livro.setAnoPublicacao(volumeInfo.publishedDate);
                        return livro;
                    }
                    return null;
                })
                .block(); // transforma em código síncrono
    }

    public List<LivroGoogleDTO> buscarLivros(String titulo) {
        String query = "?q=intitle:" + URLEncoder.encode(titulo, StandardCharsets.UTF_8);

        GoogleBooksResponse response = webClient.get()
                .uri(query)
                .retrieve()
                .bodyToMono(GoogleBooksResponse.class)
                .block();  // Transformar a requisição em síncrona

        // Transformando a resposta para LivroGoogleDTO
        return response != null && response.items != null
                ? response.items.stream()
                .map(item -> {
                    LivroGoogleDTO livroDTO = new LivroGoogleDTO(
                            item.volumeInfo.title,
                            item.volumeInfo.authors != null && !item.volumeInfo.authors.isEmpty()
                                    ? item.volumeInfo.authors.get(0) : "Desconhecido",
                            item.volumeInfo.description != null ? item.volumeInfo.description : "Sem descrição"
                    );
                    return livroDTO;
                }).toList()
                : List.of();
    }
}
