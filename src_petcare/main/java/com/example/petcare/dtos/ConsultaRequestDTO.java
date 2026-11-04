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
public class ConsultaRequestDTO {
    private Long animalId;
    private Long veterinarioId;
    private String local;
    private LocalDateTime dataHora;
}
