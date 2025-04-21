package com.isadora.api_livros.controller;

import com.isadora.api_livros.dto.LivroDTO;
import com.isadora.api_livros.dto.LivroGoogleDTO;
import com.isadora.api_livros.model.Livro;
import com.isadora.api_livros.service.GoogleBooksService;
import com.isadora.api_livros.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livros")
@Tag(name = "1. Livros Locais", description = "Operações CRUD para livros armazenados localmente")
public class LivroController {

    private final LivroService livroService;
    private final GoogleBooksService googleBooksService;

    @Autowired
    public LivroController(LivroService livroService, GoogleBooksService googleBooksService) {
        this.livroService = livroService;
        this.googleBooksService = googleBooksService;
    }

    @PostMapping
    @Operation(summary = "Cadastrar novo livro", description = "Adiciona um novo livro ao banco de dados local")
    public ResponseEntity<Livro> cadastrar(@RequestBody @Valid LivroDTO livroDTO) {
        Livro livro = new Livro(null, livroDTO.titulo(), livroDTO.autor());
        Livro livroSalvo = livroService.salvarLivro(livro);
        return ResponseEntity.ok(livroSalvo);
    }

    @GetMapping
    @Operation(summary = "Listar todos os livros", description = "Retorna todos os livros cadastrados localmente")
    public ResponseEntity<List<Livro>> listarLivros() {
        return ResponseEntity.ok(livroService.listarLivros());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar livro por ID", description = "Retorna um livro específico do banco local pelo seu ID")
    public ResponseEntity<Livro> buscarLivroPorId(@PathVariable Long id) {
        Livro livro = livroService.buscarPorId(id);
        return ResponseEntity.ok(livro);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar livro", description = "Atualiza os dados de um livro existente no banco local")
    public ResponseEntity<Livro> atualizarLivro(@PathVariable Long id, @RequestBody @Valid Livro livro) {
        Livro atualizado = livroService.atualizarLivro(id, livro);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar livro", description = "Remove um livro do banco de dados local")
    public ResponseEntity<Void> deletarLivro(@PathVariable Long id) {
        livroService.deletarLivro(id);
        return ResponseEntity.noContent().build();
    }

    @Tag(name = "2. Google Books", description = "Operações de integração com a API do Google Books")
    @GetMapping("/google/buscar")
    @Operation(summary = "Buscar livros no Google Books", description = "Consulta livros na API do Google Books pelo título")
    public ResponseEntity<List<LivroGoogleDTO>> buscarLivrosPorTitulo(@RequestParam String titulo) {
        List<LivroGoogleDTO> livros = googleBooksService.buscarLivros(titulo);
        return ResponseEntity.ok(livros);
    }

    @Tag(name = "2. Google Books")
    @PostMapping("/google/importar")
    @Operation(summary = "Importar livro do Google Books", description = "Busca um livro no Google Books e salva localmente")
    public ResponseEntity<Livro> buscarESalvarLivro(@RequestParam String titulo) {
        Livro livro = googleBooksService.buscarLivroPorTitulo(titulo);

        if (livro == null) {
            return ResponseEntity.notFound().build();
        }

        Livro livroSalvo = livroService.salvarLivro(livro);
        return ResponseEntity.status(HttpStatus.CREATED).body(livroSalvo);
    }
}