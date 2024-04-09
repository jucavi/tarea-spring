package com.example.tareaspring.dto;

import com.example.tareaspring.dto.converter.PlayerMapper;
import com.example.tareaspring.models.FieldPosition;
import com.example.tareaspring.models.Gender;
import com.example.tareaspring.utils.validators.anotations.CustomDateFormat;
import com.example.tareaspring.utils.validators.anotations.ValueOfEnum;
import com.opencsv.bean.CsvBindByName;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PlayerDto {

    @CsvBindByName(required = true)
    private Long id;

    @CsvBindByName(required = true)
    @NotBlank(message = "Player firstname can't be empty")
    private String firstname;

    @CsvBindByName(required = true)
    @NotBlank(message = "Player lastname can't be empty")
    private String lastname;

    @CsvBindByName(required = true)
    //    @Email(message = "Email address must be valid") // not check for blank message
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+=?`{|}~^.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{1,4}$", message = "Email address must be valid")
    private String email;

    @CsvBindByName(required = true)
    @NotNull
    @CustomDateFormat(message = "Invalid date, pattern required YYYY-MM-DD")
    private String birthdate;

    @CsvBindByName(required = true)
    @Enumerated(EnumType.STRING)
    @ValueOfEnum(enumClass = FieldPosition.class, message = "Position not allowed")
    private String position;

    @CsvBindByName(required = true)
    @Enumerated(EnumType.STRING)
    @ValueOfEnum(enumClass = Gender.class, message = "Gender not allowed")
    private String gender;

    @CsvBindByName(required = true)
    @NotNull
    @Min(value = 0, message = "Weight should be greater than 0 Kilograms")
    @Max(value = 300, message = "Weight should be lesser than 300 Kilograms")
    private Integer weight;

    @CsvBindByName(required = true)
    @NotNull
    @Min(value = 0, message = "High should be greater than 0 centimeters")
    @Max(value = 300, message = "High should be lesser than 300 centimeters")
    private Integer high;

    @CsvBindByName(required = true)
    private Double imc;

    @CsvBindByName(required = true)
    @NotNull
    @Min(value = 3, message = "Fat should be greater than 3 percent")
    @Max(value = 40, message = "Fat should be lesser than 40 percent")
    private Integer fat;
}
