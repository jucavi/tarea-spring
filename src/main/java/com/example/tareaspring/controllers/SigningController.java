package com.example.tareaspring.controllers;

import com.example.tareaspring.models.Player;
import com.example.tareaspring.models.Signing;
import com.example.tareaspring.services.SigningService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/")
public class SigningController {

    private final SigningService service;

    public SigningController(SigningService service) {
        this.service = service;
    }

    /**
     * Get all signings from database
     * @return all signing from the database
     */
    @GetMapping("/signings")
    public ResponseEntity<List<Signing>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    /**
     * Find a signing from database
     * @param id signing identifier
     * @return signing record if exists, otherwise {@code 404 } not found
     */
    @GetMapping("/signings/{id}")
    public ResponseEntity<Signing> findById(@PathVariable Long id) {
        Signing result = service.findById(id);

        if (result != null) {
            return ResponseEntity.ok(result);
        }

        return ResponseEntity.notFound().build();
    }
}
