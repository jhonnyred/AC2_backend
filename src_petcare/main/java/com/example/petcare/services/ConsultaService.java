package com.example.petcare.services;

import java.util.List;

import com.example.petcare.domains.enums.StatusConsulta;
import com.example.petcare.dtos.ConsultaRequestDTO;
import com.example.petcare.dtos.ConsultaResponseDTO;

public interface ConsultaService {
    ConsultaResponseDTO agendar(ConsultaRequestDTO dto);

    List<ConsultaResponseDTO> obterTodos(StatusConsulta status);

    List<ConsultaResponseDTO> obterPorAnimal(Long animalId);

    ConsultaResponseDTO obterPorId(Long id);

    ConsultaResponseDTO cancelar(Long id);
}
