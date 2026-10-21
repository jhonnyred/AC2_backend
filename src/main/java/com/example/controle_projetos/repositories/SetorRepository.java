package com.example.controle_projetos.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.controle_projetos.domains.Setor;

public interface SetorRepository extends JpaRepository<Setor, Integer> {

    @Query("SELECT DISTINCT s FROM Setor s LEFT JOIN FETCH s.funcionarios ORDER BY s.nome")
    List<Setor> listarSetoresComFuncionarios();

    @Query("SELECT s FROM Setor s LEFT JOIN FETCH s.funcionarios WHERE s.id = :id")
    Optional<Setor> buscarSetorComFuncionarios(@Param("id") Integer id);

    boolean existsByNomeIgnoreCase(String nome);
}
