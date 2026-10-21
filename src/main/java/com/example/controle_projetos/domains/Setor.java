package com.example.controle_projetos.domains;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Setor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nome;

    @OneToMany(mappedBy = "setor")
    private List<Funcionario> funcionarios = new ArrayList<>();

    public Setor(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Setor [id=" + id + ", nome=" + nome + "]";
    }
}
