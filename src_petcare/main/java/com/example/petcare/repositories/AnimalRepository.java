package com.example.petcare.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.petcare.domains.Animal;
import com.example.petcare.domains.enums.Especie;

public interface AnimalRepository extends JpaRepository<Animal, Long> {

    // Consulta personalizada: animais de um tutor
    @Query("SELECT a FROM Animal a WHERE a.tutor.id = :tutorId ORDER BY a.nome")
    List<Animal> obterPorTutor(@Param("tutorId") Long tutorId);

    List<Animal> findByEspecie(Especie especie);

    boolean existsByTutorId(Long tutorId);
}
