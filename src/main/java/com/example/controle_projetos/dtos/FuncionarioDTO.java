package com.example.controle_projetos.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioDTO {
    private Integer id;
    private String nome;
    private Integer idSetor;
}
