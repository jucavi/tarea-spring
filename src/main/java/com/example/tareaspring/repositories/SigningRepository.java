package com.example.tareaspring.repositories;

import com.example.tareaspring.models.Player;
import com.example.tareaspring.models.Signing;
import com.example.tareaspring.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SigningRepository extends JpaRepository<Signing, Long> {

    List<Signing> findByPlayer(Player player);
    List<Signing> findByTeam(Team team);
    List<Signing> findByTeamId(Long teamId);
    List<Signing> findByPlayerId(Long playerId);
}
