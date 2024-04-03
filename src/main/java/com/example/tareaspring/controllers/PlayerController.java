package com.example.tareaspring.controllers;

import com.example.tareaspring.models.Player;
import com.example.tareaspring.repositories.PlayerRepository;
import com.example.tareaspring.services.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class PlayerController {

    private final PlayerService service;

    // Definimos el logger
    private final Logger log = LoggerFactory.getLogger(PlayerController.class);

    // Necesario para la inyección de dependencias
    public PlayerController(PlayerService service) {
        this.service = service;
    }

    // CRUD sobre la entidad Player
    @GetMapping("/players")
    public List<Player> findAll() {
        // recuperar y devolver los jugadores de la base de datos
        return service.findAll();
    }

    // Buscar un jugador en la base de datos según su Id
    @GetMapping("/players/{id}")
    public ResponseEntity<Player> findById(@PathVariable Long id) {
        return service.findById(id);
    }

    // Crear un nuevo jugador en la baae de datos
    @PostMapping("/players")
    public ResponseEntity<Player> create(@RequestBody Player player) {
        return service.create(player);
    }

    // Actualizar un jugador en la base de datos
    @PutMapping("/players")
    public ResponseEntity<Player> update(@RequestBody Player player) {

        return service.update(player);
    }

    // Borrar un jugador de la base de datos
    @DeleteMapping("/players/{id}")
    public ResponseEntity delete(@PathVariable Long id) {

        return service.delete(id);
    }

    // Borrar todos los jugadores de la base de datos
    @DeleteMapping("/players")
    public ResponseEntity deleteAll() {
        return service.delete();
    }

    @PostMapping("/players/upload")
    public ResponseEntity upload(@RequestParam("file") MultipartFile file) {
        return service.parseCSVFileToPlayers(file);
    }
}
