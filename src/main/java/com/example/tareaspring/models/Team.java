package com.example.tareaspring.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@Data
@NoArgsConstructor
//@UniqueConstraint(columnNames = {"email", "name"})
// TODO: Maybe unique constrain (name, city, since)
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private LocalDate since;
    @Column(nullable = false)
    private String city;

    @JsonIgnore
    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    Set<Signing> signingSet = new HashSet<>();


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

}
