package com.example.tareaspring.models;

import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
//@UniqueConstraint(columnNames = {"email", "name"})
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    @Column(unique = true)
    private String email;
    private LocalDate since;
    private String city;

    @OneToMany(mappedBy = "team")
    List<Signing> signingSet;

    public Team() {
    }

    public Team(Long id, String name, String email, LocalDate since, String city, List<Signing> signingSet) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.since = since;
        this.city = city;
        this.signingSet = signingSet;
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

    public List<Signing> getSigningSet() {
        return signingSet;
    }

    public void setSigningSet(List<Signing> signingSet) {
        this.signingSet = signingSet;
    }
}
