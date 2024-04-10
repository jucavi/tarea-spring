package com.example.tareaspring.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.lang.NonNull;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Column(nullable = false)
    private FieldPosition position;

    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private Integer weight;

    @Column(nullable = false)
    private Integer high;

    @Column(nullable = false)
    private Double imc;

    @Column(nullable = false)
    private Integer fat;

    @JsonIgnore
    @OneToMany(mappedBy = "player", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    List<Signing> signings;


    public Player(
            Long id,
            @NonNull String firstname,
            @NonNull String lastname,
            @NonNull String email,
            @NonNull LocalDate birthdate,
            @NonNull FieldPosition position,
            @NonNull Gender gender,
            @NonNull Integer weight,
            @NonNull Integer high,
            @NonNull Integer fat) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.birthdate = birthdate;
        this.position = position;
        this.gender = gender;
        this.weight = weight;
        this.high = high;
        this.imc = weight / (high * high * 1.0);
        this.fat = fat;
    }

    public Player(
            Long id,
            @NonNull String firstname,
            @NonNull String lastname,
            @NonNull String email,
            @NonNull LocalDate birthdate,
            @NonNull FieldPosition position,
            @NonNull Gender gender,
            @NonNull Integer weight,
            @NonNull Integer high,
            @NonNull Double imc,
            @NonNull Integer fat) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.birthdate = birthdate;
        this.position = position;
        this.gender = gender;
        this.weight = weight;
        this.high = high;
        this.imc = imc;
        this.fat = fat;
    }

    /**
     * Set weight and recalculate imc
     * @param weight weight
     */
    public void setWeight(Integer weight) {

        this.weight = weight;
        calculateImc();
    }

    /**
     * Set high and recalculate imc
     * @param high high
     */
    public void setHigh(Integer high) {

        this.high = high;
        calculateImc();
    }

    /**
     * Calculate the imc based on weight and height
     */
    private void calculateImc() {
        try {
            this.imc = this.weight / (this.high * this.high * 10.0);
        } catch (Exception ex){
            this.imc = 0.0;
        }
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", birthdate=" + birthdate +
                ", position=" + position +
                ", gender=" + gender +
                ", weight=" + weight +
                ", high=" + high +
                ", imc=" + imc +
                ", fat=" + fat +
                '}';
    }
}
