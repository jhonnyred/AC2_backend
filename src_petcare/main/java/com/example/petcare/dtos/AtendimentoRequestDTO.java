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
public class AtendimentoRequestDTO {
    private Long animalId;
    private Long veterinarioId;
    // Opcional: quando informado, a consulta passa para REALIZADA
    private Long consultaId;
    // Opcional: se não informado, usa a data/hora atual
    private LocalDateTime dataAtendimento;
    private String diagnostico;
    private String observacoes;
    private String orientacoes;
}
