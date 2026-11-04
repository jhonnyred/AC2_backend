package com.example.petcare.domains;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.petcare.domains.enums.Especie;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Especie especie;

    private String raca;

    private LocalDate dataNascimento;

    // Vários animais pertencem a um tutor (N:1)
    @ManyToOne(optional = false)
    @JoinColumn(name = "tutor_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Tutor tutor;

    @OneToMany(mappedBy = "animal")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<Consulta> consultas = new ArrayList<>();

    @OneToMany(mappedBy = "animal")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<Atendimento> atendimentos = new ArrayList<>();

    @OneToMany(mappedBy = "animal")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<Vacina> vacinas = new ArrayList<>();
}
