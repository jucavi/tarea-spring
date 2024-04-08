package com.example.tareaspring.dto;

import com.example.tareaspring.models.Player;
import com.example.tareaspring.models.Team;
import com.example.tareaspring.utils.validators.anotations.DateRange;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@DateRange(before = "since", after = "until", message = "Until date must be greater then since date")
public class SigningDto {

    private Long id;

    @NotNull
    private Player player;

    @NotNull
    private Team team;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate since;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate until;

    @Min(value = 0, message = "Squad number should be greater or equal than 0")
    @Max(value = 99, message = "Squad number should be lesser or equal than 99")
    private Integer squadNumber;
}
