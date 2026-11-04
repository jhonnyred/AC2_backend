package com.example.petcare.services;

import java.util.List;

import com.example.petcare.dtos.AnimalRequestDTO;
import com.example.petcare.dtos.HistoricoAnimalDTO;

public interface AnimalService {
    AnimalRequestDTO inserir(AnimalRequestDTO dto);

    AnimalRequestDTO editar(Long id, AnimalRequestDTO dto);

    List<AnimalRequestDTO> obterTodos();

    List<AnimalRequestDTO> obterPorTutor(Long tutorId);

    AnimalRequestDTO obterPorId(Long id);

    HistoricoAnimalDTO obterHistorico(Long id);

    void excluir(Long id);
}
