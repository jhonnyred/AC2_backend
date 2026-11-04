package com.example.petcare.services;

import java.util.List;

import com.example.petcare.dtos.AtendimentoRequestDTO;
import com.example.petcare.dtos.AtendimentoResponseDTO;

public interface AtendimentoService {
    AtendimentoResponseDTO registrar(AtendimentoRequestDTO dto);

    List<AtendimentoResponseDTO> obterTodos();

    List<AtendimentoResponseDTO> obterPorAnimal(Long animalId);

    AtendimentoResponseDTO obterPorId(Long id);

    // Texto formatado do registro de atendimento, pronto para impressão
    String gerarImpressao(Long id);
}
