package com.example.tareaspring.services;

import com.example.tareaspring.dto.TeamDto;
import com.example.tareaspring.dto.TeamPlayerResponseDto;
import com.example.tareaspring.models.Player;
import com.example.tareaspring.models.Team;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TeamService {
    List<TeamDto> parseCSVFileToTeams(MultipartFile file);
    List<TeamDto> findAll();
    Optional<TeamDto> findById(Long id);
    TeamDto create(Team TeamDto);
    TeamDto update(Team TeamDto);
    void deleteById(Long id);
    List<TeamPlayerResponseDto> getTeamSignings(Long id);
    List<PlayerDto> getTeamSigningsPlayers(Long id);
    List<TeamPlayerResponseDto> getSigningsAtDate(Long id, LocalDate date);
}
