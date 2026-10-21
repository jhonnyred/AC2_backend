package com.example.controle_projetos.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.controle_projetos.domains.Projeto;

public interface ProjetoRepository extends JpaRepository<Projeto, Integer> {

    @Query("SELECT p FROM Projeto p LEFT JOIN FETCH p.funcionarios WHERE p.id = :id")
    Optional<Projeto> buscarProjetoComFuncionarios(@Param("id") Integer id);

    @Query("SELECT p FROM Projeto p WHERE p.dataInicio BETWEEN :inicio AND :fim ORDER BY p.dataInicio")
    List<Projeto> buscarPorPeriodoDeInicio(@Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim);
}
