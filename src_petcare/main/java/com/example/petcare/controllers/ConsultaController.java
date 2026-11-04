package com.example.petcare.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.petcare.domains.enums.StatusConsulta;
import com.example.petcare.dtos.ConsultaRequestDTO;
import com.example.petcare.dtos.ConsultaResponseDTO;
import com.example.petcare.services.ConsultaService;

@RestController
@RequestMapping("/api/consulta")
public class ConsultaController {
    private ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ConsultaResponseDTO agendar(@RequestBody ConsultaRequestDTO dto) {
        return consultaService.agendar(dto);
    }

    // GET /api/consulta ou /api/consulta?status=AGENDADA
    @GetMapping()
    public List<ConsultaResponseDTO> obterTodos(@RequestParam(required = false) StatusConsulta status) {
        return consultaService.obterTodos(status);
    }

    @GetMapping("{id}")
    public ConsultaResponseDTO obterPorId(@PathVariable Long id) {
        return consultaService.obterPorId(id);
    }

    @PatchMapping("{id}/cancelar")
    public ConsultaResponseDTO cancelar(@PathVariable Long id) {
        return consultaService.cancelar(id);
    }
}
