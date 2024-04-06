package com.example.tareaspring.services;

import com.example.tareaspring.models.Player;
import com.example.tareaspring.dto.PlayerCSV;
import com.example.tareaspring.repositories.PlayerRepository;
import com.example.tareaspring.utils.parsers.CSVParser;

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

// TODO: Move to Lombok @log annotation
@Log4j2
@Service
public class PlayerServiceImp implements PlayerService {

    private final PlayerRepository repository;

    public PlayerServiceImp(PlayerRepository repository) {
        this.repository = repository;
    }

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
        log.info("Retrieving all Teams from database");
        return repository.findAll();
    }

    @Override
    public Player findById(Long id) {
        Optional<Player> playerOpt = repository.findById(id);

        if (playerOpt.isPresent()) {
            log.info("Retrieving Player with ID: {}", id);
            return playerOpt.get();
        }
        log.warn("Retrieving Player with wrong ID: {}", id);
        return null;
    }

    @Override
    public Player create(Player player) {

        if (player.getId() != null) {
            log.warn("Trying to create a Player with assigned ID: {}", player.getId());
            return null;
        }

        Player result = repository.save(player);

        log.info("Player created with ID: {}", player.getId());
        return result;
    }

    @Override
    public Player update(Player player) {

        if (player.getId() != null && !repository.existsById(player.getId())) {
            log.warn("Trying to update a Player with wrong ID");
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
            log.info("Player deleted with ID: {}", id);
            return true;
        }

        log.warn("Trying to delete a Player with wrong ID");
        return false;
    }
}
