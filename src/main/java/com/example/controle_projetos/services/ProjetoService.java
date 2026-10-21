package com.example.controle_projetos.services;

import java.time.LocalDate;
import java.util.List;

import com.example.controle_projetos.dtos.DadosProjetoDTO;
import com.example.controle_projetos.dtos.ProjetoDTO;

public interface ProjetoService {
    void adicionar(ProjetoDTO dto);

    List<ProjetoDTO> listarTodos();

    DadosProjetoDTO buscarProjetoPorId(Integer id);

    List<ProjetoDTO> buscarPorPeriodo(LocalDate inicio, LocalDate fim);

    void vincularFuncionario(Integer idProjeto, Integer idFuncionario);
}
