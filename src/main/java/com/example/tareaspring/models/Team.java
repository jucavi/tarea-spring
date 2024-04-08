package com.example.tareaspring.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

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
@Table(uniqueConstraints={
        @UniqueConstraint( name = "idx_name_city",  columnNames ={"name","city"})
})
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
    List<Signing> signings = new ArrayList<>();

    public Team(Long id, String name, String email, LocalDate since, String city) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.since = since;
        this.city = city;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", since=" + since +
                ", city='" + city + '\'' +
                '}';
    }
}
