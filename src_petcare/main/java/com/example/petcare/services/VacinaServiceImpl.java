package com.example.petcare.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.petcare.configs.exceptions.RecursoNaoEncontradoException;
import com.example.petcare.configs.exceptions.RegraNegocioException;
import com.example.petcare.domains.Animal;
import com.example.petcare.domains.Vacina;
import com.example.petcare.domains.Veterinario;
import com.example.petcare.dtos.VacinaRequestDTO;
import com.example.petcare.repositories.AnimalRepository;
import com.example.petcare.repositories.VacinaRepository;
import com.example.petcare.repositories.VeterinarioRepository;

@Service
public class VacinaServiceImpl implements VacinaService {
    private VacinaRepository vacinaRepository;
    private AnimalRepository animalRepository;
    private VeterinarioRepository veterinarioRepository;

    public VacinaServiceImpl(VacinaRepository vacinaRepository,
            AnimalRepository animalRepository,
            VeterinarioRepository veterinarioRepository) {
        this.vacinaRepository = vacinaRepository;
        this.animalRepository = animalRepository;
        this.veterinarioRepository = veterinarioRepository;
    }

    @Override
    @Transactional
    public VacinaRequestDTO registrar(VacinaRequestDTO dto) {
        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new RegraNegocioException("O nome da vacina é obrigatório");
        }
        if (dto.getLote() == null || dto.getLote().isBlank()) {
            throw new RegraNegocioException("O lote da vacina é obrigatório");
        }
        if (dto.getDataAplicacao() == null) {
            throw new RegraNegocioException("A data de aplicação é obrigatória");
        }
        if (dto.getDataAplicacao().isAfter(LocalDate.now())) {
            throw new RegraNegocioException("A data de aplicação não pode ser no futuro");
        }
        if (dto.getAnimalId() == null) {
            throw new RegraNegocioException("O animal é obrigatório");
        }
        Animal animal = animalRepository.findById(dto.getAnimalId())
                .orElseThrow(() -> new RegraNegocioException("Animal não encontrado"));
        if (animal.getDataNascimento() != null && dto.getDataAplicacao().isBefore(animal.getDataNascimento())) {
            throw new RegraNegocioException("A data de aplicação não pode ser anterior ao nascimento do animal");
        }

        Veterinario vet = null;
        if (dto.getVeterinarioId() != null) {
            vet = veterinarioRepository.findById(dto.getVeterinarioId())
                    .orElseThrow(() -> new RegraNegocioException("Veterinário não encontrado"));
            if (!vet.atende(animal.getEspecie())) {
                throw new RegraNegocioException("O veterinário não está habilitado para a espécie do animal");
            }
        }

        Vacina v = Vacina.builder()
                .nome(dto.getNome().trim())
                .lote(dto.getLote().trim())
                .dataAplicacao(dto.getDataAplicacao())
                .animal(animal)
                .veterinario(vet)
                .build();
        return toDTO(vacinaRepository.save(v));
    }

    @Override
    public List<VacinaRequestDTO> obterTodos() {
        return vacinaRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public List<VacinaRequestDTO> obterPorAnimal(Long animalId) {
        if (!animalRepository.existsById(animalId)) {
            throw new RecursoNaoEncontradoException("Animal não encontrado");
        }
        return vacinaRepository.obterCarteiraVacinacao(animalId).stream().map(this::toDTO).toList();
    }

    @Override
    public VacinaRequestDTO obterPorId(Long id) {
        return toDTO(buscarEntidade(id));
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        vacinaRepository.delete(buscarEntidade(id));
    }

    private Vacina buscarEntidade(Long id) {
        return vacinaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Vacina não encontrada"));
    }

    private VacinaRequestDTO toDTO(Vacina v) {
        return new VacinaRequestDTO(v.getId(), v.getNome(), v.getDataAplicacao(), v.getLote(),
                v.getAnimal().getId(),
                v.getVeterinario() == null ? null : v.getVeterinario().getId());
    }
}
