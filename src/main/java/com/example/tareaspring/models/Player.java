package com.example.tareaspring.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


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

    @NotBlank(message = "Player firstname can't be empty")
    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    @NotBlank(message = "Player lastname can't be empty")
    private String lastname;

    @Column(unique = true, nullable = false)
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+=?`{|}~^.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{1,4}$", message = "Invalid email")
    private String email;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate birthdate;

    //@NotNull(message = "Position can't be null")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FieldPosition position;

    //@NotNull(message = "Gender can't be null")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull
    @Min(value = 0, message = "Weight should be greater than 0 Kilograms")
    @Max(value = 300, message = "Weight should be lesser than 300 Kilograms")
    @Column(nullable = false)
    private Integer weight;

    @NotNull
    @Min(value = 0, message = "High should be greater than 0 centimeters")
    @Max(value = 300, message = "High should be lesser than 300 centimeters")
    @Column(nullable = false)
    private Integer high;

    @Setter
    @NotNull
    @Column(nullable = false)
    private Double imc;

    @Setter
    @NotNull
    @Min(value = 3, message = "Fat should be greater than 3 percent")
    @Max(value = 40, message = "Fat should be lesser than 40 percent")
    @Column(nullable = false)
    private Integer fat;

    @Setter
    @JsonIgnore
    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    Set<Signing> signingSet = new HashSet<>();


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
