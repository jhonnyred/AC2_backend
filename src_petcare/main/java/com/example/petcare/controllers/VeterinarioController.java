package com.example.petcare.controllers;

import java.time.LocalDate;
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

import com.example.petcare.domains.enums.Especie;
import com.example.petcare.dtos.ConsultaResponseDTO;
import com.example.petcare.dtos.VeterinarioRequestDTO;
import com.example.petcare.services.VeterinarioService;

@RestController
@RequestMapping("/api/veterinario")
public class VeterinarioController {
    private VeterinarioService veterinarioService;

    public VeterinarioController(VeterinarioService veterinarioService) {
        this.veterinarioService = veterinarioService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public VeterinarioRequestDTO inserir(@RequestBody VeterinarioRequestDTO dto) {
        return veterinarioService.inserir(dto);
    }

    @PutMapping("{id}")
    public VeterinarioRequestDTO editar(@PathVariable Long id, @RequestBody VeterinarioRequestDTO dto) {
        return veterinarioService.editar(id, dto);
    }

    // GET /api/veterinario ou /api/veterinario?especie=GATO
    @GetMapping()
    public List<VeterinarioRequestDTO> obterTodos(@RequestParam(required = false) Especie especie) {
        return especie == null ? veterinarioService.obterTodos() : veterinarioService.obterHabilitados(especie);
    }

    @GetMapping("{id}")
    public VeterinarioRequestDTO obterPorId(@PathVariable Long id) {
        return veterinarioService.obterPorId(id);
    }

    // GET /api/veterinario/1/agenda?data=2026-11-20
    @GetMapping("{id}/agenda")
    public List<ConsultaResponseDTO> obterAgenda(@PathVariable Long id,
            @RequestParam(required = false) LocalDate data) {
        return veterinarioService.obterAgenda(id, data);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        veterinarioService.excluir(id);
    }
}
