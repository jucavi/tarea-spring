package com.example.tareaspring.models;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import org.springframework.lang.NonNull;

import java.time.LocalDate;

public class TeamCSV {

    @CsvBindByName
    private Long id;
    @CsvBindByName
    private String name;
    @CsvBindByName
    private String email;
    @CsvBindByName
    @CsvDate(value = "yyyy-MM-dd")
    private LocalDate since;
    @CsvBindByName
    private String city;

    public TeamCSV() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getSince() {
        return since;
    }

    public void setSince(LocalDate since) {
        this.since = since;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Returns an instance of Team
     * @return Team instance
     */
    public Team toBeanWithId() {
        return new Team(
                this.id,
                this.name,
                this.email,
                this.since,
                this.city);
    }
}
