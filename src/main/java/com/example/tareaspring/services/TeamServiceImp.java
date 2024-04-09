package com.example.tareaspring.services;

import com.example.tareaspring.dto.PlayerDto;
import com.example.tareaspring.dto.TeamCSV;
import com.example.tareaspring.dto.TeamDto;
import com.example.tareaspring.dto.TeamPlayerResponseDto;
import com.example.tareaspring.dto.converter.PlayerMapper;
import com.example.tareaspring.dto.converter.TeamMapper;
import com.example.tareaspring.errors.*;
import com.example.tareaspring.models.Player;
import com.example.tareaspring.models.Signing;
import com.example.tareaspring.models.Team;
import com.example.tareaspring.repositories.TeamRepository;
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

@Service
@RequiredArgsConstructor
@Log4j2
public class TeamServiceImp implements TeamService {

    private final TeamRepository repository;
    private final TeamMapper teamMapper;
    private final PlayerMapper playerMapper;


    /**
     * Retrieve all teams
     * @return list of teams in the database
     */
    @Override
    public List<TeamDto> findAll() {
        log.info("Retrieving all Teams from database");
        return repository.findAll()
                .stream()
                .map(teamMapper::mapDaoToDto)
                .collect(Collectors.toList());
    }


    /**
     * Find team by id
     */
    @Override
    public Optional<TeamDto> findById(Long id) {
        log.info("Retrieving team with ID: {}", id);
        return repository.findById(id)
                .map(teamMapper::mapDaoToDto);
    }


    /**
     * Create a team
     */
    @Override
    public TeamDto create(TeamDto teamDto) {

        if (teamDto.getId() != null) {
            throw new CreateEntityException("Trying to create a team, but ID not null");
        }

        Team team;
        try {
            team = repository.save(
                    teamMapper.mapDtoToDao(teamDto));

            log.info("Team created with ID: {}", team.getId());

        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new DatabaseSaveException("Unable to create a team");
        }

        return teamMapper.mapDaoToDto(team);
    }

    /**
     * Update team in database
     */
    @Override
    public TeamDto update(TeamDto teamDto) {

        if (teamDto.getId() == null) {
            throw new CreateEntityException("Error, trying to update team with ID: null");
        }

        Team team;
        try {
             team = repository.save(
                    teamMapper.mapDtoToDao(teamDto));

            log.info("Team updated with ID: {} to {}", team.getId(), team);

        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new DatabaseSaveException("Unable to update player: " + teamDto);
        }

        return teamMapper.mapDaoToDto(team);
    }

    /**
     * Delete team from database
     */
    @Override
    public void deleteById(Long id) {

        log.info("Trying to delete a team with ID: {}", id);
        repository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException(id));

        repository.deleteById(id);
    }

    /**
     * All signings for a team
     */
    @Override
    public List<TeamPlayerResponseDto> getTeamSignings(Long id) {

        repository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException(id));

        log.info("Trying to retrieve all player signings from team with ID: {}", id);

        return repository.findById(id)
                .get()
                .getSignings()
                .stream()
                .map(s ->
                        TeamPlayerResponseDto.builder()
                                .player(s.getPlayer())
                                .since(s.getSince())
                                .until(s.getUntil())
                                .squadNumber(s.getSquadNumber())
                                .build()
                )
                .collect(Collectors.toList());
    }

    /**
     * All players signed by a team
     */
    @Override
    public List<PlayerDto> getTeamSigningsPlayers(Long id) {

        repository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException(id));

        log.info("Trying to retrieve all teams from player with ID: {}", id);

        return repository.findById(id)
                .get()
                .getSignings()
                .stream()
                .map(Signing::getPlayer)
                .map(playerMapper::mapDaoToDto)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * All players signed by a team at date passed as parameter
     */
    @Override
    public List<TeamPlayerResponseDto> getSigningsAtDate(Long id, LocalDate date) {

        log.info("Trying to retrieve all players signed by team Id: {} at {}", id, date);

        repository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException(id));


        return repository.findById(id)
                .get()
                .getSignings()
                .stream()
                .filter(s ->
                    // signings in range since <= date <= until
                    DateUtilsValidator.isDateInRange(s.getSince(), s.getUntil(), date)
                )
                .map(s ->
                    TeamPlayerResponseDto.builder()
                            .player(s.getPlayer())
                            .since(s.getSince())
                            .until(s.getUntil())
                            .squadNumber(s.getSquadNumber())
                            .build()
                )
                .collect(Collectors.toList());
    }


    /**
     * Populate database from csv file
     * @param file file
     * @return a list of elements persisted
     */
    @Override
    public List<TeamDto> parseCSVFileToTeams(@NonNull MultipartFile file) {

        List<TeamDto> result = new ArrayList<>();

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            List<TeamDto> parseResult = CSVParser.parse(reader, TeamDto.class);
            parseResult.forEach((dto -> {
                try {
                    Long id = dto.getId();

                    // with id: then rewrite
                    if (id == null) {
                        create(dto);
                    } else  {
                        update(dto);
                    }
                    result.add(dto);
                } catch (Exception ex) {
                    log.error("{} can't be stored in the database due to: \n\t{}", dto, ex.getMessage());
                }
            }));

        } catch (Exception ex) {
            log.error("Unable to read csv file.");
        }

        return result;
    }
}
