package com.example.petcare.services;

import java.util.List;

import com.example.petcare.dtos.TutorRequestDTO;

public interface TutorService {
    TutorRequestDTO inserir(TutorRequestDTO dto);

    TutorRequestDTO editar(Long id, TutorRequestDTO dto);

    List<TutorRequestDTO> obterTodos();

    List<TutorRequestDTO> buscarPorNome(String nome);

    TutorRequestDTO obterPorId(Long id);

    void excluir(Long id);
}
