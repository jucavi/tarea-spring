package com.example.tareaspring.services;

import com.example.tareaspring.models.Player;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PlayerService {

    List<Player> parseCSVFileToPlayers(MultipartFile file);
    List<Player> findAll();
    Player findById(Long id);
    Player create(Player player);
    Player update(Player player);
    public Boolean deleteById(Long id);
}
