package com.example.petcare.domains;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

// Registro do prontuário do animal
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Atendimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "animal_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Animal animal;

    @ManyToOne(optional = false)
    @JoinColumn(name = "veterinario_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Veterinario veterinario;

    // Consulta que originou o atendimento (opcional)
    @OneToOne
    @JoinColumn(name = "consulta_id", unique = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Consulta consulta;

    @Column(nullable = false)
    private LocalDateTime dataAtendimento;

    @Column(nullable = false, length = 1000)
    private String diagnostico;

    @Column(length = 2000)
    private String observacoes;

    @Column(length = 2000)
    private String orientacoes;
}
