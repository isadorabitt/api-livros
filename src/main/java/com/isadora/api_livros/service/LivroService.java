package com.isadora.api_livros.service;

import com.isadora.api_livros.exception.LivroDuplicadoException;
import com.isadora.api_livros.exception.LivroNaoEncontradoException;
import com.isadora.api_livros.model.Livro;
import com.isadora.api_livros.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public Livro salvarLivro(Livro livro) {
        Optional<Livro> existente = livroRepository.findByTitulo(livro.getTitulo());
        if (existente.isPresent()) {
            throw new LivroDuplicadoException("Já existe um livro com este título.");
        }

        return livroRepository.save(livro);
    }

    public List<Livro> listarLivros() {
        return livroRepository.findAll();
    }
    public Livro buscarPorId(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new LivroNaoEncontradoException(id));
    }
    public Livro atualizarLivro(Long id, Livro livroAtualizado) {
        Livro livroExistente = buscarPorId(id);

        livroExistente.setTitulo(livroAtualizado.getTitulo());
        livroExistente.setAutor(livroAtualizado.getAutor());

        return livroRepository.save(livroExistente);
    }

    public void deletarLivro(Long id) {
        Livro livro = buscarPorId(id);
        livroRepository.delete(livro);
    }


}
