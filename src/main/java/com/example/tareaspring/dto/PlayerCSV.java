package com.example.tareaspring.dto;

import com.example.tareaspring.models.FieldPosition;
import com.example.tareaspring.models.Gender;
import com.example.tareaspring.models.Player;
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
public class PlayerCSV implements CsvDtoInterface<Player> {

    @CsvBindByName(required = true)
    private Long id;

    @CsvBindByName(required = true)
    private String firstname;

    @CsvBindByName(required = true)
    private String lastname;

    @CsvBindByName(required = true)
    private String email;

    @CsvBindByName(required = true)
    @CsvDate(value = "yyyy-MM-dd")
    private LocalDate birthdate;

    @CsvBindByName(required = true)
    private String position;

    @CsvBindByName(required = true)
    private String gender;

    @CsvBindByName(required = true)
    private Integer weight;

    @CsvBindByName(required = true)
    private Integer high;

    @CsvBindByName(required = true)
    private Double imc;

    @CsvBindByName(required = true)
    private Integer fat;

    @Override
    public Player mapToDao() {
        return Player.builder()
                .id(this.id)
                .firstname(this.firstname)
                .lastname(this.lastname)
                .email(this.email)
                .birthdate(this.birthdate)
                .position(FieldPosition.valueOf(this.position))
                .gender(Gender.valueOf(this.gender))
                .imc(this.imc)
                .weight(this.weight)
                .high(this.high)
                .fat(this.fat)
                .build();
    }
}

