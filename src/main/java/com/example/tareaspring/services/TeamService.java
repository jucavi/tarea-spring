package com.example.tareaspring.services;

import com.example.tareaspring.models.PlayerCSV;
import com.example.tareaspring.models.Team;
import com.example.tareaspring.models.TeamCSV;
import com.example.tareaspring.repositories.TeamRepository;
import com.opencsv.bean.BeanVerifier;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Service
public class TeamService {

    private final Logger log = LoggerFactory.getLogger(TeamService.class);
    private final TeamRepository repository;

    public TeamService(TeamRepository repository) {
        this.repository = repository;
    }

    // TODO: Refactorize
    public List<Team> parseCSVFileToTeams(@NonNull MultipartFile file) {

        List<Team> teams = new ArrayList<>();

        // validamos que el fichero existe
        if (file.isEmpty()) {
            log.warn("Not CSV file present to upload");
            return teams;
        }

        // parsemos el fichero CSV a una lista de objetos Team
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<TeamCSV> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(TeamCSV.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withVerifier(new BeanVerifier() {
                        @Override
                        public boolean verifyBean(Object bean) throws CsvConstraintViolationException {
                            TeamCSV b = (TeamCSV) bean;
                            // Todas propiedades nulas
                            if (b == null) {
                                return false;
                            }
                            return true;
                        }
                    })
                    .build();

            List<TeamCSV> teamsObj = csvToBean.parse();

            for (TeamCSV p : teamsObj) {
                Team Team = p.toBeanWithId();
                teams.add(Team);
            }

            repository.saveAll(teams);

        } catch (Exception ex) {
            log.error("An error occurred while processing the CSV file.");
        }

        return teams;
    }

    public List<Team> findAll() {
        log.info("Retrieving all Teams from database");
        return repository.findAll();
    }

    public Team findById(Long id) {
        Optional<Team> teamOpt = repository.findById(id);

        if (teamOpt.isPresent()) {
            log.info("Retrieving Team with ID: {}", id);
            return teamOpt.get();
        }
        log.warn("Retrieving Team with wrong ID: {}", id);
        return null;
    }

    public Team create(Team team) {

        if (team.getId() != null) {
            log.warn("Trying to create a Team with assigned ID: {}", team.getId());
            return null;
        }

        Team result = repository.save(team);
        log.info("Team created with ID: {}", team.getId());

        return result;
    }

    public Team update(Team team) {

        if (team.getId() != null && !repository.existsById(team.getId())) {
            log.warn("Trying to update a Team with wrong ID");
            return null;
        }

        Team result = repository.save(team);
        log.info("Team updated with ID: {}", team.getId());

        return result;
    }

    public Boolean delete(Long id) {

        if (repository.existsById(id)) {
            repository.deleteById(id);
            log.info("Team deleted with ID: {}", id);
            return true;
        }

        log.warn("Trying to delete a Team with wrong ID");
        return false;
    }
}
