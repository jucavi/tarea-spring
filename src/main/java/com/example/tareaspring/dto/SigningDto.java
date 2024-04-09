package com.example.tareaspring.dto;


import com.example.tareaspring.utils.validators.anotations.CustomDateFormat;
import com.example.tareaspring.utils.validators.anotations.DateRange;
import com.opencsv.bean.CsvBindByName;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@DateRange(before = "since", after = "until", message = "Until date must be greater then since date")
public class SigningDto {

    @CsvBindByName(required = true)
    private Long id;

    @CsvBindByName(required = true)
    @NotNull
    private Long playerId;

    @CsvBindByName(required = true)
    @NotNull
    private Long teamId;

    @CsvBindByName(required = true)
    @NotNull
    @CustomDateFormat(message = "Invalid date, pattern required (YYYY-MM-DD)")
    private String since;

    @CsvBindByName(required = true)
    @NotNull
    @CustomDateFormat(message = "Invalid date, pattern required (YYYY-MM-DD)")
    private String until;

    @NotNull
    @Min(value = 0, message = "Squad number should be greater or equal than 0")
    @Max(value = 99, message = "Squad number should be lesser or equal than 99")
    private Integer squadNumber;
}
