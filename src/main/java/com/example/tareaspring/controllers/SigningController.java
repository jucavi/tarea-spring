package com.example.tareaspring.controllers;

import com.example.tareaspring.dto.SigningDto;
import com.example.tareaspring.dto.converter.SigningMapper;
import com.example.tareaspring.errors.SigningNotFoundException;
import com.example.tareaspring.services.SigningServiceImp;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signings")
@Tag(name = "Contracts")
public class SigningController {

    private final SigningServiceImp service;
    private final SigningMapper signingMapper;


    /**
     * Get all signings from database
     * @return all signing from the database
     */
    @GetMapping
    public ResponseEntity<List<SigningDto>> findAll() {
        return ResponseEntity.ok(
                service.findAll()
                        .stream()
                        .map(signingMapper::mapDaoToDto)
                        .collect(Collectors.toList()));
    }

    /**
     * Find a signing from database
     * @param id signing identifier
     * @return signing record if exists, otherwise {@code 404 } not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<SigningDto> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(signingMapper::mapDaoToDto)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new SigningNotFoundException(id));
    }

    /**
     * Create a Signing in the database
     */
    @PostMapping
    // Validate Signing (since < until) in Entity
    public ResponseEntity<SigningDto> create(@RequestBody @Valid SigningDto signingDto) {
        return ResponseEntity.ok(
                signingMapper.mapDaoToDto(
                        service.create(
                                signingMapper.mapDtoToDao(signingDto))));
    }

    /**
     * Update signing info
     */
    @PutMapping
    // Validate Signing (since < until) in Entity
    public ResponseEntity<SigningDto> update(@RequestBody @Valid  SigningDto signingDto) {
        return ResponseEntity.ok(
                signingMapper.mapDaoToDto(
                        service.update(
                                signingMapper.mapDtoToDao(signingDto))));
    }

    /**
     * Delete a signing from a database
     * @param id signing identifier
     * @return {@code true} if a signing was deleted, {@code 404 } not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Persist all registries from csv file into database
     * @param file file
     * @return List of signings persisted, otherwise an empty list
     */
    @PostMapping("/upload")
    public ResponseEntity<List<SigningDto>> upload(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(service.parseCSVFileToSignings(file));
    }
}
