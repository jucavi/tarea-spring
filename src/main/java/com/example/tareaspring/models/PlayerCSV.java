package com.example.tareaspring.models;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import org.springframework.lang.NonNull;

import java.time.LocalDate;

public class PlayerCSV {

    @CsvBindByName
    private Long id;
    @CsvBindByName
    private String firstname;
    @CsvBindByName
    private String lastname;
    @CsvBindByName
    private String email;
    @CsvBindByName
    @CsvDate(value = "yyyy-MM-dd")
    private LocalDate birthdate;
    @CsvBindByName
    private String position;
    @CsvBindByName
    private String gender;
    @CsvBindByName
    private Double weight;
    @CsvBindByName
    private Double high;
    @CsvBindByName
    private Double imc;
    @CsvBindByName
    private Double fat;

    public PlayerCSV() {
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
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

    @Override
    public String toString() {
        return "PlayerCSV{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", birthdate=" + birthdate +
                ", position='" + position + '\'' +
                ", gender='" + gender + '\'' +
                ", weight=" + weight +
                ", high=" + high +
                ", imc=" + imc +
                ", fat=" + fat +
                '}';
    }

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

