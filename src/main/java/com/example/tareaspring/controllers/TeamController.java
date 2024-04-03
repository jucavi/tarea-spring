package com.example.tareaspring.controllers;

import com.example.tareaspring.models.Team;
import com.example.tareaspring.repositories.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1")
public class TeamController {

    private final TeamRepository repository;

    // Definimos el logger
    private final Logger log = LoggerFactory.getLogger(TeamController.class);

    // Necesario para la inyección de dependencias
    public TeamController(TeamRepository repository) {
        this.repository = repository;
    }

    // CRUD sobre la entidad Team
    @GetMapping("/teams")
    public List<Team> findAll() {
        // recuperar y devolver los equipos de la base de datos
        log.info("Retrieving all Teams from database");
        return repository.findAll();
    }

    // Buscar un equipo en la base de datos según su Id
    @GetMapping("/teams/{id}")
    public ResponseEntity<Team> findById(@PathVariable Long id) {

        Optional<Team> teamOpt = repository.findById(id);

        if (teamOpt.isPresent()) {
            log.info("Retrieving Team with ID: {}", id);
            return ResponseEntity.ok(teamOpt.get());
        }

        log.warn("Retrieving Team with wrong ID: {}", id);
        return ResponseEntity.notFound().build();
    }

    // Crear un nuevo equipo en la baae de datos
    @PostMapping("/teams")
    public ResponseEntity<Team> create(@RequestBody Team team) {

        if (team.getId() != null) {
            log.warn("Trying to create a Team with duplicate ID: {}", team.getId());
            return ResponseEntity.badRequest().build();
        }

        // guardar el equipo recibido por parámetro en la base de datos
        Team result = repository.save(team);
        log.info("Team created with ID: {}", team.getId());

        return ResponseEntity.ok(result);
    }

    // Actualizar un equipo en la base de datos
    @PutMapping("/teams")
    public ResponseEntity<Team> update(@RequestBody Team team) {

        if (team.getId() != null && !repository.existsById(team.getId())) {
            log.warn("Trying to update a team with wrong ID");
            return ResponseEntity.notFound().build();
        }

        Team result = repository.save(team);
        log.info("Team updated with ID: {}", team.getId());

        return ResponseEntity.ok(result);
    }

    // Borrar un equipo de la base de datos
    @DeleteMapping("/teams/{id}")
    public ResponseEntity delete(@PathVariable Long id) {

        if (repository.existsById(id)) {
            repository.deleteById(id);
            log.info("Team deleted with ID: {}", id);
            return ResponseEntity.noContent().build();
        }

        log.warn("Trying to delete a Team with wrong ID");
        return ResponseEntity.notFound().build();
    }

    // Borrar todos los equipos de la base de datos
    @DeleteMapping("/teams")
    public ResponseEntity deleteAll() {
        log.info("Deleting all Teams...");
        repository.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
