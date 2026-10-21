package com.example.controle_projetos.dtos;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DadosProjetoDTO {
    private Integer id;
    private String descricao;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private List<FuncionarioDTO> funcionarios;
}
