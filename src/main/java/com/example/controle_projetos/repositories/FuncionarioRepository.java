package com.example.controle_projetos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.controle_projetos.domains.Funcionario;
import com.example.controle_projetos.domains.Projeto;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer> {

    @Query("SELECT p FROM Funcionario f JOIN f.projetos p WHERE f.id = :idFuncionario ORDER BY p.dataInicio")
    List<Projeto> buscarProjetosDoFuncionario(@Param("idFuncionario") Integer idFuncionario);
}
