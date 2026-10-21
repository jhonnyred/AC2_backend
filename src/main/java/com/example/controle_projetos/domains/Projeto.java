package com.example.controle_projetos.domains;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Projeto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 200)
    private String descricao;

    @Column(nullable = false)
    private LocalDate dataInicio;

    private LocalDate dataFim;

    @ManyToMany
    @JoinTable(name = "projeto_funcionario",
            joinColumns = @JoinColumn(name = "projeto_id"),
            inverseJoinColumns = @JoinColumn(name = "funcionario_id"))
    private List<Funcionario> funcionarios = new ArrayList<>();

    public Projeto(String descricao, LocalDate dataInicio, LocalDate dataFim) {
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    @Override
    public String toString() {
        return "Projeto [id=" + id + ", descricao=" + descricao
                + ", dataInicio=" + dataInicio + ", dataFim=" + dataFim + "]";
    }
}
