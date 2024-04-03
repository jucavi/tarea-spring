package com.example.tareaspring.models;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;
    private String lastname;

    @Column(unique = true)
    private String email;
    private LocalDate birthdate;
    @Enumerated(EnumType.STRING)
    private FieldPosition position;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private Double weight;
    private Double high;
    private Double imc;
    private Double fat;

    @OneToMany(mappedBy = "player")
    List<Signing> signingSet;

    public Player() {
    }

    public Player(
            Long id,
            String firstname,
            String lastname,
            String email,
            LocalDate birthdate,
            FieldPosition position,
            Gender gender,
            Double weight,
            Double high,
            Double imc,
            Double fat,
            List<Signing> signingSet) {
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
        this.signingSet = signingSet;
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
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
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

    public List<Signing> getSigningSet() {
        return signingSet;
    }

    public void setSigningSet(List<Signing> signingSet) {
        this.signingSet = signingSet;
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
