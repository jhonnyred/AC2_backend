package com.example.petcare.services;

import java.time.LocalDate;
import java.util.List;

import com.example.petcare.domains.enums.Especie;
import com.example.petcare.dtos.ConsultaResponseDTO;
import com.example.petcare.dtos.VeterinarioRequestDTO;

public interface VeterinarioService {
    VeterinarioRequestDTO inserir(VeterinarioRequestDTO dto);

    VeterinarioRequestDTO editar(Long id, VeterinarioRequestDTO dto);

    List<VeterinarioRequestDTO> obterTodos();

    List<VeterinarioRequestDTO> obterHabilitados(Especie especie);

    VeterinarioRequestDTO obterPorId(Long id);

    List<ConsultaResponseDTO> obterAgenda(Long id, LocalDate data);

    void excluir(Long id);
}
