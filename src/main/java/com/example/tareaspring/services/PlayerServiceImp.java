package com.example.tareaspring.services;

import com.example.tareaspring.dto.PlayerDto;
import com.example.tareaspring.dto.PlayerTeamsResponseDto;
import com.example.tareaspring.dto.converter.PlayerMapper;
import com.example.tareaspring.errors.CreateEntityException;
import com.example.tareaspring.errors.DatabaseSaveException;
import com.example.tareaspring.errors.PlayerNotFoundException;
import com.example.tareaspring.models.Player;
import com.example.tareaspring.models.Signing;
import com.example.tareaspring.models.Team;
import com.example.tareaspring.repositories.PlayerRepository;
import com.example.tareaspring.utils.parsers.CSVParser;

import com.example.tareaspring.utils.validators.utils.DateUtilsValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// TODO: Modify to send custom response ??? PlayerResponseDto ???
@Log4j2
@RequiredArgsConstructor
@Service
public class PlayerServiceImp implements PlayerService {

    private final PlayerRepository repository;
    private final PlayerMapper playerMapper;


    /**
     * Retrieve all players
     * @return list of players in the database
     */
    @Override
    public List<PlayerDto> findAll() {
        log.info("Retrieving all players from database");
        return repository.findAll()
                .stream()
                .map(playerMapper::mapDaoToDto)
                .collect(Collectors.toList());
    }

    /**
     * Find player by ID
     */
    @Override
    public Optional<PlayerDto> findById(Long id) {
        log.info("Retrieving player with ID: {}", id);
        return repository.findById(id)
                .map(playerMapper::mapDaoToDto);
    }

    /**
     * Create player in database
     */
    @Override
    public PlayerDto create(PlayerDto playerDto) {

        if (playerDto.getId() != null) {
            throw new CreateEntityException("Trying to create a player, but ID not null");
        }

        Player player;
        try {
             player = repository.save(
                    playerMapper.mapDtoToDao(playerDto));

            log.info("Player created with ID: {}", player.getId());

        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new DatabaseSaveException("Unable to create Player");
        }
        return playerMapper.mapDaoToDto(player);
    }


    /**
     * Update player in database
     */
    @Override
    public PlayerDto update(PlayerDto playerDto) {

        if (playerDto.getId() == null) {
            throw new CreateEntityException("Error, trying to update player with ID: null");
        }

        Long id = playerDto.getId();
        Player player;

        try {
             player = repository.save(
                     playerMapper.mapDtoToDao(playerDto)
            );
            log.info("Player updated with ID: {} to {}", id, playerDto);

        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new DatabaseSaveException("Unable to update player: " + playerDto);
        }

        return playerMapper.mapDaoToDto(player);
    }

    /**
     * Delete a player from database
     */
    @Override
    public void deleteById(Long id) {

        log.info("Trying to delete a player with ID: {}", id);
        repository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));

        repository.deleteById(id);
    }

    /**
     * All player signings
     */
    @Override
    public List<PlayerTeamsResponseDto> getUserSignings(Long id) {

        repository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));

        log.info("Trying to retrieve all signings from player with ID: {}", id);

        return repository.findById(id)
                .get()
                .getSignings()
                .stream()
                .map(s ->
                        PlayerTeamsResponseDto.builder()
                                .team(s.getTeam())
                                .since(s.getSince())
                                .until(s.getUntil())
                                .squadNumber(s.getSquadNumber())
                                .build()
                )
                .collect(Collectors.toList());
    }

    /**
     * All the teams where a player has played
     */
    @Override
    public List<Team> getUserSigningsTeams(Long id) {

        repository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));

        log.info("Trying to retrieve all teams from player with ID: {}", id);

        return repository.findById(id)
                .get()
                .getSignings()
                .stream()
                .map(Signing::getTeam)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<PlayerTeamsResponseDto> getUserSigningAtDate(Long id, LocalDate date) {

        repository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));

        log.info("Trying to retrieve team where a player with ID: {} has signed at {}", id, date);

        return repository.findById(id)
                .get()
                .getSignings()
                .stream()
                .filter(s ->
                        // signings in range since <= date <= until
                        DateUtilsValidator.isDateInRange(s.getSince(), s.getUntil(), date)
                )
                .map(s ->
                        PlayerTeamsResponseDto.builder()
                                .team(s.getTeam())
                                .since(s.getSince())
                                .until(s.getUntil())
                                .squadNumber(s.getSquadNumber())
                                .build()
                )
                .collect(Collectors.toList());
    }


    @Override
    public List<PlayerDto> parseCSVFileToPlayers(@NonNull MultipartFile file) {

        List<PlayerDto> result = new ArrayList<>();
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            List<PlayerDto> parseResult = CSVParser.parse(reader, PlayerDto.class);

            parseResult.forEach(dto -> {
                try {
                    Long id = dto.getId();

                    // Si el csv viene con id sobreescribe
                    if (id == null) {
                        create(dto);
                    } else  {
                        update(dto);
                    }

                    result.add(dto);
                } catch (Exception ex) {
                    log.error("{} can't be stored in the database due to: \n\t{}", dto, ex.getMessage());
                }
            });

        } catch (Exception ex) {
            log.error("Unable to read csv file.");
        }

        return result;
    }
}
