package com.example.tareaspring.dto;

import com.example.tareaspring.models.Team;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamCSV implements CsvDtoInterface<Team> {

    @CsvBindByName(required = true)
    private Long id;

    @CsvBindByName(required = true)
    private String name;

    @CsvBindByName(required = true)
    private String email;

    @CsvBindByName(required = true)
    @CsvDate(value = "yyyy-MM-dd")
    private LocalDate since;
    
    @CsvBindByName(required = true)
    private String city;

    @Override
    public Team mapToDao() {
        return Team.builder()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .since(this.since)
                .city(this.city)
                .build();
    }
}
