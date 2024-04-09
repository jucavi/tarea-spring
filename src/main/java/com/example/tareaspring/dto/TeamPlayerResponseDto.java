package com.example.tareaspring.dto;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamPlayerResponseDto {

    private PlayerDto player;
    private LocalDate since;
    private LocalDate until;
    private Integer squadNumber;
}
