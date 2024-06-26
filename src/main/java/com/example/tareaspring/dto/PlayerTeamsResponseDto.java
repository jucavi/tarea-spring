package com.example.tareaspring.dto;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerTeamsResponseDto {

    private TeamDto team;
    private LocalDate since;
    private LocalDate until;
    private Integer squadNumber;
}
