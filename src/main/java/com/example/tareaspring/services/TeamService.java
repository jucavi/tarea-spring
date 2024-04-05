package com.example.tareaspring.services;

import com.example.tareaspring.models.Team;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TeamService {
    List<Team> parseCSVFileToTeams(MultipartFile file);
    List<Team> findAll();
    Team findById(Long id);
    Team create(Team Team);
    Team update(Team Team);
    public Boolean deleteById(Long id);
}
