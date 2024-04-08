package com.example.tareaspring.services;

import com.example.tareaspring.dto.SigningDto;
import com.example.tareaspring.dto.TeamPlayerResponseDto;
import com.example.tareaspring.models.Player;
import com.example.tareaspring.models.Signing;
import com.example.tareaspring.models.Team;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TeamService {
    List<Team> parseCSVFileToTeams(MultipartFile file);
    List<Team> findAll();
    Optional<Team> findById(Long id);
    Team create(Team Team);
    Team update(Team Team);
    void deleteById(Long id);
    List<TeamPlayerResponseDto> getTeamSignings(Long id);
    List<Player> getTeamSigningsPlayers(Long id);
    List<TeamPlayerResponseDto> getSigningsAtDate(Long id, LocalDate date);
    Boolean isPlayerOrSquadNumberPresentAt(Long teamId, Long playerId, Integer squadNumber, LocalDate date);
    Boolean isPlayerOrSquadNumberPresentAt(SigningDto signingDto);
    Boolean isPlayerOrSquadNumberPresentAt(Signing signing);
    Boolean isSquadNumberPresentAt(Long teamId, Integer squadNumber, LocalDate date);
    Boolean isSquadNumberPresentAt(Signing signing);
    Signing createSigning(Signing signing);
}
