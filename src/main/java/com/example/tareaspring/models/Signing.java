package com.example.tareaspring.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class Signing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "player_id", referencedColumnName = "id")
    Player player;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    Team team;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate since;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate until;

    @NotNull
    @Min(value = 0, message = "Squad number must be greater or equal than 0")
    @Max(value = 99, message = "Squad number must be lesser or equal than 99")
    private Integer squadNumber;

    public Signing() {
    }

    public Signing(Long id, Player player, Team team, LocalDate since, LocalDate until, Integer squadNumber) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Signing{" +
                "id=" + id +
                ", player_id=" + player.getId() +
                ", team_id=" + team.getId() +
                ", since=" + since +
                ", until=" + until +
                ", squadNumber=" + squadNumber +
                '}';
    }
}
