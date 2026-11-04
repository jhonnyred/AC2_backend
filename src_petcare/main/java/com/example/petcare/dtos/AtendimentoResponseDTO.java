package com.example.petcare.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtendimentoResponseDTO {
    private Long id;
    private Long animalId;
    private String animalNome;
    private Long veterinarioId;
    private String veterinarioNome;
    private String veterinarioCrmv;
    private Long consultaId;
    private LocalDateTime dataAtendimento;
    private String diagnostico;
    private String observacoes;
    private String orientacoes;
}
