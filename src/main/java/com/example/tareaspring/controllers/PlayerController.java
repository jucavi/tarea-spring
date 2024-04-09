package com.example.tareaspring.controllers;


import com.example.tareaspring.dto.PlayerDto;
import com.example.tareaspring.dto.PlayerTeamsResponseDto;
import com.example.tareaspring.dto.converter.PlayerMapper;
import com.example.tareaspring.errors.DateFormatException;
import com.example.tareaspring.errors.PlayerNotFoundException;
import com.example.tareaspring.models.Player;
import com.example.tareaspring.models.Team;
import com.example.tareaspring.services.PlayerServiceImp;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/players")
public class PlayerController {

    private final PlayerServiceImp service;
    //private final ModelMapper modelMapper;
    private final PlayerMapper playerMapper;


    /**
     * Get all players from database
     */
    @GetMapping
    public ResponseEntity<List<Player>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }


    /**
     * Find a player from database
     */
    @GetMapping("/{id}")
    public ResponseEntity<Player> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new PlayerNotFoundException(id));
    }


    /**
     * Create a player in the database
     */
    @PostMapping
    public ResponseEntity<Player> create(@RequestBody @Valid PlayerDto playerDto) {
        return ResponseEntity.ok(
                service.create(playerMapper.mapDtoToDao(playerDto))
        );
    }


    /**
     * Update player info
     */
    @PutMapping
    public ResponseEntity<Player> update(@RequestBody @Valid  PlayerDto playerDto) {
        return ResponseEntity.ok(
            service.update(playerMapper.mapDtoToDao(playerDto))
        );
    }


    /**
     * Delete a player from a database
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }


    /**
     * Find all signings of a player from database
     */
    @GetMapping("/{id}/signings")
    public ResponseEntity<List<PlayerTeamsResponseDto>> findSigningsByPlayerId(@PathVariable Long id) {

        return ResponseEntity.ok(service.getUserSignings(id));
    }


    /**
     * Find all the teams where a player has played
     */
    @GetMapping("/{id}/signings/teams/all")
    public ResponseEntity<List<Team>> findTeasByPlayerId(@PathVariable Long id) {

        return ResponseEntity.ok(service.getUserSigningsTeams(id));
    }


   /**
     * Find team where a player has signed at {@code date} passed as parameter
     */
    @GetMapping("/{id}/signings/teams/at")
    public ResponseEntity<List<PlayerTeamsResponseDto>> findSigningsByPlayerIdAt(@PathVariable Long id, @RequestParam @NonNull String date) throws DateFormatException {
        try {

            LocalDate localDate = LocalDate.parse(date);
            return ResponseEntity.ok(service.getUserSigningAtDate(id, localDate));

        } catch (DateTimeParseException ex) {
            throw new DateFormatException("Invalid Date format (yyyy-MM-dd)");
        }
    }


    /**
     * Persist registries from csv file into database
     */
    @PostMapping("/upload")
    public ResponseEntity<List<Player>> upload(@RequestParam("file") MultipartFile file) {
        List<Player> result =  service.parseCSVFileToPlayers(file);

        return ResponseEntity.ok(result);
    }
}
