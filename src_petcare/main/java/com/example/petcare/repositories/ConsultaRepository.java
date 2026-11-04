package com.example.petcare.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.petcare.domains.Consulta;
import com.example.petcare.domains.enums.StatusConsulta;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    // Consulta personalizada: verifica se o veterinário já possui consulta ativa
    // que se sobrepõe ao horário (janela de duração da consulta)
    @Query("""
            SELECT COUNT(c) > 0 FROM Consulta c
            WHERE c.veterinario.id = :veterinarioId
              AND c.status = com.example.petcare.domains.enums.StatusConsulta.AGENDADA
              AND c.dataHora > :inicio
              AND c.dataHora < :fim
            """)
    boolean existeConflitoVeterinario(@Param("veterinarioId") Long veterinarioId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);

    // Consulta personalizada: impede o mesmo animal em duas consultas ao mesmo tempo
    @Query("""
            SELECT COUNT(c) > 0 FROM Consulta c
            WHERE c.animal.id = :animalId
              AND c.status = com.example.petcare.domains.enums.StatusConsulta.AGENDADA
              AND c.dataHora > :inicio
              AND c.dataHora < :fim
            """)
    boolean existeConflitoAnimal(@Param("animalId") Long animalId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);

    // Consulta personalizada: agenda do veterinário em um período
    @Query("""
            SELECT c FROM Consulta c
            WHERE c.veterinario.id = :veterinarioId
              AND c.dataHora BETWEEN :inicio AND :fim
            ORDER BY c.dataHora
            """)
    List<Consulta> obterAgendaVeterinario(@Param("veterinarioId") Long veterinarioId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);

    List<Consulta> findByAnimalIdOrderByDataHoraDesc(Long animalId);

    List<Consulta> findByStatusOrderByDataHora(StatusConsulta status);

    boolean existsByAnimalId(Long animalId);

    boolean existsByVeterinarioId(Long veterinarioId);
}
