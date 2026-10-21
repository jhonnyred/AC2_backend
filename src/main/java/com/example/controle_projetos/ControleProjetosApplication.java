package com.example.controle_projetos;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.controle_projetos.domains.Funcionario;
import com.example.controle_projetos.domains.Projeto;
import com.example.controle_projetos.domains.Setor;
import com.example.controle_projetos.repositories.FuncionarioRepository;
import com.example.controle_projetos.repositories.ProjetoRepository;
import com.example.controle_projetos.repositories.SetorRepository;

@SpringBootApplication
public class ControleProjetosApplication {

	@Bean
	public CommandLineRunner init(SetorRepository setorRepository,
			FuncionarioRepository funcionarioRepository,
			ProjetoRepository projetoRepository) {
		return args -> {
			Setor ti = setorRepository.save(new Setor("Tecnologia"));
			Setor rh = setorRepository.save(new Setor("Recursos Humanos"));

			Funcionario ana = new Funcionario("Ana Souza");
			ana.setSetor(ti);
			ana = funcionarioRepository.save(ana);

			Funcionario bruno = new Funcionario("Bruno Lima");
			bruno.setSetor(ti);
			bruno = funcionarioRepository.save(bruno);

			Funcionario carla = new Funcionario("Carla Mendes");
			carla.setSetor(rh);
			carla = funcionarioRepository.save(carla);

			Projeto p1 = new Projeto("Portal do Colaborador",
					LocalDate.of(2026, 3, 1), LocalDate.of(2026, 9, 30));
			p1.getFuncionarios().add(ana);
			p1.getFuncionarios().add(carla);
			projetoRepository.save(p1);

			Projeto p2 = new Projeto("Migração para Nuvem",
					LocalDate.of(2026, 8, 15), null);
			p2.getFuncionarios().add(ana);
			p2.getFuncionarios().add(bruno);
			projetoRepository.save(p2);

			setorRepository.listarSetoresComFuncionarios().forEach(System.out::println);
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(ControleProjetosApplication.class, args);
	}

}
