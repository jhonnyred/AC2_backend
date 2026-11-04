package com.example.petcare;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.petcare.domains.Animal;
import com.example.petcare.domains.Atendimento;
import com.example.petcare.domains.Consulta;
import com.example.petcare.domains.Tutor;
import com.example.petcare.domains.Vacina;
import com.example.petcare.domains.Veterinario;
import com.example.petcare.domains.enums.Especie;
import com.example.petcare.domains.enums.StatusConsulta;
import com.example.petcare.repositories.AnimalRepository;
import com.example.petcare.repositories.AtendimentoRepository;
import com.example.petcare.repositories.ConsultaRepository;
import com.example.petcare.repositories.TutorRepository;
import com.example.petcare.repositories.VacinaRepository;
import com.example.petcare.repositories.VeterinarioRepository;

@SpringBootApplication
public class ProjetoPetcareApplication {

	// Dados iniciais para facilitar os testes no Thunder Client
	@Bean
	public CommandLineRunner init(
			@Autowired TutorRepository tutorRepository,
			@Autowired AnimalRepository animalRepository,
			@Autowired VeterinarioRepository veterinarioRepository,
			@Autowired ConsultaRepository consultaRepository,
			@Autowired AtendimentoRepository atendimentoRepository,
			@Autowired VacinaRepository vacinaRepository) {
		return args -> {
			System.out.println("=== Criando tutores ===");
			Tutor maria = tutorRepository.save(Tutor.builder()
					.nome("Maria Souza").cpf("52998224725")
					.telefone("(15) 99999-1111").endereco("Rua das Flores, 100 - Sorocaba/SP").build());
			Tutor joao = tutorRepository.save(Tutor.builder()
					.nome("João Lima").cpf("11144477735")
					.telefone("(15) 98888-2222").endereco("Av. Brasil, 500 - Sorocaba/SP").build());

			System.out.println("=== Criando animais ===");
			Animal rex = animalRepository.save(Animal.builder()
					.nome("Rex").especie(Especie.CACHORRO).raca("Labrador")
					.dataNascimento(LocalDate.of(2020, 5, 10)).tutor(maria).build());
			Animal mia = animalRepository.save(Animal.builder()
					.nome("Mia").especie(Especie.GATO).raca("Siamês")
					.dataNascimento(LocalDate.of(2022, 1, 15)).tutor(maria).build());
			animalRepository.save(Animal.builder()
					.nome("Loro").especie(Especie.AVE).raca("Papagaio")
					.dataNascimento(LocalDate.of(2019, 8, 1)).tutor(joao).build());

			System.out.println("=== Criando veterinários ===");
			Veterinario ana = veterinarioRepository.save(Veterinario.builder()
					.nome("Dra. Ana Pereira").crmv("SP-12345")
					.especies(new java.util.HashSet<>(Set.of(Especie.CACHORRO, Especie.GATO))).build());
			veterinarioRepository.save(Veterinario.builder()
					.nome("Dr. Carlos Mendes").crmv("SP-67890")
					.especies(new java.util.HashSet<>(Set.of(Especie.AVE, Especie.REPTIL))).build());

			System.out.println("=== Criando consulta, atendimento e vacinas ===");
			consultaRepository.save(Consulta.builder()
					.animal(rex).veterinario(ana).local("Clínica Centro - Sala 1")
					.dataHora(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(10, 0)))
					.status(StatusConsulta.AGENDADA).build());

			atendimentoRepository.save(Atendimento.builder()
					.animal(mia).veterinario(ana)
					.dataAtendimento(LocalDateTime.now().minusDays(10))
					.diagnostico("Otite leve no ouvido esquerdo")
					.observacoes("Animal dócil, sem febre")
					.orientacoes("Aplicar gotas otológicas 2x ao dia por 7 dias").build());

			vacinaRepository.save(Vacina.builder()
					.nome("V10").lote("LT-2025-001").dataAplicacao(LocalDate.now().minusMonths(6))
					.animal(rex).veterinario(ana).build());
			vacinaRepository.save(Vacina.builder()
					.nome("Antirrábica").lote("AR-7788").dataAplicacao(LocalDate.now().minusMonths(2))
					.animal(rex).veterinario(ana).build());

			System.out.println("=== Dados iniciais carregados ===");
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(ProjetoPetcareApplication.class, args);
	}

}
