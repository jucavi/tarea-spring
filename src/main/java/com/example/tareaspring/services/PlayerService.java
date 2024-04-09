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

    List<PlayerDto> parseCSVFileToPlayers(MultipartFile file);
    List<PlayerDto> findAll();
    Optional<PlayerDto> findById(Long id);
    PlayerDto create(PlayerDto playerDto);
    PlayerDto update(PlayerDto playerDto);
    void deleteById(Long id);
    List<PlayerTeamsResponseDto> getUserSignings(Long id);
    List<Team> getUserSigningsTeams(Long id);
    List<PlayerTeamsResponseDto> getUserSigningAtDate(Long id, LocalDate date);
}
