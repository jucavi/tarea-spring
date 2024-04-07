package com.example.tareaspring.services;

import com.example.tareaspring.models.Player;
import com.example.tareaspring.dto.PlayerCSV;
import com.example.tareaspring.repositories.PlayerRepository;
import com.example.tareaspring.utils.parsers.CSVParser;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class PlayerServiceImp implements PlayerService {

    private final PlayerRepository repository;


    @Override
    public List<Player> parseCSVFileToPlayers(@NonNull MultipartFile file) {

        List<Player> players = new ArrayList<>();
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            List<PlayerCSV> result = CSVParser.parse(reader, PlayerCSV.class);
            result.forEach((playerCsv -> {
                try {
                    Player player = playerCsv.mapToDao();

                    repository.save(player);
                    players.add(player);
                } catch (Exception ex) {
                    log.error("{} can't be stored in the database due to: \n\t{}", playerCsv, ex.getMessage());
                }
            }));

        } catch (Exception ex) {
            log.error("Unable to read csv file.");
        }

        return players;
    }

    @Override
    public List<Player> findAll() {
        log.info("Retrieving all players from database");
        return repository.findAll();
    }

    @Override
    public Optional<Player> findById(Long id) {
        log.info("Retrieving player with ID: {}", id);
        return repository.findById(id);
    }

    @Override
    public Player create(Player player) {

        if (player.getId() != null) {
            log.warn("Trying to create a player with assigned ID: {}", player.getId());
            return null;
        }

        Player result = repository.save(player);

        log.info("Player created with ID: {}", player.getId());
        return result;
    }

    @Override
    public Player update(Player player) {

        //repository.findById(player.getId()).orElse(null);
        if (player.getId() != null && !repository.existsById(player.getId())) {
            log.warn("Trying to update a player with wrong ID");
            return null;
        }

        Player result = repository.save(player);
        log.info("Player updated with ID: {}", player.getId());

        return result;
    }

    @Override
    public Boolean deleteById(Long id) {

        if (repository.existsById(id)) {
            repository.deleteById(id);
            log.info("Player deleted");
            return true;
        }

        log.warn("Trying to delete a Player with wrong ID");
        return false;
    }
}
