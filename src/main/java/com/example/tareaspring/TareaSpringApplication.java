package com.example.tareaspring;


import com.example.tareaspring.models.*;
import com.example.tareaspring.services.PlayerService;
import com.example.tareaspring.services.SigningService;
import com.example.tareaspring.services.TeamService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;
import java.util.*;

@SpringBootApplication
public class TareaSpringApplication {

	public static void main(String[] args){


		ApplicationContext context = SpringApplication.run(TareaSpringApplication.class, args);

		// TODO CREATE TEST CASES
		LocalDate since = LocalDate.of(2000, 2, 13);

		PlayerService playerS = context.getBean(PlayerService.class);
		TeamService teamS = context.getBean(TeamService.class);
		SigningService signS = context.getBean(SigningService.class);


		// crear un jugador y almacenar
		Player player1 = new Player(
				null,
				"player1",
				"one",
				"email1@email.es",
				LocalDate.now(),
				FieldPosition.Defensa,
				Gender.Mujer,
				67,
				178,
				7);
		Player player2 = new Player(
				null,
				"Adelle",
				"Fiester",
				"Adelle.Fiester@player.com",
				LocalDate.of(1993, 4, 29),
				FieldPosition.Defensa,
				Gender.Hombre,
				64,
				166,
				33.6,
				8);

		playerS.create(player1);
		playerS.create(player2);


		System.out.println("Number of players in database: " + playerS.findAll().size());



		Team team1 = new Team(
				null,
				"team1",
				"team1email@email.es",
				since,
				"Santander",
				null);

		Team team2 = new Team(
				null,
				"team2",
				"team2email@email.es",
				since,
				"Madrid",
				null);

		teamS.create(team1);
		teamS.create(team2);
		System.out.println("Number of teams in database: " + teamS.findAll().size());

		Optional<Player> p1 = playerS.findById(1L);
		Optional<Player> p2 = playerS.findById(2L);

		Optional<Team> t1 = teamS.findById(1L);
		Optional<Team> t2 = teamS.findById(2L);


//		Signing signing1 = new Signing(
//				null,
//				p1.get(),
//				t1.get(),
//				LocalDate.of(2001, 1, 1),
//				LocalDate.of(2002, 1, 1),
//				6);
//
//		Signing signing2 = new Signing(
//				null,
//				p1.get(),
//				t1.get(),
//				LocalDate.of(2002, 4, 1),
//				LocalDate.of(2002, 12, 1),
//				8);
//
//		// player with signing on same team invalid
//		Signing signing3 = new Signing(
//				null,
//				p2.get(),
//				t1.get(),
//				LocalDate.of(2001, 5, 1),
//				LocalDate.of(2002, 5, 1),
//				55);
//
//		// player with valid range but no number invalid
//		Signing signing4 = new Signing(
//				null,
//				p2.get(),
//				t1.get(),
//				LocalDate.of(2000, 1, 1),
//				LocalDate.of(2000, 6, 18),
//				66);
//
//		// valid number
//		Signing signing5 = new Signing(
//				null,
//				p2.get(),
//				t1.get(),
//				LocalDate.of(2000, 1, 1),
//				LocalDate.of(2000, 6, 18),
//				55);
//
//
//
//		signS.create(signing1);
//		signS.create(signing2);
//		signS.create(signing3);
//		signS.create(signing4);
//		signS.create(signing5);


		System.out.println("Signings on database: " + signS.findAll().size());
	}

}
