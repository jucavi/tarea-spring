package com.example.tareaspring.repositories;

import com.example.tareaspring.models.Player;
import com.example.tareaspring.models.Signing;
import com.example.tareaspring.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SigningRepository extends JpaRepository<Signing, Long> {

    List<Signing> findByPlayer(Player player);
    List<Signing> findByTeam(Team team);
    List<Signing> findByTeamAndPlayer(Team team, Player player);
    List<Signing> findByUntil(LocalDate date);
    List<Signing> findBySince(LocalDate date);
    List<Signing> findBySquadNumberAndUntil(Integer squadNumber, LocalDate date);
}
