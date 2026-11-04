package com.example.petcare.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.petcare.dtos.AnimalRequestDTO;
import com.example.petcare.dtos.AtendimentoResponseDTO;
import com.example.petcare.dtos.ConsultaResponseDTO;
import com.example.petcare.dtos.HistoricoAnimalDTO;
import com.example.petcare.dtos.VacinaRequestDTO;
import com.example.petcare.services.AnimalService;
import com.example.petcare.services.AtendimentoService;
import com.example.petcare.services.ConsultaService;
import com.example.petcare.services.VacinaService;

@RestController
@RequestMapping("/api/animal")
public class AnimalController {
    private AnimalService animalService;
    private ConsultaService consultaService;
    private AtendimentoService atendimentoService;
    private VacinaService vacinaService;

    public AnimalController(AnimalService animalService, ConsultaService consultaService,
            AtendimentoService atendimentoService, VacinaService vacinaService) {
        this.animalService = animalService;
        this.consultaService = consultaService;
        this.atendimentoService = atendimentoService;
        this.vacinaService = vacinaService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public AnimalRequestDTO inserir(@RequestBody AnimalRequestDTO dto) {
        return animalService.inserir(dto);
    }

    @PutMapping("{id}")
    public AnimalRequestDTO editar(@PathVariable Long id, @RequestBody AnimalRequestDTO dto) {
        return animalService.editar(id, dto);
    }

    @GetMapping()
    public List<AnimalRequestDTO> obterTodos() {
        return animalService.obterTodos();
    }

    @GetMapping("{id}")
    public AnimalRequestDTO obterPorId(@PathVariable Long id) {
        return animalService.obterPorId(id);
    }

    // Histórico completo: atendimentos + vacinas
    @GetMapping("{id}/historico")
    public HistoricoAnimalDTO obterHistorico(@PathVariable Long id) {
        return animalService.obterHistorico(id);
    }

    @GetMapping("{id}/consultas")
    public List<ConsultaResponseDTO> obterConsultas(@PathVariable Long id) {
        return consultaService.obterPorAnimal(id);
    }

    @GetMapping("{id}/atendimentos")
    public List<AtendimentoResponseDTO> obterAtendimentos(@PathVariable Long id) {
        return atendimentoService.obterPorAnimal(id);
    }

    @GetMapping("{id}/vacinas")
    public List<VacinaRequestDTO> obterVacinas(@PathVariable Long id) {
        return vacinaService.obterPorAnimal(id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        animalService.excluir(id);
    }
}
