package com.example.petcare.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VacinaRequestDTO {
    private Long id;
    private String nome;
    private LocalDate dataAplicacao;
    private String lote;
    private Long animalId;
    // Opcional
    private Long veterinarioId;
}
