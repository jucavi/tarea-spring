package com.example.tareaspring.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
public class SigningKey implements Serializable {

    @Column(name = "player_id")
    Long playerId;

    @Column(name = "team_id")
    Long teamId;

    /*
    @Column(name = "since")
    LocalDate since;
    */

    public SigningKey() {
    }

    public SigningKey(Long playerId, Long teamId) {
        this.playerId = playerId;
        this.teamId = teamId;
    }

    /*
    public SigningKey(Long playerId, Long teamId, LocalDate since) {
        this.playerId = playerId;
        this.teamId = teamId;
        this.since = since;
    }
    */

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }
}
