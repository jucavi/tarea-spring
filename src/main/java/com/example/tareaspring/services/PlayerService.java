package com.example.tareaspring.services;

import com.example.tareaspring.dto.PlayerDto;
import com.example.tareaspring.dto.PlayerTeamsResponseDto;
import com.example.tareaspring.models.Player;
import com.example.tareaspring.models.Team;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PlayerService {

    List<Player> findAll();
    Optional<Player> findById(Long id);
    Player create(Player player);
    Player update(Player player);
    void deleteById(Long id);
    List<PlayerDto> parseCSVFileToPlayers(MultipartFile file);
    List<PlayerTeamsResponseDto> getUserSignings(Long id);
    List<Team> getUserSigningsTeams(Long id);
    List<PlayerTeamsResponseDto> getUserSigningAtDate(Long id, LocalDate date);
}
