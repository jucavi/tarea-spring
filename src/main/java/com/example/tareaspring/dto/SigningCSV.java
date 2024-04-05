package com.example.tareaspring.dto;

import com.example.tareaspring.models.Player;
import com.example.tareaspring.models.Signing;
import com.example.tareaspring.models.Team;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class SigningCSV {

    @CsvBindByName(required = true)
    private Long id;
    @CsvBindByName(required = true, column = "playerId")
    Long playerId;
    @CsvBindByName(required = true, column = "teamId")
    Long teamId;
    @CsvBindByName(required = true)
    @CsvDate(value = "yyyy-MM-dd")
    private LocalDate since;
    @CsvBindByName(required = true)
    @CsvDate(value = "yyyy-MM-dd")
    private LocalDate until;
    @CsvBindByName
    private Integer squadNumber;

    public Signing toBeanWithId() {

        Player player = new Player();
        Team team = new Team();

        player.setId(this.playerId);
        team.setId(this.teamId);

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
