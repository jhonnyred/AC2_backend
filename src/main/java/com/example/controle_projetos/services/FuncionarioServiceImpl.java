package com.example.controle_projetos.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.controle_projetos.configs.exceptions.RegraNegocioException;
import com.example.controle_projetos.domains.Funcionario;
import com.example.controle_projetos.domains.Projeto;
import com.example.controle_projetos.domains.Setor;
import com.example.controle_projetos.dtos.DadosProjetoDTO;
import com.example.controle_projetos.dtos.FuncionarioDTO;
import com.example.controle_projetos.repositories.FuncionarioRepository;
import com.example.controle_projetos.repositories.SetorRepository;

@Service
public class FuncionarioServiceImpl implements FuncionarioService {
    private FuncionarioRepository funcionarioRepository;
    private SetorRepository setorRepository;

    public FuncionarioServiceImpl(FuncionarioRepository funcionarioRepository,
            SetorRepository setorRepository) {
        this.funcionarioRepository = funcionarioRepository;
        this.setorRepository = setorRepository;
    }

    @Override
    @Transactional
    public void adicionar(FuncionarioDTO dto) {
        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new RegraNegocioException("O nome do funcionário é obrigatório");
        }
        if (dto.getIdSetor() == null) {
            throw new RegraNegocioException("O setor do funcionário é obrigatório");
        }
        Setor setor = setorRepository.findById(dto.getIdSetor())
                .orElseThrow(() -> new RegraNegocioException("Setor não encontrado"));

        Funcionario funcionario = new Funcionario(dto.getNome().trim());
        funcionario.setSetor(setor);
        funcionarioRepository.save(funcionario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DadosProjetoDTO> buscarProjetos(Integer idFuncionario) {
        if (!funcionarioRepository.existsById(idFuncionario)) {
            throw new RegraNegocioException("Funcionário não encontrado");
        }
        return funcionarioRepository.buscarProjetosDoFuncionario(idFuncionario).stream()
                .map(this::converter)
                .toList();
    }

    private DadosProjetoDTO converter(Projeto p) {
        List<FuncionarioDTO> funcionarios = p.getFuncionarios().stream()
                .map(f -> new FuncionarioDTO(f.getId(), f.getNome(), f.getSetor().getId()))
                .toList();
        return new DadosProjetoDTO(p.getId(), p.getDescricao(),
                p.getDataInicio(), p.getDataFim(), funcionarios);
    }
}
