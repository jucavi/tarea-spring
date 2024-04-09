package com.example.tareaspring.controllers;

import com.example.tareaspring.errors.SigningNotFoundException;
import com.example.tareaspring.models.Signing;
import com.example.tareaspring.services.SigningServiceImp;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signings")
public class SigningController {

    private final SigningServiceImp service;
    private final ModelMapper modelMapper;


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
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new SigningNotFoundException(id));
    }

    /**
     * Create a Signing in the database
     */
    @PostMapping
    public ResponseEntity<Signing> create(@RequestBody @Valid Signing signing) {
        return ResponseEntity.ok(
                service.create(signing)
        );
    }

    /**
     * Update signing info
     * @param signing signing
     * @return signing if updated, otherwise {@code 404 } not found
     */
    @PutMapping
    public ResponseEntity<Signing> update(@RequestBody @Valid  Signing signing) {
        return ResponseEntity.ok(
                service.update(signing)
        );
    }

    /**
     * Delete a signing from a database
     * @param id signing identifier
     * @return {@code true} if a signing was deleted, {@code 404 } not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
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
