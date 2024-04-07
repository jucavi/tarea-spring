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
public class SigningCSV implements CsvDtoInterface<Signing> {

    @CsvBindByName(required = true)
    private Long id;

    @CsvBindByName(required = true, column = "playerId")
    private Long playerId;

    @CsvBindByName(required = true, column = "teamId")
    private Long teamId;

    @CsvBindByName(required = true)
    @CsvDate(value = "yyyy-MM-dd")
    private LocalDate since;

    @CsvBindByName(required = true)
    @CsvDate(value = "yyyy-MM-dd")
    private LocalDate until;

    @CsvBindByName
    private Integer squadNumber;

    @Override
    public Signing mapToDao() {

        return Signing.builder()
                .id(this.id)
                .player(Player.builder().id(this.teamId).build())
                .team(Team.builder().id(this.teamId).build())
                .since(this.since)
                .until(this.until)
                .squadNumber(this.squadNumber)
                .build();
    }
}
