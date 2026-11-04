package com.example.petcare.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.petcare.configs.exceptions.RecursoNaoEncontradoException;
import com.example.petcare.configs.exceptions.RegraNegocioException;
import com.example.petcare.domains.Animal;
import com.example.petcare.domains.Atendimento;
import com.example.petcare.domains.Consulta;
import com.example.petcare.domains.Veterinario;
import com.example.petcare.domains.enums.StatusConsulta;
import com.example.petcare.dtos.AtendimentoRequestDTO;
import com.example.petcare.dtos.AtendimentoResponseDTO;
import com.example.petcare.repositories.AnimalRepository;
import com.example.petcare.repositories.AtendimentoRepository;
import com.example.petcare.repositories.ConsultaRepository;
import com.example.petcare.repositories.VeterinarioRepository;

@Service
public class AtendimentoServiceImpl implements AtendimentoService {
    private static final DateTimeFormatter FMT_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter FMT_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private AtendimentoRepository atendimentoRepository;
    private AnimalRepository animalRepository;
    private VeterinarioRepository veterinarioRepository;
    private ConsultaRepository consultaRepository;

    public AtendimentoServiceImpl(AtendimentoRepository atendimentoRepository,
            AnimalRepository animalRepository,
            VeterinarioRepository veterinarioRepository,
            ConsultaRepository consultaRepository) {
        this.atendimentoRepository = atendimentoRepository;
        this.animalRepository = animalRepository;
        this.veterinarioRepository = veterinarioRepository;
        this.consultaRepository = consultaRepository;
    }

