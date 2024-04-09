package com.example.tareaspring.controllers;

import com.example.tareaspring.dto.TeamDto;
import com.example.tareaspring.dto.TeamPlayerResponseDto;
import com.example.tareaspring.errors.DateFormatException;
import com.example.tareaspring.errors.TeamNotFoundException;
import com.example.tareaspring.models.Player;
import com.example.tareaspring.models.Team;
import com.example.tareaspring.services.TeamServiceImp;
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
@RequestMapping("/teams")
public class TeamController {

    private final TeamServiceImp service;
    private final ModelMapper modelMapper;


    /**
     * Get all teams from database
     */
    @GetMapping
    public ResponseEntity<List<Team>> findAll() {

        return ResponseEntity.ok(service.findAll());
    }


    /**
     * Find a team from database
     */
    @GetMapping("/{id}")
    public ResponseEntity<Team> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new TeamNotFoundException(id));
    }


    /**
     * Create a team in the database
     */
    @PostMapping
    public ResponseEntity<Team> create(@RequestBody @Valid TeamDto teamDto) {
        return ResponseEntity.ok(
                service.create(modelMapper.map(teamDto, Team.class))
        );
    }


    /**
     * Update team info
     */
    @PutMapping
    public ResponseEntity<Team> update(@RequestBody @Valid TeamDto teamDto) {
        return ResponseEntity.ok(
                service.update(modelMapper.map(teamDto, Team.class))
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
    public ResponseEntity<List<Player>> findTeasByPlayerId(@PathVariable Long id) {

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
    public ResponseEntity<List<Team>> upload(@RequestParam("file") MultipartFile file) {
        List<Team> result =  service.parseCSVFileToTeams(file);

        return ResponseEntity.ok(result);
    }
}
