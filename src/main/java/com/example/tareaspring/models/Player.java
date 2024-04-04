package com.example.tareaspring.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.lang.NonNull;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
//@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {"email" }) })
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstname;
    @Column(nullable = false)
    //@NotEmpty(message = "Lastname cannot be empty")
    private String lastname;
    @Column(unique = true, nullable = false)
    //@Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;
    @Column(nullable = false)
    private LocalDate birthdate;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FieldPosition position;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(nullable = false)
    private Double weight;
    @Column(nullable = false)
    private Double high;
    @Column(nullable = false)
    private Double imc;
    @Column(nullable = false)
    private Double fat;

    @JsonIgnore
    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    Set<Signing> signingSet = new HashSet<>();

    public Player() {
    }

    public Player(
            Long id,
            @NonNull String firstname,
            @NonNull String lastname,
            @NonNull String email,
            @NonNull LocalDate birthdate,
            @NonNull FieldPosition position,
            @NonNull Gender gender,
            @NonNull Double weight,
            @NonNull Double high,
            @NonNull Double fat) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.birthdate = birthdate;
        this.position = position;
        this.gender = gender;
        this.weight = weight;
        this.high = high;
        this.imc = weight / (high * high);
        // No se si fat es calculable a partir del imc
        this.fat = fat;
    }

    public Player(
            Long id,
            @NonNull String firstname,
            @NonNull String lastname,
            @NonNull String email,
            @NonNull LocalDate birthdate,
            @NonNull String position,
            @NonNull String gender,
            @NonNull Double weight,
            @NonNull Double high,
            @NonNull Double fat) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.birthdate = birthdate;
        // raise IllegalArgumentException
        this.position = FieldPosition.valueOf(position);
        // raise IllegalArgumentException
        this.gender = Gender.valueOf(gender);
        this.weight = weight;
        this.high = high;
        // No se si fat es calculable desde imc
        this.fat = fat;
        calculateImc();
    }

    public Player(
            Long id,
            @NonNull String firstname,
            @NonNull String lastname,
            @NonNull String email,
            @NonNull LocalDate birthdate,
            @NonNull FieldPosition position,
            @NonNull Gender gender,
            @NonNull Double weight,
            @NonNull Double high,
            @NonNull Double imc,
            @NonNull Double fat) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public FieldPosition getPosition() {
        return position;
    }

    public void setPosition(FieldPosition position) {
        this.position = position;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {

        this.weight = weight;
        // ante un cambio del peso se recalcula el imc
        calculateImc();
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {

        this.high = high;
        // ante un cambio del peso se recalcula el imc
        calculateImc();
    }

    public Double getImc() {
        return imc;
    }

    public void setImc(Double imc) {
        this.imc = imc;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public Set<Signing> getSigningSet() {
        return signingSet;
    }

    public void setSigningSet(Set<Signing> signingSet) {
        this.signingSet = signingSet;
    }

    /**
     * Calculate the imc from weight and height
     */
    private void calculateImc() {
        if (this.weight != null && this.high != null && this.weight != 0) {
            this.imc = this.weight / (this.high * this.high);
        } else {
            this.imc = 0.0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player player = (Player) o;
        return Objects.equals(getFirstname(), player.getFirstname())
                && Objects.equals(getLastname(), player.getLastname())
                && Objects.equals(getEmail(), player.getEmail())
                && Objects.equals(getBirthdate(), player.getBirthdate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstname(), getLastname(), getEmail(), getBirthdate());
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", birthdate=" + birthdate +
                ", position='" + position + '\'' +
                ", gender=" + gender +
                ", weight=" + weight +
                ", high=" + high +
                ", imc=" + imc +
                ", fat=" + fat +
                '}';
    }
}
