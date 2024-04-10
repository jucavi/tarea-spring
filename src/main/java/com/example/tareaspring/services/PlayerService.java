package com.example.tareaspring.services;

import com.example.tareaspring.dto.PlayerDto;
import com.example.tareaspring.dto.PlayerTeamsResponseDto;
import com.example.tareaspring.dto.TeamDto;
import com.example.tareaspring.models.Player;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface PlayerService {
    List<PlayerDto> findAll();
    PlayerDto findById(Long id);
    PlayerDto create(Player player);
    PlayerDto update(Player player);
    void deleteById(Long id);
    List<PlayerDto> parseCSVFileToPlayers(MultipartFile file);
    List<PlayerTeamsResponseDto> findByPlayerIdAllSignings(Long id);
    List<TeamDto> findByPlayerIdTeamsWerePlayerHasPlayed(Long id);
    List<PlayerTeamsResponseDto> findByPlayerIdSigningAtDate(Long id, LocalDate date);
}
