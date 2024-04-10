package com.example.tareaspring.services;

import com.example.tareaspring.dto.SigningDto;
import com.example.tareaspring.dto.converter.SigningMapper;
import com.example.tareaspring.errors.*;
import com.example.tareaspring.models.Player;
import com.example.tareaspring.models.Signing;
import com.example.tareaspring.repositories.SigningRepository;
import com.example.tareaspring.utils.DateUtils;
import com.example.tareaspring.utils.parsers.CSVParser;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class SigningServiceImp implements SigningService {

    private final SigningRepository repository;
    private final SigningMapper signingMapper;

    @Override
    public List<SigningDto> findAll() {
        log.info("Retrieving all Signings from database");
        return repository.findAll()
                .stream()
                .map(signingMapper::mapDaoToDto)
                .collect(Collectors.toList());
    }


    @Override
    public SigningDto findById(Long id) {
        log.info("Retrieving signing with ID: {}", id);

        Optional<Signing> signing = repository.findById(id);

        if (signing.isEmpty()) {
            throw new PlayerNotFoundException(id);
        }
        return signingMapper.mapDaoToDto(signing.get());
    }


    /**
     * Create a signing
     */
    @Override
    public SigningDto create(Signing signing) {

        if (signing.getId() != null) {
            throw new CreateEntityException("Trying to create a signing, but ID not null");
        }

        Signing result;
        if (isValidSigning(signing)) {
            try {
                result = repository.save(signing);

                log.info("Signing created with ID: {}", result.getId());
            } catch (Exception ex) {
                throw new DatabaseSaveException("Unable to create signing: " + signing);
            }

            return signingMapper.mapDaoToDto(signing);
        }

        throw new ValidSigningException("Unable to create invalid signing in database: " + signing);
    }


    /**
     * Update signing
     */
    @Override
    public SigningDto update(Signing signing) {

        if (signing.getId() == null) {
            throw new CreateEntityException("Error, trying to update signing with ID: null");
        }

        Long id = signing.getId();

        Optional<Signing> oldSigningOpt = repository.findById(id);
        Signing oldSigning;

        if (oldSigningOpt.isEmpty()) {
            // Different validation
            signing.setId(null); // set as new signing

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

            isValidSquadNumber = isValidSquadNumberAt(signing);

        }

        boolean isValidDateRange = true;

        // if limits range change
        if (!oldSigning.getSince()
                .isEqual(signing.getSince())
            || !oldSigning.getUntil()
                .equals(signing.getUntil())) {

            isValidDateRange = isSigningCanBeModified(signing);
        }

        if (isValidSquadNumber && isValidDateRange) {
            try {
                Signing result = repository.save(signing);

                log.info("Signing updated with ID: {} to {}", id, signing);

                return signingMapper.mapDaoToDto(result);
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
        //      interface ServiceI<S> {create, update}
        //      method csvToDatabase -> ServiceI service, File, Class<DTO>
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

        return isValidSquadNumberAt(signing)
                && isPlayerNotSignedAt(signing);
    }

    /**
     * Check if player haven't signed in that date range (since, until)
     * {@code true} if player has no contract, otherwise {@code false}
     */
    private Boolean isPlayerNotSignedAt(Signing signing) {

        Long playerId = signing.getPlayer().getId();
        LocalDate since = signing.getSince();
        LocalDate until = signing.getUntil();

        List<Signing> signings = repository.findByPlayerId(playerId);

        for (Signing s : signings) {
            LocalDate currentSince = s.getSince();
            LocalDate currentUntil = s.getUntil();

            boolean isSinceDateIn = DateUtils.isDateInRange(currentSince, currentUntil, since);
            boolean isUntilDateIn = DateUtils.isDateInRange(currentSince, currentUntil, until);
            boolean isBiggerRange = DateUtils.isRangeDateSubset(currentSince, currentUntil, since, until);

            if (isSinceDateIn) {
                log.error("Player can't sign due to since date conflict with current contract");
                return false;
            }

            if (isUntilDateIn) {
                log.error("Player can't sign due to until date conflict with current contract");
                return false;
            }

            if (isBiggerRange) {
                log.error("Player can't sign due to range date include current contract");
                return false;
            }
        }

        return true;
    }

    /**
     * Check if squad number are taken on any signing for that date range (since, until)
     * {@code true} if squad number hasn't taken by any player, otherwise {@code false}
     */
    private boolean isValidSquadNumberAt(Signing signing) {

        Long teamId = signing.getTeam().getId();
        Integer squadNumber = signing.getSquadNumber();
        LocalDate since = signing.getSince();
        LocalDate until = signing.getUntil();

        List<Signing> signings = repository.findByTeamId(teamId);

        // user can be signed by team, but active signing and isPlayerNotSignedAt -> false
        for (Signing s : signings) {

            LocalDate currentSince = s.getSince();
            LocalDate currentUntil = s.getUntil();

            // if squad number already taken -> false
            if (s.getSquadNumber().equals(squadNumber)) {

                boolean isSinceDateIn = DateUtils.isDateInRange(currentSince, currentUntil, since);
                boolean isUntilDateIn = DateUtils.isDateInRange(currentSince, currentUntil, until);
                boolean isBiggerRange = DateUtils.isRangeDateSubset(currentSince, currentUntil, since, until);

                if (isSinceDateIn || isUntilDateIn || isBiggerRange) {
                    log.warn("Squad number already taken: " + squadNumber);
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Check if player signing date can be modified
     */
    private Boolean isSigningCanBeModified(Signing signing) {

        Long playerId = signing.getPlayer().getId();
        Long signingId  = signing.getId();
        LocalDate since = signing.getSince();
        LocalDate until = signing.getUntil();

        List<Signing> signings = repository.findByPlayerId(playerId);

        for (Signing s : signings) {
            LocalDate currentSince = s.getSince();
            LocalDate currentUntil = s.getUntil();

            // all signings excluding the one we want to modify
            if (!s.getId().equals(signingId)) {

                boolean isSinceDateIn = DateUtils.isDateInRange(currentSince, currentUntil, since);
                boolean isUntilDateIn = DateUtils.isDateInRange(currentSince, currentUntil, until);
                boolean isBiggerRange = DateUtils.isRangeDateSubset(currentSince, currentUntil, since, until);

                if (isSinceDateIn || isUntilDateIn || isBiggerRange) {
                    log.warn("Player can't sign due to contract boundaries");
                    return false;
                }
            }
        }

        return true;
    }
}
