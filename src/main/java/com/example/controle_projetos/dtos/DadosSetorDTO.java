package com.example.controle_projetos.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DadosSetorDTO {
    private Integer id;
    private String nome;
    private List<FuncionarioDTO> funcionarios;
}
