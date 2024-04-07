package com.example.tareaspring;


import com.example.tareaspring.models.*;
import com.example.tareaspring.repositories.PlayerRepository;
import com.example.tareaspring.repositories.SigningRepository;
import com.example.tareaspring.repositories.TeamRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;
import java.util.*;

@SpringBootApplication
public class TareaSpringApplication {

	public static void main(String[] args){
		ApplicationContext context = SpringApplication.run(TareaSpringApplication.class, args);

		LocalDate since = LocalDate.of(2000, 2, 13);

		PlayerRepository playerRep = context.getBean(PlayerRepository.class);
		TeamRepository teamRep = context.getBean(TeamRepository.class);
		SigningRepository signRep = context.getBean(SigningRepository.class);


		// crear un jugador y almacenar
		Player player = new Player(null, "player", "one", "email@email.es", since, FieldPosition.Defensa, Gender.Mujer, 67, 178, 7);
		// month with octal value
		//Player player1 = new Player(1L,"Adelle","Fiester","Adelle.Fiester@player.com", LocalDate.of(1993, 4, 29), FieldPosition.Defensa,Gender.Hombre,64,166,33.6,8);
		System.out.println("Número de jugadores en la base de datos: " + playerRep.findAll().size());
		playerRep.save(player);
		//playerRep.save(player1);
		System.out.println("Número de jugadores en la base de datos: " + playerRep.findAll().size());
		System.out.printf("Imc: %,.6f%n", player.getImc());
		player.setHigh(200);
		player.setWeight(50);
		System.out.printf("Imc: %,.6f%n", player.getImc());

		// Crear un equipo y almacenar
		Team team1 = new Team(null, "team1", "team1email@email.es", since, "Santander", null);
		Team team2 = new Team(null, "team2", "team2email@email.es", since, "Santander", null);
		System.out.println("Número de equipos en la base de datos: " + teamRep.findAll().size());
		teamRep.save(team1);
		teamRep.save(team2);
		System.out.println("Número de equipos en la base de datos: " + teamRep.findAll().size());

		Optional<Player> op1 = playerRep.findById(1L);
		Optional<Team> ot1 = teamRep.findById(1L);
		Optional<Team> ot2 = teamRep.findById(2L);


		Signing signing1 = new Signing(null, op1.get(), ot1.get(),
				LocalDate.of(2000, 1, 23),
				LocalDate.of(2000, 6, 13),
				66);
		Signing signing2 = new Signing(null, op1.get(), ot1.get(),
				LocalDate.of(2010, 1, 23),
				LocalDate.of(2020, 6, 13),
				66);
		Signing signing3 = new Signing(null, op1.get(), ot1.get(),
				LocalDate.of(2000, 5, 1),
				LocalDate.of(2000, 6, 18),
				66);
		System.out.println("Número de joining data en la base de datos: " + signRep.findAll().size());
		signRep.save(signing1);
		signRep.save(signing2);
		signRep.save(signing3);
		System.out.println("Número de joining data en la base de datos: " + signRep.findAll().size());
	}

}
