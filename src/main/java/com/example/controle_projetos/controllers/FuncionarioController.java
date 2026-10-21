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

import com.example.controle_projetos.dtos.DadosProjetoDTO;
import com.example.controle_projetos.dtos.FuncionarioDTO;
import com.example.controle_projetos.services.FuncionarioService;

@RestController
@RequestMapping("/api/funcionario")
public class FuncionarioController {
    private FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void adicionar(@RequestBody FuncionarioDTO funcionario) {
        funcionarioService.adicionar(funcionario);
    }

    @GetMapping("{idFuncionario}/projetos")
    public List<DadosProjetoDTO> buscarProjetos(@PathVariable Integer idFuncionario) {
        return funcionarioService.buscarProjetos(idFuncionario);
    }
}
