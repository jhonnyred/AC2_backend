package com.example.petcare.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.petcare.domains.Vacina;

public interface VacinaRepository extends JpaRepository<Vacina, Long> {

    // Consulta personalizada: carteira de vacinação do animal
    @Query("SELECT v FROM Vacina v WHERE v.animal.id = :animalId ORDER BY v.dataAplicacao DESC")
    List<Vacina> obterCarteiraVacinacao(@Param("animalId") Long animalId);

    boolean existsByAnimalId(Long animalId);
}
