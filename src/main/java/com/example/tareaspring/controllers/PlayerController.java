package com.example.tareaspring.controllers;


import com.example.tareaspring.dto.PlayerDto;
import com.example.tareaspring.dto.PlayerTeamsResponseDto;
import com.example.tareaspring.dto.TeamDto;
import com.example.tareaspring.dto.converter.PlayerMapper;
import com.example.tareaspring.errors.DateFormatException;
import com.example.tareaspring.services.PlayerServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
@Tag(name = "Players")
public class PlayerController {

    private final PlayerServiceImp service;
    private final PlayerMapper playerMapper;


    /**
     * Get all players from database
     */
    @GetMapping
    @Operation(summary = "All players", description = "Retrieve all teams from database")
    @ApiResponse(description = "Whatever it does", responseCode = "200")
    public ResponseEntity<List<PlayerDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }


    /**
     * Find a player from database
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlayerDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }


    /**
     * Create a player in the database
     */
    @PostMapping
    public ResponseEntity<PlayerDto> create(@RequestBody @Valid PlayerDto playerDto) {
        return ResponseEntity.ok(
                    service.create(
                            playerMapper.mapDtoToDao(playerDto)));
    }


    /**
     * Update player info
     */
    @PutMapping
    public ResponseEntity<PlayerDto> update(@RequestBody @Valid  PlayerDto playerDto) {
        return ResponseEntity.ok(
                        service.update(
                                playerMapper.mapDtoToDao(playerDto)));
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
    public ResponseEntity<List<PlayerTeamsResponseDto>> findByPlayerIdAllSignings(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByPlayerIdAllSignings(id));
    }


    /**
     * Find all the teams where a player has played
     */
    @GetMapping("/{id}/signings/teams")
    public ResponseEntity<List<TeamDto>> findByPlayerIdAllTeamsWerePlayerHasPlayed(@PathVariable Long id) {
        return ResponseEntity.ok(
                service.findByPlayerIdTeamsWerePlayerHasPlayed(id));
    }


   /**
     * Find team where a player has signed at {@code date} passed as parameter
     */
    @GetMapping("/{id}/signings/teams/at")
    public ResponseEntity<List<PlayerTeamsResponseDto>> findByPlayerIdSigningAtDate(@PathVariable Long id, @RequestParam @NonNull String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            return ResponseEntity.ok(service.findByPlayerIdSigningAtDate(id, localDate));

        } catch (DateTimeParseException ex) {
            throw new DateFormatException("Invalid Date format (yyyy-MM-dd)");
        }
    }


    /**
     * Persist registries from csv file into database
     */
    @PostMapping("/upload")
    public ResponseEntity<List<PlayerDto>> upload(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(service.parseCSVFileToPlayers(file));
    }
}
