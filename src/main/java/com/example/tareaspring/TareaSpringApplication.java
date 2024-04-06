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
		LocalDate until = LocalDate.of(2025, 6, 13);

		PlayerRepository playerRep = context.getBean(PlayerRepository.class);
		TeamRepository teamRep = context.getBean(TeamRepository.class);
		SigningRepository signRep = context.getBean(SigningRepository.class);


		// crear un jugador y almacenar
		Player player = new Player(null, "player", "one", "email@email.es", since, FieldPosition.Defensa, Gender.Mujer, 67, 178, 7);
		System.out.println("Número de jugadores en la base de datos: " + playerRep.findAll().size());
		playerRep.save(player);
		System.out.println("Número de jugadores en la base de datos: " + playerRep.findAll().size());
		System.out.printf("Imc: %,.6f%n", player.getImc());
		player.setHigh(200);
		player.setWeight(50);
		System.out.printf("Imc: %,.6f%n", player.getImc());

		// Crear un equipo y almacenar
		Team team = new Team(null, "team", "teamemail@email.es", since, "Santander", null);
		System.out.println("Número de equipos en la base de datos: " + teamRep.findAll().size());
		teamRep.save(team);
		System.out.println("Número de equipos en la base de datos: " + teamRep.findAll().size());

		Optional<Player> op = playerRep.findById(1L);
		Optional<Team> ot = teamRep.findById(1L);

		Player p = op.orElseThrow();
		Team t = ot.orElseThrow();

		Signing signing = new Signing(null, p, t, since, until, 66);
		System.out.println("Número de joining data en la base de datos: " + signRep.findAll().size());
		signRep.save(signing);
		System.out.println("Número de joining data en la base de datos: " + signRep.findAll().size());
	}

}
