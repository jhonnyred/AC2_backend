package com.example.petcare.services;

import java.util.List;

import com.example.petcare.dtos.VacinaRequestDTO;

public interface VacinaService {
    VacinaRequestDTO registrar(VacinaRequestDTO dto);

    List<VacinaRequestDTO> obterTodos();

    List<VacinaRequestDTO> obterPorAnimal(Long animalId);

    VacinaRequestDTO obterPorId(Long id);

    void excluir(Long id);
}
