package com.example.tareaspring.dto;

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
public class PlayerCSV {

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
    private Double weight;
    @CsvBindByName(required = true)
    private Double high;
    @CsvBindByName(required = true)
    private Double imc;
    @CsvBindByName(required = true)
    private Double fat;

    /**
     * Returns an instance of Team
     * @return Team instance
     */
    public Player toBeanWithId() {
        // TODO: Check for nulls if file not match with Object properties
        return new Player(
                this.id,
                this.firstname,
                this.lastname,
                this.email,
                this.birthdate,
                this.position,
                this.gender,
                this.weight,
                this.high,
                this.fat
        );
    }
}

