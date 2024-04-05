package com.example.tareaspring.controllers;

import com.example.tareaspring.models.Signing;
import com.example.tareaspring.services.SigningServiceImp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/")
public class SigningController {

    private final SigningServiceImp service;

    public SigningController(SigningServiceImp service) {
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

    /**
     * Create a Signing in the database
     * @param signing signing
     * @return player if created, otherwise {@code 404 } not found
     */
    @PostMapping("/signings")
    public ResponseEntity<Signing> create(@RequestBody Signing signing) {
        Signing result = service.create(signing);

        if (result != null) {
            return ResponseEntity.ok(result);
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Persist all registries from csv file into database
     * @param file file
     * @return List of signings persisted, otherwise an empty list
     */
    @PostMapping("/signings/upload")
    public ResponseEntity<List<Signing>> upload(@RequestParam("file") MultipartFile file) {
        List<Signing> result =  service.parseCSVFileToSignings(file);

        return ResponseEntity.ok(result);
    }
}
