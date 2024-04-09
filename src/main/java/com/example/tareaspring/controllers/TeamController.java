package com.example.tareaspring.controllers;

import com.example.tareaspring.dto.PlayerDto;
import com.example.tareaspring.dto.TeamDto;
import com.example.tareaspring.dto.TeamPlayerResponseDto;
import com.example.tareaspring.dto.converter.TeamMapper;
import com.example.tareaspring.errors.TeamNotFoundException;
import com.example.tareaspring.services.TeamServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams")
@Tag(name = "Teams")
public class TeamController {

    private final TeamServiceImp service;
    private final TeamMapper teamMapper;


    /**
     * Get all teams from database
     */
    @GetMapping
    @Operation(summary = "All teams", description = "Retrieve all teams from database")
    public ResponseEntity<List<TeamDto>> findAll() {
        return ResponseEntity.ok(
                service.findAll()
                        .stream()
                        .map(teamMapper::mapDaoToDto)
                        .collect(Collectors.toList()));
    }


    /**
     * Find a team from database
     */
    @GetMapping("/{id}")
    public ResponseEntity<TeamDto> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(teamMapper::mapDaoToDto)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new TeamNotFoundException(id));
    }


    /**
     * Create a team in the database
     */
    @PostMapping
    public ResponseEntity<TeamDto> create(@RequestBody @Valid TeamDto teamDto) {
        return ResponseEntity.ok(
                teamMapper.mapDaoToDto(
                        service.create(
                                teamMapper.mapDtoToDao(teamDto))));
    }


    /**
     * Update team info
     */
    @PutMapping
    public ResponseEntity<TeamDto> update(@RequestBody @Valid TeamDto teamDto) {
        return ResponseEntity.ok(
                teamMapper.mapDaoToDto(
                        service.update(
                                teamMapper.mapDtoToDao(teamDto))));
    }


    /**
     * Delete a team from a database
     * @param id team identifier
     * @return {@code true} if a team was deleted, {@code 404 } not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
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
     * Persist all registries from csv file into database
     * @param file file
     * @return List of teams persisted, otherwise an empty list
     */
    @PostMapping("/upload")
    public ResponseEntity<List<TeamDto>> upload(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(service.parseCSVFileToTeams(file));
    }
}
