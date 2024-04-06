package com.example.tareaspring.services;

import com.example.tareaspring.dto.SigningCSV;
import com.example.tareaspring.models.Player;
import com.example.tareaspring.models.Signing;
import com.example.tareaspring.models.Team;
import com.example.tareaspring.repositories.SigningRepository;
import com.example.tareaspring.utils.parsers.CSVParser;

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

// TODO: Move to Lombok @log annotation
@Log4j2
@Service
public class SigningServiceImp implements SigningService {

    private final SigningRepository repository;

    public SigningServiceImp(SigningRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Signing> findAll() {
        log.info("Retrieving all Signings from database");
        return repository.findAll();
    }

    @Override
    public Signing findById(Long id) {
        Optional<Signing> signingOpt = repository.findById(id);

        if (signingOpt.isPresent()) {
            log.info("Retrieving Signing with ID: {}", id);
            return signingOpt.get();
        }
        log.warn("Trying to retrieve Signing with wrong ID: {}", id);
        return null;
    }

    /*
    TODO:
        First: need a valid until date
        Second: check for squad number
        If Signing at current date is signed, then
            if it is same team and 'until date' is grater than actual Signing 'until date', then
                * (extend signing)
                    * Check for squad number
                    * Update 'until date' with new 'until date'
                * Write logs
            if it is not the same team and 'until date' is greater than current date, then
                * (new signing)
                    * if 'since date' is before or equal current date and new 'until date' is after current date
                        * Check for squad number
                        * Close current signing 'until date' with current date and update 'since date' to current date to the new signing
                    * if since date is a after de current date and new 'until date' is after current date
                        * Check for squad number
                        * keep all data
        If Signing at current date is not signed, persist
    */
    @Override
    public Signing create(Signing signing) {

        // Only new Signings available
        if (signing.getId() != null) {
            // maybe trow exception message to catch in controller
            log.warn("Trying to create a Signing with assigned ID: {}", signing.getId());
            return null;
        }

        if (!isValidSigning(signing)) {
            return null;
        }

        List<Signing> SigningSignings = repository.findByPlayer(signing.getPlayer());
        Signing currentSigning = findCurrentSigningSigning(SigningSignings);

        // If Signing where not signings or at current date are not signed
        if (SigningSignings.isEmpty() || currentSigning == null) {
            if (isValidSquadNumber(signing)) {
                Signing result = repository.save(signing);
                log.info("Created signing with ID: {}", result.getId() );
                return result;
            } else {
                log.warn("Trying to create a signing with squad number");
                return null;
            }
        }

        // if signing on a same team
        boolean isSigningModification = currentSigning.getTeam().equals(signing.getTeam());

        // Signing with existing contract
        if (isSigningModification) {
            // Update data with new signing values keeping since date
            signing.setId(currentSigning.getId());
            signing.setSince(currentSigning.getSince());

            Signing result = repository.save(signing);
            log.info("Updated signing with ID: {}", result.getId());

            return result;
        } else {

            boolean isNewSigningWithContractInForce = currentSigning.getUntil().isBefore(signing.getSince());

            if (isNewSigningWithContractInForce) {
                currentSigning.setUntil(signing.getSince());
                // Modify current contract
                log.info("Updated contract in force with ID: {}, due new contract ", currentSigning.getId());
                repository.save(currentSigning);
            }
        }

        Signing result = repository.save(signing);
        log.info("Created signing with ID: {}", result.getId() );

        return result;
    }

    @Override
    public Signing update(Signing Signing) {
        throw new RuntimeException("Not implemented");
    }


    public List<Signing> parseCSVFileToSignings(@NonNull MultipartFile file) {

        List<Signing> signings = new ArrayList<>();
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            List<SigningCSV> result = CSVParser.parse(reader, SigningCSV.class);
            result.forEach((signingCsv -> {
                try {
                    Signing signing = signingCsv.mapToDao();

                    repository.save(signing);
                    signings.add(signing);
                } catch (Exception ex) {
                    log.error("{} can't be stored in the database due to: \n\t{}", signingCsv, ex.getMessage());
                }
            }));

        } catch (Exception ex) {
            log.error("Unable to read csv file.");
        }

        return signings;
    }

    /**
     * Check if signing contains valid data
     * @param signing signing
     * @return {@code true} if is valid, {@code false} otherwise
     */
    private boolean isValidSigning(Signing signing) {
        LocalDate today = LocalDate.now();
        LocalDate until = signing.getUntil();
        LocalDate since = signing.getSince();

        // needed because properties of those objects send in request are null
        Team team = repository.findByTeamWithId(signing.getTeam().getId());
        Player player = repository.findByPlayerWithId(signing.getPlayer().getId());

        // fk needed, need some kind of validation in form
        if (team == null || player == null) {
            log.warn("Trying to create a Signing with non available team or Signing");
            return false;
        }

        // Signings with invalid expiration date
        if (until != null && until.isBefore(today)) {
            log.warn("Trying to create a Signing with invalid expiring date: {}", until);
            return false;
        }

        if (until == null || since.isBefore(until)) {
            return true;
        }

        log.warn("Invalid range of dates");
        return false;
    }


    /**
     * Check if the squad number of a Signing do not exist un current signings
     * @return {@code true} if squad number is valid, {@code false} otherwise
     */
    private boolean isValidSquadNumber(Signing signing) {
        // Check nullable team before
        List<Signing> teamSignings = repository.findByTeam(signing.getTeam());

        for (Signing s : teamSignings) {
            boolean isSameSigning = s.getPlayer().equals(signing.getPlayer());

            if (isSigningActive(s) && !isSameSigning) {
                if (s.getSquadNumber().equals(signing.getSquadNumber())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Signing with contract in force
     * <p>Not future signing are considered</p>
     * @param signing signing
     * @return {@code true} if Signing has contract in force, {@code false} otherwise
     */
    private boolean isSigningActive(Signing signing) {
        LocalDate today = LocalDate.now();
        LocalDate until = signing.getUntil();

        // signing expiration should be after current date
        // inclusive signing ending? if true || until.isEqual(today)
        return until == null || until.isAfter(today);
    }

    /**
     * Find the current signing of a Signing
     * @param SigningSignings list of Signing signings
     * @return a current signing if exists, else {@code null}
     */
    private Signing findCurrentSigningSigning(List<Signing> SigningSignings) {

        for (Signing signing : SigningSignings) {
            if (isSigningActive(signing)) {
                return signing;
            }
        }
        return null;
    }

    @Override
    public Boolean deleteById(Long id) {

        if (repository.existsById(id)) {
            repository.deleteById(id);
            log.info("Signing deleted with ID: {}", id);
            return true;
        }

        log.warn("Trying to delete a Signing with wrong ID");
        return false;
    }
}
