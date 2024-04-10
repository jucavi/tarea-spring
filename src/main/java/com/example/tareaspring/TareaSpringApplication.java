package com.example.tareaspring;


import com.example.tareaspring.services.PlayerService;
import com.example.tareaspring.services.SigningService;
import com.example.tareaspring.services.TeamService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;

@SpringBootApplication
public class TareaSpringApplication {

	public static void main(String[] args){
		SpringApplication.run(TareaSpringApplication.class, args);
	}

}
