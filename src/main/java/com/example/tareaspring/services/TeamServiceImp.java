package com.example.tareaspring.services;

import com.example.tareaspring.dto.TeamCSV;
import com.example.tareaspring.dto.TeamPlayerResponseDto;
import com.example.tareaspring.errors.CreateEntityException;
import com.example.tareaspring.errors.TeamNotFoundException;
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


    /**
     * Retrieve all teams
     * @return list of teams in the database
     */
    @Override
    public List<Team> findAll() {
        log.info("Retrieving all Teams from database");
        return repository.findAll();
    }

    /**
     * Find team by id
     */
    @Override
    public Optional<Team> findById(Long id) {
        log.info("Retrieving team with ID: {}", id);
        return repository.findById(id);
    }

    @Override
    public Team create(Team team) {

        if (team.getId() != null) {
            throw new CreateEntityException("Change ID yo null");
        }

        Team result = repository.save(team);

        log.info("Team created with ID: {}", team.getId());
        return result;
    }

    /**
     * Update team in database
     */
    @Override
    public Team update(Team team) {

        if (team.getId() == null) {
            throw new TeamNotFoundException(team.getId());
        }

        Team result = repository.save(team);

        log.info("Team updated with ID: {}", team.getId());
        return result;
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

    @Override
    public List<Player> getTeamSigningsPlayers(Long id) {

        repository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException(id));

        log.info("Trying to retrieve all teams from player with ID: {}", id);

        return repository.findById(id)
                .get()
                .getSignings()
                .stream()
                .map(Signing::getPlayer)
                .distinct()
                .collect(Collectors.toList());
    }

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

    @Override
    public List<Team> parseCSVFileToTeams(@NonNull MultipartFile file) {

        List<Team> teams = new ArrayList<>();
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            List<TeamCSV> result = CSVParser.parse(reader, TeamCSV.class);
            result.forEach((teamCsv -> {
                try {
                    Team team = teamCsv.mapToDao();

                    repository.save(team);
                    teams.add(team);
                } catch (Exception ex) {
                    log.error("{} can't be stored in the database due to: \n\t{}", teamCsv, ex.getMessage());
                }
            }));

        } catch (Exception ex) {
            log.error("Unable to read csv file.");
        }

        return teams;
    }
}
