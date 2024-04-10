package com.example.tareaspring.services;

import com.example.tareaspring.dto.SigningDto;
import com.example.tareaspring.models.Signing;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface SigningService {

    List<SigningDto> parseCSVFileToSignings(MultipartFile file);
    List<SigningDto> findAll();
    SigningDto findById(Long id);
    SigningDto create(Signing Signing);
    SigningDto update(Signing Signing);
    void deleteById(Long id);
}
