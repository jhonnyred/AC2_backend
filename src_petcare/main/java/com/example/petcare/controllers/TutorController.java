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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.petcare.dtos.AnimalRequestDTO;
import com.example.petcare.dtos.TutorRequestDTO;
import com.example.petcare.services.AnimalService;
import com.example.petcare.services.TutorService;

@RestController
@RequestMapping("/api/tutor")
public class TutorController {
    private TutorService tutorService;
    private AnimalService animalService;

    public TutorController(TutorService tutorService, AnimalService animalService) {
        this.tutorService = tutorService;
        this.animalService = animalService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public TutorRequestDTO inserir(@RequestBody TutorRequestDTO dto) {
        return tutorService.inserir(dto);
    }

    @PutMapping("{id}")
    public TutorRequestDTO editar(@PathVariable Long id, @RequestBody TutorRequestDTO dto) {
        return tutorService.editar(id, dto);
    }

    // GET /api/tutor ou /api/tutor?nome=maria
    @GetMapping()
    public List<TutorRequestDTO> obterTodos(@RequestParam(required = false) String nome) {
        return nome == null ? tutorService.obterTodos() : tutorService.buscarPorNome(nome);
    }

    @GetMapping("{id}")
    public TutorRequestDTO obterPorId(@PathVariable Long id) {
        return tutorService.obterPorId(id);
    }

    @GetMapping("{id}/animais")
    public List<AnimalRequestDTO> obterAnimais(@PathVariable Long id) {
        return animalService.obterPorTutor(id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        tutorService.excluir(id);
    }
}
