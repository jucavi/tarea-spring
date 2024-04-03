package com.example.tareaspring.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
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

    @JsonIgnore
    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    Set<Signing> signingSet = new HashSet<>();

    public Team() {
    }

    public Team(
            Long id,
            @NonNull String name,
            @NonNull String email,
            @NonNull LocalDate since,
            @NonNull String city) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.since = since;
        this.city = city;
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

    public Set<Signing> getSigningSet() {
        return signingSet;
    }

    public void setSigningSet(Set<Signing> signingSet) {
        this.signingSet = signingSet;
    }
}
