package com.isadora.api_livros.controller;

import com.isadora.api_livros.dto.LivroDTO;
import com.isadora.api_livros.dto.LivroGoogleDTO;
import com.isadora.api_livros.model.Livro;
import com.isadora.api_livros.service.GoogleBooksService;
import com.isadora.api_livros.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livros")
public class LivroController {

    private final LivroService livroService;
    private final GoogleBooksService googleBooksService;

    @Autowired
    public LivroController(LivroService livroService, GoogleBooksService googleBooksService) {
        this.livroService = livroService;
        this.googleBooksService = googleBooksService;
    }

    // Endpoint para cadastrar um livro
    @PostMapping
    public ResponseEntity<Livro> cadastrarLivro(@RequestBody @Valid LivroDTO livroDTO) {
        Livro livro = new Livro(null, livroDTO.titulo(), livroDTO.autor());
        Livro livroSalvo = livroService.salvarLivro(livro);
        return ResponseEntity.ok(livroSalvo);
    }

    // Endpoint para listar todos os livros
    @GetMapping
    public ResponseEntity<List<Livro>> listarLivros() {
        return ResponseEntity.ok(livroService.listarLivros());
    }

    // Endpoint para buscar um livro por ID
    @GetMapping("/{id}")
    public ResponseEntity<Livro> buscarLivroPorId(@PathVariable Long id) {
        Livro livro = livroService.buscarPorId(id);
        return ResponseEntity.ok(livro);
    }

    // Endpoint para atualizar um livro
    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizarLivro(@PathVariable Long id, @RequestBody @Valid Livro livro) {
        Livro atualizado = livroService.atualizarLivro(id, livro);
        return ResponseEntity.ok(atualizado);
    }

    // Endpoint para deletar um livro
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLivro(@PathVariable Long id) {
        livroService.deletarLivro(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para buscar livros na API do Google Books
    @GetMapping("/buscar")
    public ResponseEntity<List<LivroGoogleDTO>> buscarLivrosPorTitulo(@RequestParam String titulo) {
        List<LivroGoogleDTO> livros = googleBooksService.buscarLivros(titulo);
        return ResponseEntity.ok(livros);
    }

    // Endpoint para buscar e salvar um livro retornado pela API do Google Books
    @PostMapping("/buscar")
    public ResponseEntity<Livro> buscarESalvarLivro(@RequestParam String titulo) {
        Livro livro = googleBooksService.buscarLivroPorTitulo(titulo);

        if (livro == null) {
            return ResponseEntity.notFound().build();
        }

        Livro livroSalvo = livroService.salvarLivro(livro); // Salva o livro no banco
        return ResponseEntity.status(HttpStatus.CREATED).body(livroSalvo);
    }
}
