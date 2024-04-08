package com.example.tareaspring.services;

import com.example.tareaspring.dto.SigningDto;
import com.example.tareaspring.dto.TeamCSV;
import com.example.tareaspring.dto.TeamPlayerResponseDto;
import com.example.tareaspring.errors.*;
import com.example.tareaspring.models.Player;
import com.example.tareaspring.models.Signing;
import com.example.tareaspring.models.Team;
import com.example.tareaspring.repositories.PlayerRepository;
import com.example.tareaspring.repositories.SigningRepository;
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
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class TeamServiceImp implements TeamService {

    private final TeamRepository repository;
    private final PlayerRepository playerRepository;
    private final SigningRepository signingRepository;


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


    /**
     * Create a team
     */
    @Override
    public Team create(Team team) {

        if (team.getId() != null) {
            throw new CreateEntityException("Trying to create a team, but ID not null");
        }

        try {
            Team result = repository.save(team);

            log.info("Team created with ID: {}", team.getId());
            return result;

        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new DatabaseSaveException("Unable to create team");
        }
    }

    /**
     * Update team in database
     */
    @Override
    public Team update(Team team) {

        if (team.getId() == null) {
            throw new CreateEntityException("Error, trying to update team with ID: null");
        }

        Long id = team.getId();

        try {
            Team result = repository.save(
                    new Team(
                            id,
                            team.getName(),
                            team.getEmail(),
                            team.getSince(),
                            team.getCity())
            );

            log.info("Team updated with ID: {} to {}", id, team);
            return result;

        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new DatabaseSaveException("Unable to update player: " + team);
        }
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
     * Check if player had been signed already or squad number had been taken
     * at date passed as parameter
     */
    @Override
    public Boolean isPlayerOrSquadNumberPresentAt(Long teamId, Long playerId, Integer squadNumber, LocalDate date) {
        List<TeamPlayerResponseDto> currentPlayers = getSigningsAtDate(teamId, date);

        // TODO: Throw custom Exceptions to write logs for player and number
        Predicate<TeamPlayerResponseDto> squadNumberPresent = tp -> tp.getSquadNumber().equals(squadNumber);
        Predicate<TeamPlayerResponseDto> isPlayerSigned = p -> p.getPlayer().getId().equals(playerId);
        Predicate<TeamPlayerResponseDto> combinedCondition = squadNumberPresent.or(isPlayerSigned);

        return currentPlayers.stream().anyMatch(combinedCondition);
    }

    /**
     * Check if squad number had been taken
     * at date passed as parameter
     */
    @Override
    public Boolean isSquadNumberPresentAt(Long teamId, Integer squadNumber, LocalDate date) {
        List<TeamPlayerResponseDto> currentPlayers = getSigningsAtDate(teamId, date);

        // TODO: Throw custom Exceptions to write logs for player and number -> change to for
        Predicate<TeamPlayerResponseDto> squadNumberPresent = tp -> tp.getSquadNumber().equals(squadNumber);

        return currentPlayers.stream().anyMatch(squadNumberPresent);
    }

    @Override
    public Boolean isSquadNumberPresentAt(Signing signing) {
        Long teamId = signing.getTeam().getId(); // not null
        Integer squadNumber = signing.getSquadNumber(); // 0-99
        LocalDate since = signing.getSince(); // since before until / valid dates
        LocalDate until = signing.getUntil();

        return isSquadNumberPresentAt(teamId, squadNumber, since)
                || isSquadNumberPresentAt(teamId, squadNumber, until);
    }

    /**
     * Check if player had been signed already or squad number had been taken
     * at date passed as parameter
     */
    @Override
    public Boolean isPlayerOrSquadNumberPresentAt(SigningDto signingDto) {
        // @Valid from controller
        Long teamId = signingDto.getTeam().getId(); // not null
        Long playerId = signingDto.getPlayer().getId(); // not null
        Integer squadNumber = signingDto.getSquadNumber(); // 0-99
        LocalDate since = signingDto.getSince(); // since before until / valid dates
        LocalDate until = signingDto.getUntil();

        return isPlayerOrSquadNumberPresentAt(teamId, playerId, squadNumber, since)
                || isPlayerOrSquadNumberPresentAt(teamId, playerId, squadNumber, until);
    }

    /**
     * Check if player had been signed already or squad number had been taken
     * at date passed as parameter
     */
    @Override
    public Boolean isPlayerOrSquadNumberPresentAt(Signing signing) {
        // Valid from controller
        Long teamId = signing.getTeam().getId(); // not null
        Long playerId = signing.getPlayer().getId(); // not null
        Integer squadNumber = signing.getSquadNumber(); // 0-99
        LocalDate since = signing.getSince(); // since before until / valid dates
        LocalDate until = signing.getUntil();

        return isPlayerOrSquadNumberPresentAt(teamId, playerId, squadNumber, since)
                || isPlayerOrSquadNumberPresentAt(teamId, playerId, squadNumber, until);
    }

    @Override
    public Signing createSigning(Signing signing) {

        // TODO: CREATE TEAM OR PLAYER IF NOT EXIST AND VALID
        log.info("Trying to create a signing...");

        Long playerId = signing.getPlayer().getId();
        Optional<Player> optionalPlayer = playerRepository.findById(playerId);

        if (optionalPlayer.isEmpty()) {
            throw new PlayerNotFoundException(playerId);
        }

        Long teamId = signing.getTeam().getId();
        Optional<Team> optionalTeam = repository.findById(teamId);

        if (optionalTeam.isEmpty()) {
            throw new PlayerNotFoundException(teamId);
        }

        Integer squadNumber = signing.getSquadNumber();
        LocalDate dateSince = signing.getSince();
        LocalDate dateUntil = signing.getUntil();

        List<Signing> allSignings = signingRepository.findAll();

        allSignings.forEach(s -> {
            if (s.getPlayer().getId().equals(playerId)) {
                LocalDate since = s.getSince();
                LocalDate until = s.getUntil();

                // TODO: see signing service for logic
                if ((since.isBefore(dateSince)
                        && until.isAfter(dateSince)) // date since in signing range
                        || ((since.isBefore(dateUntil) // date until in signing range
                        && until.isAfter(dateUntil)))) {
                    throw new PlayerAlreadySignedException("Player has a current signing: " + playerId);
                }
            }

            if (s.getPlayer().getId().equals(playerId)
                    && s.getTeam().getId().equals(teamId)
                    && isSquadNumberPresentAt(s)) {
                throw new SquadNumberAlreadyTakenException("Squad number has already taken: " + squadNumber);
            }
        });

        Signing result = signingRepository.save(signing);

        log.info("Signing created with ID: {}", result.getId());
        return result;
    }

    /**
     * Parse data
     */
    @Override
    public List<Team> parseCSVFileToTeams(@NonNull MultipartFile file) {

        List<Team> teams = new ArrayList<>();
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            List<TeamCSV> result = CSVParser.parse(reader, TeamCSV.class);
            result.forEach((teamCsv -> {
                try {
                    Team team = teamCsv.mapToDao();
                    Long id = team.getId();

                    // Si el csv viene con id sobreescribe
                    if (id == null) {
                        create(team);
                    } else  {
                        update(team);
                    }

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
