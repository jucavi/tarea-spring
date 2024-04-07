package com.example.tareaspring.controllers;

import com.example.tareaspring.models.Signing;
import com.example.tareaspring.services.SigningServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/signings")
public class SigningController {

    private final SigningServiceImp service;


    /**
     * Get all signings from database
     * @return all signing from the database
     */
    @GetMapping
    public ResponseEntity<List<Signing>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    /**
     * Find a signing from database
     * @param id signing identifier
     * @return signing record if exists, otherwise {@code 404 } not found
     */
    @GetMapping("/{id}")
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
    @PostMapping
    public ResponseEntity<Signing> create(@RequestBody @Valid Signing signing) {
        Signing result = service.create(signing);

        if (result != null) {
            return ResponseEntity.ok(result);
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Update signing info
     * @param signing signing
     * @return signing if updated, otherwise {@code 404 } not found
     */
    @PutMapping
    public ResponseEntity<Signing> update(@RequestBody @Valid  Signing signing) {
        throw new RuntimeException("not implemented yet");
    }

    /**
     * Delete a signing from a database
     * @param id signing identifier
     * @return {@code true} if a signing was deleted, {@code 404 } not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        Boolean result = service.deleteById(id);

        if (result) {
            return ResponseEntity.ok(true);
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Persist all registries from csv file into database
     * @param file file
     * @return List of signings persisted, otherwise an empty list
     */
    @PostMapping("/upload")
    public ResponseEntity<List<Signing>> upload(@RequestParam("file") MultipartFile file) {
        List<Signing> result =  service.parseCSVFileToSignings(file);

        return ResponseEntity.ok(result);
    }
}
