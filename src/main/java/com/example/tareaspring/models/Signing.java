package com.example.tareaspring.models;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Signing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id", referencedColumnName = "id")
    Player player;

    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    Team team;
    private LocalDate since;
    private LocalDate until;
    private Integer squadNumber;

    public Signing() {
    }

    public Signing(Player player, Team team, LocalDate since, LocalDate until, Integer squadNumber) {
        this.since = since;
        this.player = player;
        this.team = team;
        this.until = until;
        this.squadNumber = squadNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public LocalDate getSince() {
        return since;
    }

    public void setSince(LocalDate since) {
        this.since = since;
    }

    public LocalDate getUntil() {
        return until;
    }

    public void setUntil(LocalDate until) {
        this.until = until;
    }

    public Integer getSquadNumber() {
        return squadNumber;
    }

    public void setSquadNumber(Integer squadNumber) {
        this.squadNumber = squadNumber;
    }
}
