package com.example.tareaspring.dto;


import com.example.tareaspring.utils.validators.anotations.CustomDateFormat;
import com.opencsv.bean.CsvBindByName;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamDto {

    @CsvBindByName(required = true)
    private Long id;

    @CsvBindByName(required = true)
    @NotBlank(message = "Team name can't be blank")
    private String name;

    @CsvBindByName(required = true)
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+=?`{|}~^.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{1,4}$", message = "Email address must be valid")
    private String email;

    @CsvBindByName(required = true)
    @NotNull
    @CustomDateFormat(message = "Invalid date, pattern required YYYY-MM-DD")
    private String since;

    @CsvBindByName(required = true)
    @NotBlank(message = "City name can't be blank")
    private String city;
}