    @Override
    @Transactional
    public AtendimentoResponseDTO registrar(AtendimentoRequestDTO dto) {
        if (dto.getDiagnostico() == null || dto.getDiagnostico().isBlank()) {
            throw new RegraNegocioException("O diagnóstico é obrigatório");
        }

        Animal animal;
        Veterinario vet;
        Consulta consulta = null;

        if (dto.getConsultaId() != null) {
            // Atendimento a partir de uma consulta agendada
            consulta = consultaRepository.findById(dto.getConsultaId())
                    .orElseThrow(() -> new RegraNegocioException("Consulta não encontrada"));
            if (consulta.getStatus() == StatusConsulta.CANCELADA) {
                throw new RegraNegocioException("Não é possível registrar atendimento de uma consulta cancelada");
            }
            if (consulta.getStatus() == StatusConsulta.REALIZADA
                    || atendimentoRepository.existsByConsultaId(consulta.getId())) {
                throw new RegraNegocioException("Esta consulta já possui um atendimento registrado");
            }
            animal = consulta.getAnimal();
            vet = consulta.getVeterinario();
            if (dto.getAnimalId() != null && !dto.getAnimalId().equals(animal.getId())) {
                throw new RegraNegocioException("O animal informado não corresponde ao animal da consulta");
            }
            if (dto.getVeterinarioId() != null && !dto.getVeterinarioId().equals(vet.getId())) {
                throw new RegraNegocioException("O veterinário informado não corresponde ao veterinário da consulta");
            }
        } else {
            // Atendimento sem agendamento prévio (ex.: emergência)
            if (dto.getAnimalId() == null || dto.getVeterinarioId() == null) {
                throw new RegraNegocioException("Informe a consulta ou então o animal e o veterinário");
            }
            animal = animalRepository.findById(dto.getAnimalId())
                    .orElseThrow(() -> new RegraNegocioException("Animal não encontrado"));
            vet = veterinarioRepository.findById(dto.getVeterinarioId())
                    .orElseThrow(() -> new RegraNegocioException("Veterinário não encontrado"));
        }

        if (!vet.atende(animal.getEspecie())) {
            throw new RegraNegocioException("O veterinário " + vet.getNome()
                    + " não está habilitado para atender a espécie " + animal.getEspecie());
        }

        LocalDateTime data = dto.getDataAtendimento() == null
                ? LocalDateTime.now().withNano(0)
                : dto.getDataAtendimento();
        if (data.isAfter(LocalDateTime.now().plusMinutes(5))) {
            throw new RegraNegocioException("A data do atendimento não pode ser no futuro");
        }

        Atendimento at = Atendimento.builder()
                .animal(animal)
                .veterinario(vet)
                .consulta(consulta)
                .dataAtendimento(data)
                .diagnostico(dto.getDiagnostico().trim())
                .observacoes(dto.getObservacoes())
                .orientacoes(dto.getOrientacoes())
                .build();
        at = atendimentoRepository.save(at);

        if (consulta != null) {
            consulta.setStatus(StatusConsulta.REALIZADA);
            consultaRepository.save(consulta);
        }
        return toDTO(at);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtendimentoResponseDTO> obterTodos() {
        return atendimentoRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtendimentoResponseDTO> obterPorAnimal(Long animalId) {
        if (!animalRepository.existsById(animalId)) {
            throw new RecursoNaoEncontradoException("Animal não encontrado");
        }
        return atendimentoRepository.obterHistoricoPorAnimal(animalId)
                .stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AtendimentoResponseDTO obterPorId(Long id) {
        return toDTO(buscarEntidade(id));
    }

    @Override
    @Transactional(readOnly = true)
    public String gerarImpressao(Long id) {
        Atendimento at = buscarEntidade(id);
        Animal a = at.getAnimal();
        Veterinario v = at.getVeterinario();
        String linha = "=".repeat(60);
        StringBuilder sb = new StringBuilder();
        sb.append(linha).append("\n");
        sb.append("          PETCARE DIGITAL SOLUTIONS - PRONTUÁRIO\n");
        sb.append("              Registro de Atendimento Nº ").append(at.getId()).append("\n");
        sb.append(linha).append("\n");
        sb.append("Data do atendimento: ").append(at.getDataAtendimento().format(FMT_DATA_HORA)).append("\n");
        if (at.getConsulta() != null) {
            sb.append("Consulta Nº: ").append(at.getConsulta().getId())
                    .append(" | Local: ").append(at.getConsulta().getLocal()).append("\n");
        }
        sb.append("\n--- ANIMAL ---\n");
        sb.append("Nome: ").append(a.getNome()).append("\n");
        sb.append("Espécie: ").append(a.getEspecie())
                .append(" | Raça: ").append(a.getRaca() == null ? "-" : a.getRaca()).append("\n");
        sb.append("Nascimento: ")
                .append(a.getDataNascimento() == null ? "-" : a.getDataNascimento().format(FMT_DATA)).append("\n");
        sb.append("\n--- TUTOR ---\n");
        sb.append("Nome: ").append(a.getTutor().getNome()).append("\n");
        sb.append("CPF: ").append(formatarCpf(a.getTutor().getCpf()))
                .append(" | Telefone: ").append(a.getTutor().getTelefone()).append("\n");
        sb.append("\n--- VETERINÁRIO ---\n");
        sb.append(v.getNome()).append(" | CRMV: ").append(v.getCrmv()).append("\n");
        sb.append("\n--- DIAGNÓSTICO ---\n").append(at.getDiagnostico()).append("\n");
        sb.append("\n--- OBSERVAÇÕES ---\n").append(valorOuTraco(at.getObservacoes())).append("\n");
        sb.append("\n--- ORIENTAÇÕES AO TUTOR ---\n").append(valorOuTraco(at.getOrientacoes())).append("\n");
        sb.append("\n\n_______________________________\n");
        sb.append(v.getNome()).append(" - CRMV ").append(v.getCrmv()).append("\n");
        sb.append(linha).append("\n");
        return sb.toString();
    }

    private Atendimento buscarEntidade(Long id) {
        return atendimentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Atendimento não encontrado"));
    }

    private String valorOuTraco(String s) {
        return (s == null || s.isBlank()) ? "-" : s;
    }

    private String formatarCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return cpf;
        }
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "."
                + cpf.substring(6, 9) + "-" + cpf.substring(9);
    }

    private AtendimentoResponseDTO toDTO(Atendimento at) {
        return new AtendimentoResponseDTO(at.getId(),
                at.getAnimal().getId(), at.getAnimal().getNome(),
                at.getVeterinario().getId(), at.getVeterinario().getNome(), at.getVeterinario().getCrmv(),
                at.getConsulta() == null ? null : at.getConsulta().getId(),
                at.getDataAtendimento(), at.getDiagnostico(),
                at.getObservacoes(), at.getOrientacoes());
    }
}
