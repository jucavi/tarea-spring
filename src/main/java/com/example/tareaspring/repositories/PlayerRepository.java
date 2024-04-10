package com.example.tareaspring.repositories;

import com.example.tareaspring.models.Player;
import com.example.tareaspring.models.Signing;
import com.example.tareaspring.models.Team;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    @Query("SELECT DISTINCT t FROM Player p INNER JOIN p.signings s INNER JOIN s.team t WHERE p.id=:id")
    List<Team> findByPlayerIdTeamsWerePlayerHasPlayed(Long id);

    @Query("SELECT t FROM Player p INNER JOIN p.signings s INNER JOIN s.team t WHERE p.id=:id")
    List<Team> findByPlayerIdAllTeamsWerePlayerHasPlayed(Long id);

//    @Query("SELECT s FROM P :date BETWEEN s.since AND s.until")
//    List<Signing> findPlayerSigningAtDate(Long id, LocalDate date);
}
