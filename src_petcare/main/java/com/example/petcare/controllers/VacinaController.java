package com.example.petcare.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.petcare.dtos.VacinaRequestDTO;
import com.example.petcare.services.VacinaService;

@RestController
@RequestMapping("/api/vacina")
public class VacinaController {
    private VacinaService vacinaService;

    public VacinaController(VacinaService vacinaService) {
        this.vacinaService = vacinaService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public VacinaRequestDTO registrar(@RequestBody VacinaRequestDTO dto) {
        return vacinaService.registrar(dto);
    }

    @GetMapping()
    public List<VacinaRequestDTO> obterTodos() {
        return vacinaService.obterTodos();
    }

    @GetMapping("{id}")
    public VacinaRequestDTO obterPorId(@PathVariable Long id) {
        return vacinaService.obterPorId(id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        vacinaService.excluir(id);
    }
}
