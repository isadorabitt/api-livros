package com.isadora.api_livros.controller;

import com.isadora.api_livros.dto.LivroDTO;
import com.isadora.api_livros.dto.LivroGoogleDTO;
import com.isadora.api_livros.model.Livro;
import com.isadora.api_livros.service.GoogleBooksService;
import com.isadora.api_livros.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livros")
@Tag(name = "Livros", description = "Gerencia livros locais e integração com Google Books")
public class LivroController {

    private final LivroService livroService;
    private final GoogleBooksService googleBooksService;

    @Autowired
    public LivroController(LivroService livroService, GoogleBooksService googleBooksService) {
        this.livroService = livroService;
        this.googleBooksService = googleBooksService;
    }

    // ========== CRUD LOCAL ========== //
    @PostMapping
    @Operation(summary = "Cadastra um novo livro", description = "Recebe um livro via JSON e salva no banco de dados local")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Livro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou incompletos")
    })
    public ResponseEntity<Livro> cadastrarLivro(
            @Parameter(description = "DTO com título e autor do livro", required = true)
            @RequestBody @Valid LivroDTO livroDTO) {
        Livro livro = new Livro(null, livroDTO.titulo(), livroDTO.autor());
        Livro livroSalvo = livroService.salvarLivro(livro);
        return ResponseEntity.status(HttpStatus.CREATED).body(livroSalvo);
    }

    @GetMapping
    @Operation(summary = "Lista todos os livros", description = "Retorna todos os livros cadastrados localmente")
    @ApiResponse(responseCode = "200", description = "Lista de livros retornada com sucesso")
    public ResponseEntity<List<Livro>> listarLivros() {
        return ResponseEntity.ok(livroService.listarLivros());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca livro por ID", description = "Retorna um livro específico baseado no ID local")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro encontrado"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    public ResponseEntity<Livro> buscarLivroPorId(
            @Parameter(description = "ID do livro a ser buscado", required = true, example = "1")
            @PathVariable Long id) {
        Livro livro = livroService.buscarPorId(id);
        return ResponseEntity.ok(livro);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um livro", description = "Atualiza todos os dados de um livro existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou incompletos")
    })
    public ResponseEntity<Livro> atualizarLivro(
            @Parameter(description = "ID do livro a ser atualizado", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Dados completos do livro para atualização", required = true)
            @RequestBody @Valid Livro livro) {
        Livro atualizado = livroService.atualizarLivro(id, livro);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um livro", description = "Exclui permanentemente um livro do banco local")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Livro removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    public ResponseEntity<Void> deletarLivro(
            @Parameter(description = "ID do livro a ser removido", required = true, example = "1")
            @PathVariable Long id) {
        livroService.deletarLivro(id);
        return ResponseEntity.noContent().build();
    }

    // ========== INTEGRAÇÃO GOOGLE BOOKS ========== //
    @GetMapping("/google")
    @Operation(summary = "Busca livros no Google Books", description = "Consulta a API do Google Books e retorna resultados públicos")
    @Tag(name = "Google Books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetro de busca inválido")
    })
    public ResponseEntity<List<LivroGoogleDTO>> buscarLivrosPorTitulo(
            @Parameter(description = "Título para busca no Google Books", required = true, example = "Harry Potter")
            @RequestParam @NotBlank String titulo) {
        List<LivroGoogleDTO> livros = googleBooksService.buscarLivros(titulo);
        return ResponseEntity.ok(livros);
    }

    @PostMapping("/google/importar")
    @Operation(summary = "Importa livro do Google Books", description = "Busca um livro no Google Books pelo título e salva localmente")
    @Tag(name = "Google Books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Livro importado e salvo com sucesso"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado no Google Books"),
            @ApiResponse(responseCode = "400", description = "Parâmetro de busca inválido")
    })
    public ResponseEntity<Livro> importarLivroDoGoogle(
            @Parameter(description = "Título exato do livro a ser importado", required = true, example = "O Senhor dos Anéis")
            @RequestParam @NotBlank String titulo) {
        Livro livro = googleBooksService.buscarLivroPorTitulo(titulo);

        if (livro == null) {
            return ResponseEntity.notFound().build();
        }

        Livro livroSalvo = livroService.salvarLivro(livro);
        return ResponseEntity.status(HttpStatus.CREATED).body(livroSalvo);
    }
}