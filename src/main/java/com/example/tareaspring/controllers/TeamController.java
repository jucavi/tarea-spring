package com.example.tareaspring.controllers;

import com.example.tareaspring.models.Team;
import com.example.tareaspring.services.TeamServiceImp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/teams")
public class TeamController {

    private final TeamServiceImp service;

    // Necesario para la inyección de dependencias
    public TeamController(TeamServiceImp service) {
        this.service = service;
    }

    // CRUD sobre la entidad Team
    /**
     * Get all teams from database
     * @return all teams from the database
     */
    @GetMapping
    public ResponseEntity<List<Team>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    /**
     * Find a team from database
     * @param id team identifier
     * @return team if exists, otherwise {@code 404 } not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Team> findById(@PathVariable Long id) {
        Team result = service.findById(id);

        return ResponseEntity.ok(result);
    }

    /**
     * Create a team in the database
     * @param team team
     * @return team if created, otherwise {@code 404 } not found
     */
    @PostMapping
    public ResponseEntity<Team> create(@RequestBody @Valid Team team) {
        Team result = service.create(team);

        return ResponseEntity.ok(result);
    }

    /**
     * Update team info
     * @param team team
     * @return team if updated, otherwise {@code 404 } not found
     */
    @PutMapping
    public ResponseEntity<Team> update(@RequestBody @Valid Team team) {
        Team result = service.update(team);

        if (result != null) {
            return ResponseEntity.ok(result);
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Delete a team from a database
     * @param id team identifier
     * @return {@code true} if a team was deleted, {@code 404 } not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        Boolean result = service.deleteById(id);

        if (result) {
            return ResponseEntity.ok(true);
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Persist all registries from csv file into database
     * @param file file
     * @return List of teams persisted, otherwise an empty list
     */
    @PostMapping("/upload")
    public ResponseEntity<List<Team>> upload(@RequestParam("file") MultipartFile file) {
        List<Team> result =  service.parseCSVFileToTeams(file);

        return ResponseEntity.ok(result);
    }
}
