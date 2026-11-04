package com.example.petcare.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.petcare.domains.Atendimento;

public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {

    // Consulta personalizada: histórico de atendimentos do animal (mais recentes primeiro)
    @Query("""
            SELECT a FROM Atendimento a
            JOIN FETCH a.veterinario
            WHERE a.animal.id = :animalId
            ORDER BY a.dataAtendimento DESC
            """)
    List<Atendimento> obterHistoricoPorAnimal(@Param("animalId") Long animalId);

    boolean existsByConsultaId(Long consultaId);

    boolean existsByAnimalId(Long animalId);

    boolean existsByVeterinarioId(Long veterinarioId);
}
