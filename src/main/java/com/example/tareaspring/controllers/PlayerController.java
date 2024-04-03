package com.example.tareaspring.controllers;

import com.example.tareaspring.models.Player;
import com.example.tareaspring.repositories.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class PlayerController {

    private final PlayerRepository repository;

    // Definimos el logger
    private final Logger log = LoggerFactory.getLogger(PlayerController.class);

    // Necesario para la inyección de dependencias
    public PlayerController(PlayerRepository repository) {
        this.repository = repository;
    }

    // CRUD sobre la entidad Player
    @GetMapping("/players")
    public List<Player> findAll() {
        // recuperar y devolver los jugadores de la base de datos
        log.info("Retrieving all Teams from database");
        return repository.findAll();
    }

    // Buscar un jugador en la base de datos según su Id
    @GetMapping("/players/{id}")
    public ResponseEntity<Player> findById(@PathVariable Long id) {

        Optional<Player> playerOpt = repository.findById(id);

        if (playerOpt.isPresent()) {
            log.info("Retrieving Player with ID: {}", id);
            return ResponseEntity.ok(playerOpt.get());
        }
        log.warn("Retrieving Player with wrong ID: {}", id);
        return ResponseEntity.notFound().build();
    }

    // Crear un nuevo jugador en la baae de datos
    @PostMapping("/players")
    public ResponseEntity<Player> create(@RequestBody Player player) {

        if (player.getId() != null) {
            log.warn("Trying to create a Player with assigned ID: {}", player.getId());
            return ResponseEntity.badRequest().build();
        }

        // guardar el jugador recibido por parámetro en la base de datos
        Player result = repository.save(player);
        log.info("Player created with ID: {}", player.getId());

        return ResponseEntity.ok(result);
    }

    // Actualizar un jugador en la base de datos
    @PutMapping("/players")
    public ResponseEntity<Player> update(@RequestBody Player player) {

        if (player.getId() != null && !repository.existsById(player.getId())) {
            log.warn("Trying to update a jugador with wrong ID");
            return ResponseEntity.notFound().build();
        }

        Player result = repository.save(player);
        log.info("Player updated with ID: {}", player.getId());

        return ResponseEntity.ok(result);
    }

    // Borrar un jugador de la base de datos
    @DeleteMapping("/players/{id}")
    public ResponseEntity delete(@PathVariable Long id) {

        if (repository.existsById(id)) {
            repository.deleteById(id);
            log.info("Player deleted with ID: {}", id);
            return ResponseEntity.noContent().build();
        }

        log.warn("Trying to delete a Player with wrong ID");
        return ResponseEntity.notFound().build();
    }

    // Borrar todos los jugadores de la base de datos
    @DeleteMapping("/players")
    public ResponseEntity deleteAll() {
        log.info("Deleting all Players...");
        repository.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
