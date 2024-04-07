package com.example.tareaspring.controllers;


import com.example.tareaspring.models.Player;
import com.example.tareaspring.models.Signing;
import com.example.tareaspring.services.PlayerServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/players")
public class PlayerController {

    private final PlayerServiceImp service;

    /**
     * Get all players from database
     * @return all player from the database
     */
    @GetMapping
    public ResponseEntity<List<Player>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }


    /**
     * Find a player from database
     * @param id player identifier
     * @return player if exists, otherwise {@code 404 } not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Player> findById(@PathVariable Long id) {
        Optional<Player> result = service.findById(id);

        return result.map(ResponseEntity::ok).orElseThrow(RuntimeException::new);
    }

    /**
     * Find signing of a player  from database
     * @param id player identifier
     * @return list of signings, otherwise {@code 404 } not found
     */
    @GetMapping("/{id}/signings")
    public ResponseEntity<Set<Signing>> findSigningsByPlayerId(@PathVariable Long id) {

//        Player result = service.findById(id);
//
//        if (result != null) {
//            return ResponseEntity.ok(result);
//        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Create a player in the database
     * @param player player
     * @return player if created, otherwise {@code 404 } not found
     */
    @PostMapping
    public ResponseEntity<Player> create(@RequestBody @Valid Player player) {
        Player result = service.create(player);

        if (result != null) {
            return ResponseEntity.ok(result);
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Update player info
     * @param player player
     * @return player if updated, otherwise {@code 404 } not found
     */
    @PutMapping
    public ResponseEntity<Player> update(@RequestBody @Valid  Player player) {
        Player result = service.update(player);

        if (result != null) {
            return ResponseEntity.ok(result);
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Delete a player from a database
     * @param id player identifier
     * @return {@code true} if a player was deleted, {@code 404 } not found
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
     * @return List of players persisted, otherwise an empty list
     */
    @PostMapping("/upload")
    public ResponseEntity<List<Player>> upload(@RequestParam("file") MultipartFile file) {
        List<Player> result =  service.parseCSVFileToPlayers(file);

        return ResponseEntity.ok(result);
    }
}
