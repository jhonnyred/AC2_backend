package com.example.controle_projetos.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.controle_projetos.dtos.DadosSetorDTO;
import com.example.controle_projetos.dtos.SetorDTO;
import com.example.controle_projetos.services.SetorService;

@RestController
@RequestMapping("/api/setor")
public class SetorController {
    private SetorService setorService;

    public SetorController(SetorService setorService) {
        this.setorService = setorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void adicionar(@RequestBody SetorDTO setor) {
        setorService.adicionar(setor);
    }

    @GetMapping("{idSetor}")
    public DadosSetorDTO buscarSetorPorId(@PathVariable Integer idSetor) {
        return setorService.buscarSetorPorId(idSetor);
    }

    @GetMapping
    public List<DadosSetorDTO> listarComFuncionarios() {
        return setorService.listarComFuncionarios();
    }
}
