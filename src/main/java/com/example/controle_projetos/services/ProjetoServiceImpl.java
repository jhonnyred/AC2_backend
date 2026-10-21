package com.example.controle_projetos.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.controle_projetos.configs.exceptions.RegraNegocioException;
import com.example.controle_projetos.domains.Funcionario;
import com.example.controle_projetos.domains.Projeto;
import com.example.controle_projetos.dtos.DadosProjetoDTO;
import com.example.controle_projetos.dtos.FuncionarioDTO;
import com.example.controle_projetos.dtos.ProjetoDTO;
import com.example.controle_projetos.repositories.FuncionarioRepository;
import com.example.controle_projetos.repositories.ProjetoRepository;

@Service
public class ProjetoServiceImpl implements ProjetoService {
    private ProjetoRepository projetoRepository;
    private FuncionarioRepository funcionarioRepository;

    public ProjetoServiceImpl(ProjetoRepository projetoRepository,
            FuncionarioRepository funcionarioRepository) {
        this.projetoRepository = projetoRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    @Override
    @Transactional
    public void adicionar(ProjetoDTO dto) {
        if (dto.getDescricao() == null || dto.getDescricao().isBlank()) {
            throw new RegraNegocioException("A descrição do projeto é obrigatória");
        }
        if (dto.getDescricao().trim().length() > 200) {
            throw new RegraNegocioException("A descrição deve ter no máximo 200 caracteres");
        }
        if (dto.getDataInicio() == null) {
            throw new RegraNegocioException("A data de início é obrigatória");
        }
        if (dto.getDataFim() != null && dto.getDataFim().isBefore(dto.getDataInicio())) {
            throw new RegraNegocioException("A data de fim não pode ser anterior à data de início");
        }
        projetoRepository.save(new Projeto(dto.getDescricao().trim(),
                dto.getDataInicio(), dto.getDataFim()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjetoDTO> listarTodos() {
        return projetoRepository.findAll().stream()
                .map(p -> new ProjetoDTO(p.getId(), p.getDescricao(),
                        p.getDataInicio(), p.getDataFim()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DadosProjetoDTO buscarProjetoPorId(Integer id) {
        Projeto p = projetoRepository.buscarProjetoComFuncionarios(id)
                .orElseThrow(() -> new RegraNegocioException("Projeto não encontrado"));

        List<FuncionarioDTO> funcionarios = p.getFuncionarios().stream()
                .map(f -> new FuncionarioDTO(f.getId(), f.getNome(), f.getSetor().getId()))
                .toList();

        return new DadosProjetoDTO(p.getId(), p.getDescricao(),
                p.getDataInicio(), p.getDataFim(), funcionarios);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjetoDTO> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        if (inicio.isAfter(fim)) {
            throw new RegraNegocioException("A data inicial do filtro não pode ser maior que a final");
        }
        return projetoRepository.buscarPorPeriodoDeInicio(inicio, fim).stream()
                .map(p -> new ProjetoDTO(p.getId(), p.getDescricao(),
                        p.getDataInicio(), p.getDataFim()))
                .toList();
    }

    @Override
    @Transactional
    public void vincularFuncionario(Integer idProjeto, Integer idFuncionario) {
        Projeto projeto = projetoRepository.buscarProjetoComFuncionarios(idProjeto)
                .orElseThrow(() -> new RegraNegocioException("Projeto não encontrado"));
        Funcionario funcionario = funcionarioRepository.findById(idFuncionario)
                .orElseThrow(() -> new RegraNegocioException("Funcionário não encontrado"));

        boolean jaVinculado = projeto.getFuncionarios().stream()
                .anyMatch(f -> f.getId().equals(idFuncionario));
        if (jaVinculado) {
            throw new RegraNegocioException("Funcionário já está vinculado a este projeto");
        }

        projeto.getFuncionarios().add(funcionario);
        projetoRepository.save(projeto);
    }
}
