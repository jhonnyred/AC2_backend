package com.example.petcare.dtos;

import java.time.LocalDate;

import com.example.petcare.domains.enums.Especie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalRequestDTO {
    private Long id;
    private String nome;
    private Especie especie;
    private String raca;
    private LocalDate dataNascimento;
    private Long tutorId;
    private String tutorNome;
}
