package com.example.controle_projetos.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.controle_projetos.configs.exceptions.RegraNegocioException;
import com.example.controle_projetos.domains.Setor;
import com.example.controle_projetos.dtos.DadosSetorDTO;
import com.example.controle_projetos.dtos.FuncionarioDTO;
import com.example.controle_projetos.dtos.SetorDTO;
import com.example.controle_projetos.repositories.SetorRepository;

@Service
public class SetorServiceImpl implements SetorService {
    private SetorRepository setorRepository;

    public SetorServiceImpl(SetorRepository setorRepository) {
        this.setorRepository = setorRepository;
    }

    @Override
    @Transactional
    public void adicionar(SetorDTO dto) {
        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new RegraNegocioException("O nome do setor é obrigatório");
        }
        String nome = dto.getNome().trim();
        if (setorRepository.existsByNomeIgnoreCase(nome)) {
            throw new RegraNegocioException("Já existe um setor com esse nome");
        }
        setorRepository.save(new Setor(nome));
    }

    @Override
    @Transactional(readOnly = true)
    public DadosSetorDTO buscarSetorPorId(Integer idSetor) {
        Setor setor = setorRepository.buscarSetorComFuncionarios(idSetor)
                .orElseThrow(() -> new RegraNegocioException("Setor não encontrado"));
        return converter(setor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DadosSetorDTO> listarComFuncionarios() {
        return setorRepository.listarSetoresComFuncionarios().stream()
                .map(this::converter)
                .toList();
    }

    private DadosSetorDTO converter(Setor setor) {
        List<FuncionarioDTO> funcionarios = setor.getFuncionarios().stream()
                .map(f -> new FuncionarioDTO(f.getId(), f.getNome(), setor.getId()))
                .toList();
        return new DadosSetorDTO(setor.getId(), setor.getNome(), funcionarios);
    }
}
