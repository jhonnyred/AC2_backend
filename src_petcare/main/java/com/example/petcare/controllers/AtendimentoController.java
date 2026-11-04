package com.example.petcare.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.petcare.dtos.AtendimentoRequestDTO;
import com.example.petcare.dtos.AtendimentoResponseDTO;
import com.example.petcare.services.AtendimentoService;

@RestController
@RequestMapping("/api/atendimento")
public class AtendimentoController {
    private AtendimentoService atendimentoService;

    public AtendimentoController(AtendimentoService atendimentoService) {
        this.atendimentoService = atendimentoService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public AtendimentoResponseDTO registrar(@RequestBody AtendimentoRequestDTO dto) {
        return atendimentoService.registrar(dto);
    }

    @GetMapping()
    public List<AtendimentoResponseDTO> obterTodos() {
        return atendimentoService.obterTodos();
    }

    @GetMapping("{id}")
    public AtendimentoResponseDTO obterPorId(@PathVariable Long id) {
        return atendimentoService.obterPorId(id);
    }

    // Versão em texto do registro, pronta para impressão
    @GetMapping(value = "{id}/impressao", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
    public String imprimir(@PathVariable Long id) {
        return atendimentoService.gerarImpressao(id);
    }
}
