package com.example.petcare.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.petcare.configs.exceptions.RecursoNaoEncontradoException;
import com.example.petcare.configs.exceptions.RegraNegocioException;
import com.example.petcare.domains.Animal;
import com.example.petcare.domains.Consulta;
import com.example.petcare.domains.Veterinario;
import com.example.petcare.domains.enums.StatusConsulta;
import com.example.petcare.dtos.ConsultaRequestDTO;
import com.example.petcare.dtos.ConsultaResponseDTO;
import com.example.petcare.repositories.AnimalRepository;
import com.example.petcare.repositories.ConsultaRepository;
import com.example.petcare.repositories.VeterinarioRepository;

@Service
public class ConsultaServiceImpl implements ConsultaService {
    // Duração padrão de uma consulta, usada para verificar conflito de horário
    public static final int DURACAO_CONSULTA_MINUTOS = 30;

    private ConsultaRepository consultaRepository;
    private AnimalRepository animalRepository;
    private VeterinarioRepository veterinarioRepository;

    public ConsultaServiceImpl(ConsultaRepository consultaRepository,
            AnimalRepository animalRepository,
            VeterinarioRepository veterinarioRepository) {
        this.consultaRepository = consultaRepository;
        this.animalRepository = animalRepository;
        this.veterinarioRepository = veterinarioRepository;
    }

    @Override
    @Transactional
    public ConsultaResponseDTO agendar(ConsultaRequestDTO dto) {
        if (dto.getAnimalId() == null) {
            throw new RegraNegocioException("O animal é obrigatório");
        }
        if (dto.getVeterinarioId() == null) {
            throw new RegraNegocioException("O veterinário é obrigatório");
        }
        if (dto.getLocal() == null || dto.getLocal().isBlank()) {
            throw new RegraNegocioException("O local da consulta é obrigatório");
        }
        if (dto.getDataHora() == null) {
            throw new RegraNegocioException("A data e o horário da consulta são obrigatórios");
        }
        if (dto.getDataHora().isBefore(LocalDateTime.now())) {
            throw new RegraNegocioException("Não é possível agendar consultas em data/horário passado");
        }

        Animal animal = animalRepository.findById(dto.getAnimalId())
                .orElseThrow(() -> new RegraNegocioException("Animal não encontrado"));
        Veterinario vet = veterinarioRepository.findById(dto.getVeterinarioId())
                .orElseThrow(() -> new RegraNegocioException("Veterinário não encontrado"));

        // Regra: veterinário precisa estar habilitado para a espécie do animal
        if (!vet.atende(animal.getEspecie())) {
            throw new RegraNegocioException("O veterinário " + vet.getNome()
                    + " não está habilitado para atender a espécie " + animal.getEspecie());
        }

        // Regra: veterinário não pode ter duas consultas no mesmo horário
        LocalDateTime inicio = dto.getDataHora().minusMinutes(DURACAO_CONSULTA_MINUTOS);
        LocalDateTime fim = dto.getDataHora().plusMinutes(DURACAO_CONSULTA_MINUTOS);
        if (consultaRepository.existeConflitoVeterinario(vet.getId(), inicio, fim)) {
            throw new RegraNegocioException("O veterinário já possui uma consulta agendada neste horário");
        }
        if (consultaRepository.existeConflitoAnimal(animal.getId(), inicio, fim)) {
            throw new RegraNegocioException("O animal já possui uma consulta agendada neste horário");
        }

        Consulta c = Consulta.builder()
                .animal(animal)
                .veterinario(vet)
                .local(dto.getLocal().trim())
                .dataHora(dto.getDataHora())
                .status(StatusConsulta.AGENDADA)
                .build();
        return toResponseDTO(consultaRepository.save(c));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultaResponseDTO> obterTodos(StatusConsulta status) {
        List<Consulta> consultas = status == null
                ? consultaRepository.findAll()
                : consultaRepository.findByStatusOrderByDataHora(status);
        return consultas.stream().map(ConsultaServiceImpl::toResponseDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultaResponseDTO> obterPorAnimal(Long animalId) {
        if (!animalRepository.existsById(animalId)) {
            throw new RecursoNaoEncontradoException("Animal não encontrado");
        }
        return consultaRepository.findByAnimalIdOrderByDataHoraDesc(animalId)
                .stream().map(ConsultaServiceImpl::toResponseDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ConsultaResponseDTO obterPorId(Long id) {
        return toResponseDTO(buscarEntidade(id));
    }

    @Override
    @Transactional
    public ConsultaResponseDTO cancelar(Long id) {
        Consulta c = buscarEntidade(id);
        if (c.getStatus() == StatusConsulta.CANCELADA) {
            throw new RegraNegocioException("Esta consulta já está cancelada");
        }
        if (c.getStatus() == StatusConsulta.REALIZADA) {
            throw new RegraNegocioException("Não é possível cancelar uma consulta já realizada");
        }
        c.setStatus(StatusConsulta.CANCELADA);
        return toResponseDTO(consultaRepository.save(c));
    }

    private Consulta buscarEntidade(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Consulta não encontrada"));
    }

    public static ConsultaResponseDTO toResponseDTO(Consulta c) {
        return new ConsultaResponseDTO(c.getId(),
                c.getAnimal().getId(), c.getAnimal().getNome(),
                c.getVeterinario().getId(), c.getVeterinario().getNome(),
                c.getLocal(), c.getDataHora(), c.getStatus());
    }
}
