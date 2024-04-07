package com.example.tareaspring.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class TeamDto {

    private Long id;

    @NotBlank(message = "Team name can't be blank")
    private String name;

    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+=?`{|}~^.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{1,4}$", message = "Email address must be valid")
    private String email;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate since;

    @NotBlank(message = "City name can't be blank")
    private String city;

}
