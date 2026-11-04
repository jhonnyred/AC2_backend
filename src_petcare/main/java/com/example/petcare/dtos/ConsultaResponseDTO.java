package com.example.petcare.dtos;

import java.time.LocalDateTime;

import com.example.petcare.domains.enums.StatusConsulta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaResponseDTO {
    private Long id;
    private Long animalId;
    private String animalNome;
    private Long veterinarioId;
    private String veterinarioNome;
    private String local;
    private LocalDateTime dataHora;
    private StatusConsulta status;
}
