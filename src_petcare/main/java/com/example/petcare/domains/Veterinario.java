package com.example.petcare.domains;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.petcare.domains.enums.Especie;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Veterinario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String crmv;

    // Espécies que o veterinário está habilitado a atender
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "veterinario_especie",
            joinColumns = @JoinColumn(name = "veterinario_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "especie")
    @Builder.Default
    private Set<Especie> especies = new HashSet<>();

    @OneToMany(mappedBy = "veterinario")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<Consulta> consultas = new ArrayList<>();

    public boolean atende(Especie especie) {
        return especies != null && especies.contains(especie);
    }
}
