package com.example.petcare.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.petcare.configs.exceptions.RecursoNaoEncontradoException;
import com.example.petcare.configs.exceptions.RegraNegocioException;
import com.example.petcare.domains.Tutor;
import com.example.petcare.dtos.TutorRequestDTO;
import com.example.petcare.repositories.AnimalRepository;
import com.example.petcare.repositories.TutorRepository;

@Service
public class TutorServiceImpl implements TutorService {
    private TutorRepository tutorRepository;
    private AnimalRepository animalRepository;

    public TutorServiceImpl(TutorRepository tutorRepository,
            AnimalRepository animalRepository) {
        this.tutorRepository = tutorRepository;
        this.animalRepository = animalRepository;
    }

    @Override
    @Transactional
    public TutorRequestDTO inserir(TutorRequestDTO dto) {
        String cpf = validarDados(dto);
        if (tutorRepository.existsByCpf(cpf)) {
            throw new RegraNegocioException("Já existe um tutor cadastrado com este CPF");
        }
        Tutor t = Tutor.builder()
                .nome(dto.getNome().trim())
                .cpf(cpf)
                .telefone(dto.getTelefone())
                .endereco(dto.getEndereco())
                .build();
        return toDTO(tutorRepository.save(t));
    }

    @Override
    @Transactional
    public TutorRequestDTO editar(Long id, TutorRequestDTO dto) {
        Tutor t = buscarEntidade(id);
        String cpf = validarDados(dto);
        tutorRepository.findByCpf(cpf).ifPresent(outro -> {
            if (!outro.getId().equals(id)) {
                throw new RegraNegocioException("Já existe outro tutor cadastrado com este CPF");
            }
        });
        t.setNome(dto.getNome().trim());
        t.setCpf(cpf);
        t.setTelefone(dto.getTelefone());
        t.setEndereco(dto.getEndereco());
        return toDTO(tutorRepository.save(t));
    }

    @Override
    public List<TutorRequestDTO> obterTodos() {
        return tutorRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public List<TutorRequestDTO> buscarPorNome(String nome) {
        return tutorRepository.buscarPorNome(nome == null ? "" : nome)
                .stream().map(this::toDTO).toList();
    }

    @Override
    public TutorRequestDTO obterPorId(Long id) {
        return toDTO(buscarEntidade(id));
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        Tutor t = buscarEntidade(id);
        if (animalRepository.existsByTutorId(id)) {
            throw new RegraNegocioException("Não é possível excluir um tutor que possui animais cadastrados");
        }
        tutorRepository.delete(t);
    }

    private Tutor buscarEntidade(Long id) {
        return tutorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tutor não encontrado"));
    }

    // Valida os campos obrigatórios e devolve o CPF somente com números
    private String validarDados(TutorRequestDTO dto) {
        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new RegraNegocioException("O nome do tutor é obrigatório");
        }
        if (dto.getCpf() == null || dto.getCpf().isBlank()) {
            throw new RegraNegocioException("O CPF do tutor é obrigatório");
        }
        String cpf = dto.getCpf().replaceAll("\\D", "");
        if (!cpfValido(cpf)) {
            throw new RegraNegocioException("CPF inválido");
        }
        if (dto.getTelefone() == null || dto.getTelefone().isBlank()) {
            throw new RegraNegocioException("O telefone do tutor é obrigatório");
        }
        if (dto.getEndereco() == null || dto.getEndereco().isBlank()) {
            throw new RegraNegocioException("O endereço do tutor é obrigatório");
        }
        return cpf;
    }

    // Validação dos dígitos verificadores do CPF
    private boolean cpfValido(String cpf) {
        if (cpf.length() != 11 || cpf.chars().distinct().count() == 1) {
            return false;
        }
        for (int j = 9; j <= 10; j++) {
            int soma = 0;
            for (int i = 0; i < j; i++) {
                soma += (cpf.charAt(i) - '0') * (j + 1 - i);
            }
            int digito = (soma * 10) % 11;
            if (digito == 10) {
                digito = 0;
            }
            if (digito != cpf.charAt(j) - '0') {
                return false;
            }
        }
        return true;
    }

    private TutorRequestDTO toDTO(Tutor t) {
        return new TutorRequestDTO(t.getId(), t.getNome(), t.getCpf(),
                t.getTelefone(), t.getEndereco());
    }
}
