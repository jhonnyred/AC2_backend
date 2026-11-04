package com.example.petcare.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.petcare.configs.exceptions.RecursoNaoEncontradoException;
import com.example.petcare.configs.exceptions.RegraNegocioException;
import com.example.petcare.domains.Animal;
import com.example.petcare.domains.Tutor;
import com.example.petcare.dtos.AnimalRequestDTO;
import com.example.petcare.dtos.HistoricoAnimalDTO;
import com.example.petcare.dtos.TutorRequestDTO;
import com.example.petcare.repositories.AnimalRepository;
import com.example.petcare.repositories.AtendimentoRepository;
import com.example.petcare.repositories.ConsultaRepository;
import com.example.petcare.repositories.TutorRepository;
import com.example.petcare.repositories.VacinaRepository;

@Service
public class AnimalServiceImpl implements AnimalService {
    private AnimalRepository animalRepository;
    private TutorRepository tutorRepository;
    private ConsultaRepository consultaRepository;
    private AtendimentoRepository atendimentoRepository;
    private VacinaRepository vacinaRepository;
    private AtendimentoService atendimentoService;
    private VacinaService vacinaService;

    public AnimalServiceImpl(AnimalRepository animalRepository,
            TutorRepository tutorRepository,
            ConsultaRepository consultaRepository,
            AtendimentoRepository atendimentoRepository,
            VacinaRepository vacinaRepository,
            AtendimentoService atendimentoService,
            VacinaService vacinaService) {
        this.animalRepository = animalRepository;
        this.tutorRepository = tutorRepository;
        this.consultaRepository = consultaRepository;
        this.atendimentoRepository = atendimentoRepository;
        this.vacinaRepository = vacinaRepository;
        this.atendimentoService = atendimentoService;
        this.vacinaService = vacinaService;
    }

    @Override
    @Transactional
    public AnimalRequestDTO inserir(AnimalRequestDTO dto) {
        Tutor tutor = validarDados(dto);
        Animal a = Animal.builder()
                .nome(dto.getNome().trim())
                .especie(dto.getEspecie())
                .raca(dto.getRaca())
                .dataNascimento(dto.getDataNascimento())
                .tutor(tutor)
                .build();
        return toDTO(animalRepository.save(a));
    }

    @Override
    @Transactional
    public AnimalRequestDTO editar(Long id, AnimalRequestDTO dto) {
        Animal a = buscarEntidade(id);
        Tutor tutor = validarDados(dto);
        a.setNome(dto.getNome().trim());
        a.setEspecie(dto.getEspecie());
        a.setRaca(dto.getRaca());
        a.setDataNascimento(dto.getDataNascimento());
        a.setTutor(tutor);
        return toDTO(animalRepository.save(a));
    }

    @Override
    public List<AnimalRequestDTO> obterTodos() {
        return animalRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public List<AnimalRequestDTO> obterPorTutor(Long tutorId) {
        if (!tutorRepository.existsById(tutorId)) {
            throw new RecursoNaoEncontradoException("Tutor não encontrado");
        }
        return animalRepository.obterPorTutor(tutorId).stream().map(this::toDTO).toList();
    }

    @Override
    public AnimalRequestDTO obterPorId(Long id) {
        return toDTO(buscarEntidade(id));
    }

    @Override
    @Transactional(readOnly = true)
    public HistoricoAnimalDTO obterHistorico(Long id) {
        Animal a = buscarEntidade(id);
        Tutor t = a.getTutor();
        return HistoricoAnimalDTO.builder()
                .animal(toDTO(a))
                .tutor(new TutorRequestDTO(t.getId(), t.getNome(), t.getCpf(),
                        t.getTelefone(), t.getEndereco()))
                .atendimentos(atendimentoService.obterPorAnimal(id))
                .vacinas(vacinaService.obterPorAnimal(id))
                .build();
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        Animal a = buscarEntidade(id);
        if (consultaRepository.existsByAnimalId(id)
                || atendimentoRepository.existsByAnimalId(id)
                || vacinaRepository.existsByAnimalId(id)) {
            throw new RegraNegocioException(
                    "Não é possível excluir um animal com consultas, atendimentos ou vacinas registrados");
        }
        animalRepository.delete(a);
    }

    private Animal buscarEntidade(Long id) {
        return animalRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Animal não encontrado"));
    }

    private Tutor validarDados(AnimalRequestDTO dto) {
        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new RegraNegocioException("O nome do animal é obrigatório");
        }
        if (dto.getEspecie() == null) {
            throw new RegraNegocioException("A espécie do animal é obrigatória");
        }
        if (dto.getDataNascimento() != null && dto.getDataNascimento().isAfter(LocalDate.now())) {
            throw new RegraNegocioException("A data de nascimento não pode ser no futuro");
        }
        if (dto.getTutorId() == null) {
            throw new RegraNegocioException("O tutor responsável é obrigatório");
        }
        return tutorRepository.findById(dto.getTutorId())
                .orElseThrow(() -> new RegraNegocioException("Tutor não encontrado"));
    }

    private AnimalRequestDTO toDTO(Animal a) {
        return new AnimalRequestDTO(a.getId(), a.getNome(), a.getEspecie(), a.getRaca(),
                a.getDataNascimento(), a.getTutor().getId(), a.getTutor().getNome());
    }
}
