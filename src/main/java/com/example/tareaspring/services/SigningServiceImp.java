package com.example.tareaspring.services;

import com.example.tareaspring.dto.SigningDto;
import com.example.tareaspring.dto.converter.SigningMapper;
import com.example.tareaspring.errors.*;
import com.example.tareaspring.models.Signing;
import com.example.tareaspring.repositories.SigningRepository;
import com.example.tareaspring.utils.parsers.CSVParser;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class SigningServiceImp implements SigningService {

    private final SigningRepository repository;
    private final SigningMapper signingMapper;

    @Override
    public List<Signing> findAll() {
        log.info("Retrieving all Signings from database");
        return repository.findAll();
    }


    @Override
    public Optional<Signing> findById(Long id) {
        log.info("Retrieving signing with ID: {}", id);
        return repository.findById(id);
    }


    /**
     * Create a signing
     */
    @Override
    public Signing create(Signing signing) {

        //return repository.save(signing);
        if (signing.getId() != null) {
            throw new CreateEntityException("Trying to create a signing, but ID not null");
        }

        if (isValidSigning(signing)) {
            try {
                repository.save(signing);
            } catch (Exception ex) {
                throw new DatabaseSaveException("Unable to create in database: " + signing);
            }
            return signing;
        }

        throw new ValidSigningException("Unable to create invalid signing in database: " + signing);
    }


    /**
     * Update signing
     */
    @Override
    public Signing update(Signing signing) {

        if (signing.getId() == null) {

            throw new CreateEntityException("Error, trying to update signing with ID: null");
        }

        Long id = signing.getId();

        Optional<Signing> oldSigningOpt = repository.findById(id);
        Signing oldSigning;

        if (!oldSigningOpt.isPresent()) {
            signing.setId(null);
            return create(signing);
        } else {
            oldSigning = oldSigningOpt.get();
        }

        // If foreign keys changed
        if (!oldSigning.getPlayer().getId()
                    .equals(signing.getPlayer().getId())
                || !oldSigning.getTeam().getId()
                    .equals(signing.getTeam().getId())) {

            throw new DatabaseSaveException("Foreign keys can't be changed");
      }

    boolean isValidSquadNumber = true;

        if (!oldSigning.getSquadNumber()
                .equals(signing.getSquadNumber())) {

        isValidSquadNumber = isValidSquadNumberAt(
                signing.getTeam().getId(),
                signing.getSquadNumber(),
                signing.getSince(),
                signing.getUntil());
    }

    boolean isValidDateRange = true;

        // if limits range change
        if (!oldSigning.getSince()
                .isEqual(signing.getSince())
            || !oldSigning.getUntil()
                .equals(signing.getUntil())) {

            isValidDateRange = isSigningCanBeModified(
                    signing.getPlayer().getId(),
                    id,
                    signing.getSince(),
                    signing.getUntil());
        }

        if (isValidSquadNumber && isValidDateRange) {
            try {
                Signing result = repository.save(signing);

                log.info("Signing updated with ID: {} to {}", id, signing);

                return result;
            } catch (Exception ex) {
                log.error(ex.getMessage());
                throw new DatabaseSaveException("Unable to update signing: " + signing);
            }

        }

        throw new ValidSigningException("Unable to update invalid signing in database: " + signing);
    }

    /**
     * Delete Signing by id
     */
    @Override
    public void deleteById(Long id) {

        if (repository.existsById(id)) {
            repository.deleteById(id);
            log.info("Signing deleted with ID: {}", id);
            return;
        }

        log.warn("Trying to delete a Signing with wrong ID");
    }


    // TODO: Refactor to
    /**
     * Parse csv file to DAO
     */
    @Override
    public List<SigningDto> parseCSVFileToSignings(@NonNull MultipartFile file) {

        List<SigningDto> result = new ArrayList<>();

        // TODO: Refactor to Generic
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            List<SigningDto> parseResult = CSVParser.parse(reader, SigningDto.class);

            parseResult.forEach((dto -> {
                try {
                    Long id = dto.getId();
                    Signing signing = signingMapper.mapDtoToDao(dto);

                    if (id == null) {
                        create(signing);
                    } else  { // if id rewrite
                        update(signing);
                    }

                    result.add(dto);
                } catch (Exception ex) {
                    log.error("{} can't be stored in the database due to: \n\t{}", dto, ex.getMessage());
                }
            }));

        } catch (Exception ex) {
            log.error("Unable to read csv file: " + ex.getMessage());
        }

        return result;
    }

    /**
     * Check if signing contains valid info
     */
    private Boolean isValidSigning(@NonNull Signing signing) {

        Long playerId = signing.getPlayer().getId();
        Long teamId = signing.getTeam().getId();
        LocalDate since = signing.getSince();
        LocalDate until = signing.getUntil();
        Integer squadNumber = signing.getSquadNumber();

        return isValidSquadNumberAt(teamId, squadNumber, since, until)
                && isPlayerNotSignedAt(playerId, since, until);
    }

    /**
     * Check if player haven't signed in that date range (since, until)
     * {@code true} if player has no contract, otherwise {@code false}
     */
    private Boolean isPlayerNotSignedAt(
            @NonNull Long playerId,
            @NonNull LocalDate since,
            @NonNull LocalDate until) {

        List<Signing> signings = repository.findByPlayerId(playerId);

        for (Signing s : signings) {
            LocalDate currentSince = s.getSince();
            LocalDate currentUntil = s.getUntil();

            // TODO: CHANGE FOR: DateUtilsValidator.isDateInRange(s.getSince(), s.getUntil(), date)
            boolean isSinceDateIn = isDateInRangeInclusive(currentSince, currentUntil, since);
            boolean isUntilDateIn = isDateInRangeInclusive(currentSince, currentUntil, until);
            // TODO: MOVE TO DateUtilsValidator
            boolean isBiggerRange = isDateRangeSubset(currentSince, currentUntil, since, until);

            if (isSinceDateIn || isUntilDateIn || isBiggerRange) {
                log.warn("Player can't sign due to current contract");
                return false;
            }
        }

        return true;
    }

    /**
     * Check if squad number are taken on any signing for that date range (since, until)
     * {@code true} if squad number hasn't taken by any player, otherwise {@code false}
     */
    private boolean isValidSquadNumberAt(
            @NonNull Long teamId,
            @NonNull Integer squadNumber,
            @NonNull LocalDate since,
            @NonNull LocalDate until) {

        List<Signing> signings = repository.findByTeamId(teamId);

        // user can be signed by team, but active signing and isPlayerNotSignedAt -> false
        for (Signing s : signings) {

            LocalDate currentSince = s.getSince();
            LocalDate currentUntil = s.getUntil();

            // if date in any contract check squad number
            // TODO: CHANGE FOR: DateUtilsValidator.isDateInRange(s.getSince(), s.getUntil(), date)
            boolean isSinceDateIn = isDateInRangeInclusive(currentSince, currentUntil, since);
            boolean isUntilDateIn = isDateInRangeInclusive(currentSince, currentUntil, until);
            boolean isBiggerRange = isDateRangeSubset(currentSince, currentUntil, since, until);

            if (isSinceDateIn || isUntilDateIn || isBiggerRange) {

                // if squad number already taken -> false
                if (s.getSquadNumber().equals(squadNumber)) {
                    log.warn("Squad number already taken: " + squadNumber);
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * check if date is in range [startDate, endDate]
     */
    private boolean isDateInRangeInclusive(
            @NonNull LocalDate startDate,
            @NonNull LocalDate endDate,
            @NonNull LocalDate date) {

        // if date between [startDate, endDate]
        // Could be separated in different messages
        if ((startDate.isBefore(date) || startDate.isEqual(date))
                && (endDate.isAfter(date) || endDate.isEqual(date))) {
                    return true;
        }

        log.warn("Range date no valid");
        return false;
    }

    /**
     * check if [starDate, endDate] is included in [since, until]
     */
    private boolean isDateRangeSubset(
            @NonNull LocalDate startDate,
            @NonNull LocalDate endDate,
            @NonNull LocalDate since,
            @NonNull LocalDate until) {

        // if both edges in [since, until] is at least a subset
        if (isDateInRangeInclusive(since, until, startDate)
                && isDateInRangeInclusive(since, until, endDate)) {
            return true;
        }

        log.warn("Range date no valid");
        return false;
    }

    /**
     * Checck if player signing date can be modified
     */
    private Boolean isSigningCanBeModified(
            @NonNull Long playerId,
            @NonNull Long signingId,
            @NonNull LocalDate since,
            @NonNull LocalDate until) {

        List<Signing> signings = repository.findByPlayerId(playerId);

        for (Signing s : signings) {
            LocalDate currentSince = s.getSince();
            LocalDate currentUntil = s.getUntil();

            boolean isSinceDateIn = isDateInRangeInclusive(currentSince, currentUntil, since);
            boolean isUntilDateIn = isDateInRangeInclusive(currentSince, currentUntil, until);
            boolean isBiggerRange = isDateRangeSubset(currentSince, currentUntil, since, until);

            // all signings excluding the one we want to modify
            if (!s.getId().equals(signingId)) {

                if (isSinceDateIn || isUntilDateIn || isBiggerRange) {
                    log.warn("Player can't sign due to contract boundaries");
                    return false;
                }
            }
        }

        return true;
    }
}
