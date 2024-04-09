package com.example.tareaspring.services;

import com.example.tareaspring.dto.PlayerDto;
import com.example.tareaspring.dto.TeamDto;
import com.example.tareaspring.dto.TeamPlayerResponseDto;
import com.example.tareaspring.models.Team;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TeamService {
    List<Team> findAll();
    Optional<Team> findById(Long id);
    Team create(Team team);
    Team update(Team team);
    void deleteById(Long id);
    List<TeamDto> parseCSVFileToTeams(MultipartFile file);
    List<TeamPlayerResponseDto> getTeamSignings(Long id);
    List<PlayerDto> getTeamSigningsPlayers(Long id);
    List<TeamPlayerResponseDto> getSigningsAtDate(Long id, LocalDate date);
}
