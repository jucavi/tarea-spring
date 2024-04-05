package com.example.tareaspring.services;

import com.example.tareaspring.models.Signing;
import com.example.tareaspring.models.Signing;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SigningService {

    List<Signing> parseCSVFileToSignings(MultipartFile file);
    List<Signing> findAll();
    Signing findById(Long id);
    Signing create(Signing Signing);
    Signing update(Signing Signing);
    public Boolean deleteById(Long id);
}
