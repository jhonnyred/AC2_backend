package com.example.petcare.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.petcare.domains.Tutor;

public interface TutorRepository extends JpaRepository<Tutor, Long> {

    Optional<Tutor> findByCpf(String cpf);

    boolean existsByCpf(String cpf);

    // Consulta personalizada: busca tutores pelo nome (parcial, sem diferenciar maiúsculas)
    @Query("SELECT t FROM Tutor t WHERE LOWER(t.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Tutor> buscarPorNome(@Param("nome") String nome);
}
