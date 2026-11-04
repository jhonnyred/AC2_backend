package com.example.petcare.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoAnimalDTO {
    private AnimalRequestDTO animal;
    private TutorRequestDTO tutor;
    private List<AtendimentoResponseDTO> atendimentos;
    private List<VacinaRequestDTO> vacinas;
}
