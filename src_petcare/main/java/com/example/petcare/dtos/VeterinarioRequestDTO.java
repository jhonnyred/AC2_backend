package com.example.petcare.dtos;

import java.util.Set;

import com.example.petcare.domains.enums.Especie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VeterinarioRequestDTO {
    private Long id;
    private String nome;
    private String crmv;
    private Set<Especie> especies;
}
