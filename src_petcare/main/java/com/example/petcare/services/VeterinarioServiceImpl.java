package com.example.petcare.services;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.petcare.configs.exceptions.RecursoNaoEncontradoException;
import com.example.petcare.configs.exceptions.RegraNegocioException;
import com.example.petcare.domains.Veterinario;
import com.example.petcare.domains.enums.Especie;
import com.example.petcare.dtos.ConsultaResponseDTO;
import com.example.petcare.dtos.VeterinarioRequestDTO;
import com.example.petcare.repositories.AtendimentoRepository;
import com.example.petcare.repositories.ConsultaRepository;
import com.example.petcare.repositories.VeterinarioRepository;

@Service
public class VeterinarioServiceImpl implements VeterinarioService {
    private VeterinarioRepository veterinarioRepository;
    private ConsultaRepository consultaRepository;
    private AtendimentoRepository atendimentoRepository;

    public VeterinarioServiceImpl(VeterinarioRepository veterinarioRepository,
            ConsultaRepository consultaRepository,
            AtendimentoRepository atendimentoRepository) {
        this.veterinarioRepository = veterinarioRepository;
        this.consultaRepository = consultaRepository;
        this.atendimentoRepository = atendimentoRepository;
    }

    @Override
    @Transactional
    public VeterinarioRequestDTO inserir(VeterinarioRequestDTO dto) {
        validarDados(dto);
        String crmv = dto.getCrmv().trim().toUpperCase();
        if (veterinarioRepository.existsByCrmv(crmv)) {
            throw new RegraNegocioException("Já existe um veterinário cadastrado com este CRMV");
        }
        Veterinario v = Veterinario.builder()
                .nome(dto.getNome().trim())
                .crmv(crmv)
                .especies(new HashSet<>(dto.getEspecies()))
                .build();
        return toDTO(veterinarioRepository.save(v));
    }

    @Override
    @Transactional
    public VeterinarioRequestDTO editar(Long id, VeterinarioRequestDTO dto) {
        Veterinario v = buscarEntidade(id);
        validarDados(dto);
        String crmv = dto.getCrmv().trim().toUpperCase();
        if (!crmv.equals(v.getCrmv()) && veterinarioRepository.existsByCrmv(crmv)) {
            throw new RegraNegocioException("Já existe outro veterinário cadastrado com este CRMV");
        }
        v.setNome(dto.getNome().trim());
        v.setCrmv(crmv);
        v.getEspecies().clear();
        v.getEspecies().addAll(dto.getEspecies());
        return toDTO(veterinarioRepository.save(v));
    }

    @Override
    public List<VeterinarioRequestDTO> obterTodos() {
        return veterinarioRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public List<VeterinarioRequestDTO> obterHabilitados(Especie especie) {
        return veterinarioRepository.obterHabilitadosPorEspecie(especie)
                .stream().map(this::toDTO).toList();
    }

    @Override
    public VeterinarioRequestDTO obterPorId(Long id) {
        return toDTO(buscarEntidade(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultaResponseDTO> obterAgenda(Long id, LocalDate data) {
        buscarEntidade(id);
        LocalDate dia = data == null ? LocalDate.now() : data;
        return consultaRepository
                .obterAgendaVeterinario(id, dia.atStartOfDay(), dia.atTime(23, 59, 59))
                .stream()
                .map(ConsultaServiceImpl::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        Veterinario v = buscarEntidade(id);
        if (consultaRepository.existsByVeterinarioId(id) || atendimentoRepository.existsByVeterinarioId(id)) {
            throw new RegraNegocioException(
                    "Não é possível excluir um veterinário com consultas ou atendimentos registrados");
        }
        veterinarioRepository.delete(v);
    }

    private Veterinario buscarEntidade(Long id) {
        return veterinarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Veterinário não encontrado"));
    }

    private void validarDados(VeterinarioRequestDTO dto) {
        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new RegraNegocioException("O nome do veterinário é obrigatório");
        }
        if (dto.getCrmv() == null || dto.getCrmv().isBlank()) {
            throw new RegraNegocioException("O CRMV é obrigatório");
        }
        if (dto.getEspecies() == null || dto.getEspecies().isEmpty()) {
            throw new RegraNegocioException("Informe pelo menos uma espécie que o veterinário pode atender");
        }
    }

    private VeterinarioRequestDTO toDTO(Veterinario v) {
        return new VeterinarioRequestDTO(v.getId(), v.getNome(), v.getCrmv(),
                new HashSet<>(v.getEspecies()));
    }
}
