package com.example.tareaspring.services;

import com.example.tareaspring.models.Player;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface PlayerService {

    List<Player> parseCSVFileToPlayers(MultipartFile file);
    List<Player> findAll();
    Optional<Player> findById(Long id);
    Player create(Player player);
    Player update(Player player);
    Boolean deleteById(Long id);
}
