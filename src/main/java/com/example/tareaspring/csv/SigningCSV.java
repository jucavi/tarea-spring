package com.example.tareaspring.csvmodels;

import com.example.tareaspring.models.Player;
import com.example.tareaspring.models.Signing;
import com.example.tareaspring.models.Team;
import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class SigningCSV {

    @CsvBindByName(required = true)
    private Long id;
    @CsvBindByName(required = true, column = "playerId")
    Player player;
    @CsvBindByName(required = true, column = "teamId")
    Team team;
    @CsvBindByName(required = true)
    private LocalDate since;
    @CsvBindByName(required = true)
    private LocalDate until;
    @CsvBindByName
    private Integer squadNumber;

    public Signing toBeanWithId() {

        Player player = new Player();
        Team team = new Team();

        player.setId(this.player.getId());
        team.setId(this.team.getId());

        return new Signing(
             this.id,
             player,
             team,
             this.since,
             this.until,
             this.squadNumber
        );
    }
}
