package com.example.petcare.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.petcare.domains.Veterinario;
import com.example.petcare.domains.enums.Especie;

public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {

    boolean existsByCrmv(String crmv);

    // Consulta personalizada: veterinários habilitados para uma espécie
    @Query("SELECT DISTINCT v FROM Veterinario v JOIN v.especies e WHERE e = :especie ORDER BY v.nome")
    List<Veterinario> obterHabilitadosPorEspecie(@Param("especie") Especie especie);
}
