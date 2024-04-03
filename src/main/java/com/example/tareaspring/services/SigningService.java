package com.example.tareaspring.services;

import com.example.tareaspring.models.Player;
import com.example.tareaspring.models.Signing;
import com.example.tareaspring.repositories.SigningRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.Mapping;

import java.util.List;
import java.util.Optional;

@Service
public class SigningService {

    private final SigningRepository repository;
    private final Logger log = LoggerFactory.getLogger(SigningService.class);

    public SigningService(SigningRepository repository) {
        this.repository = repository;
    }

    public List<Signing> findAll() {
        log.info("Retrieving all Signings from database");
        return repository.findAll();
    }

    public Signing findById(Long id) {
        Optional<Signing> signingOpt = repository.findById(id);

        if (signingOpt.isPresent()) {
            log.info("Retrieving Signing with ID: {}", id);
            return signingOpt.get();
        }
        log.warn("Trying to retrieve Signing with wrong ID: {}", id);
        return null;
    }

    public Signing create(Signing signing) {

        if (signing.getId() != null) {
            log.warn("Trying to create a Signing with assigned ID: {}", signing.getId());
            return null;
        }

        List<Signing> signingsOpt = repository.findByTeamAndPlayer(signing.getTeam(), signing.getPlayer());



        // guardar el ???? recibido por parámetro en la base de datos
        Signing result = repository.save(signing);
        log.info("Player created with ID: {}", signing.getId());

        return result;
    }

    public Boolean delete(Long id) {

        if (repository.existsById(id)) {
            repository.deleteById(id);
            log.info("Signing deleted with ID: {}", id);
            return true;
        }

        log.warn("Trying to delete a Signing with wrong ID");
        return false;
    }
}
