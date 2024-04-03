package com.example.tareaspring.services;

import com.example.tareaspring.models.Player;
import com.example.tareaspring.models.PlayerCSV;
import com.example.tareaspring.repositories.PlayerRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    private final Logger log = LoggerFactory.getLogger(PlayerService.class);
    private final PlayerRepository repository;

    public PlayerService(PlayerRepository repository) {
        this.repository = repository;
    }


    public ResponseEntity parseCSVFileToPlayers(@NonNull MultipartFile file) {

        List<Player> players = new ArrayList<>();

        // validamos que el fichero existe
        if (file.isEmpty()) {
            log.warn("Not CSV file present to upload");
            return ResponseEntity.noContent().build();
        }

        // parsemos el fichero CSV a una lista de objetos Player
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<PlayerCSV> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(PlayerCSV.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<PlayerCSV> playersObj = csvToBean.parse();

            for (PlayerCSV p : playersObj) {
                Player player = p.toBeanWithId();
                players.add(player);
            }

            repository.saveAll(players);

        } catch (Exception ex) {
            log.error("An error occurred while processing the CSV file.");
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(players);
    }

    public List<Player> findAll() {
        log.info("Retrieving all Teams from database");
        return repository.findAll();
    }

    public ResponseEntity<Player> findById(Long id) {
        Optional<Player> playerOpt = repository.findById(id);

        if (playerOpt.isPresent()) {
            log.info("Retrieving Player with ID: {}", id);
            return ResponseEntity.ok(playerOpt.get());
        }
        log.warn("Retrieving Player with wrong ID: {}", id);
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<Player> create(Player player) {

        if (player.getId() != null) {
            log.warn("Trying to create a Player with assigned ID: {}", player.getId());
            return ResponseEntity.badRequest().build();
        }

        // guardar el jugador recibido por parámetro en la base de datos
        Player result = repository.save(player);
        log.info("Player created with ID: {}", player.getId());

        return ResponseEntity.ok(result);
    }

    public ResponseEntity<Player> update(Player player) {

        if (player.getId() != null && !repository.existsById(player.getId())) {
            log.warn("Trying to update a jugador with wrong ID");
            return ResponseEntity.notFound().build();
        }

        Player result = repository.save(player);
        log.info("Player updated with ID: {}", player.getId());

        return ResponseEntity.ok(result);
    }

    public ResponseEntity delete(Long id) {

        if (repository.existsById(id)) {
            repository.deleteById(id);
            log.info("Player deleted with ID: {}", id);
            return ResponseEntity.noContent().build();
        }

        log.warn("Trying to delete a Player with wrong ID");
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity delete() {
        log.info("Deleting all Players...");
        repository.deleteAll();
        return ResponseEntity.noContent().build();
    }

}
