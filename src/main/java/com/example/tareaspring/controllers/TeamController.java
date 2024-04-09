package com.example.tareaspring.controllers;

import com.example.tareaspring.dto.PlayerDto;
import com.example.tareaspring.dto.TeamDto;
import com.example.tareaspring.dto.TeamPlayerResponseDto;
import com.example.tareaspring.errors.DateFormatException;
import com.example.tareaspring.errors.TeamNotFoundException;
import com.example.tareaspring.services.TeamServiceImp;
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
@RequestMapping("/teams")
public class TeamController {

    private final TeamServiceImp service;


    /**
     * Get all teams from database
     */
    @GetMapping
    public ResponseEntity<List<TeamDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }


    /**
     * Find a team from database
     */
    @GetMapping("/{id}")
    public ResponseEntity<TeamDto> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new TeamNotFoundException(id));
    }


    /**
     * Create a team in the database
     */
    @PostMapping
    public ResponseEntity<TeamDto> create(@RequestBody @Valid TeamDto teamDto) {
        return ResponseEntity.ok(
                service.create(teamDto)
        );
    }


    /**
     * Update team info
     */
    @PutMapping
    public ResponseEntity<TeamDto> update(@RequestBody @Valid TeamDto teamDto) {
        return ResponseEntity.ok(
                service.update(teamDto)
        );
    }


    /**
     * Delete a team from a database
     * @param id team identifier
     * @return {@code true} if a team was deleted, {@code 404 } not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }


    /**
     * Find signings of a team from database
     */
    @GetMapping("/{id}/signings")
    public ResponseEntity<List<TeamPlayerResponseDto>> findSigningsByPlayerId(@PathVariable Long id) {

        return ResponseEntity.ok(service.getTeamSignings(id));
    }


    /**
     * All players who have been signed by a team
     */
    @GetMapping("/{id}/signings/players/all")
    public ResponseEntity<List<PlayerDto>> findTeasByPlayerId(@PathVariable Long id) {

        return ResponseEntity.ok(service.getTeamSigningsPlayers(id));
    }

    /**
     * Find players where has signed for a team at {@code date} passed as parameter
     */
    @GetMapping("/{id}/signings/players/at")
    public ResponseEntity<List<TeamPlayerResponseDto>> findSigningsByTeamIdAt(
            @PathVariable Long id,
            @RequestParam @NonNull String date) throws DateFormatException {
        try {

            LocalDate localDate = LocalDate.parse(date);
            return ResponseEntity.ok(service.getSigningsAtDate(id, localDate));

        } catch (DateTimeParseException ex) {
            throw new DateFormatException("Invalid Date format (yyyy-MM-dd)");
        }
    }


    /**
     * Persist all registries from csv file into database
     * @param file file
     * @return List of teams persisted, otherwise an empty list
     */
    @PostMapping("/upload")
    public ResponseEntity<List<TeamDto>> upload(@RequestParam("file") MultipartFile file) {
        List<TeamDto> result =  service.parseCSVFileToTeams(file);

        return ResponseEntity.ok(result);
    }
}
