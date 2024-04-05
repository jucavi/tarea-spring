package com.example.tareaspring.models;


import com.example.tareaspring.utils.validators.date.CustomDateConstraint;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
//@UniqueConstraint(columnNames = {"email", "name"})
// TODO: Maybe unique constrain (name, city, since)
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Team name can't be empty")
    @Column(unique = true, nullable = false)
    private String name;

    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+=?`{|}~^.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{1,4}$", message = "Invalid email")
    @Column(unique = true, nullable = false)
    private String email;

    @NotNull
    @CustomDateConstraint(message = "Date in format YYYY-MM-DD")
    @Column(nullable = false)
    private LocalDate since;

    @NotBlank(message = "City name can't be empty")
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
