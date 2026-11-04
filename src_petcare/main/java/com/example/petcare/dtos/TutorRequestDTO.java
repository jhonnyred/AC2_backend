package com.example.petcare.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TutorRequestDTO {
    private Long id;
    private String nome;
    private String cpf;
    private String telefone;
    private String endereco;
}
