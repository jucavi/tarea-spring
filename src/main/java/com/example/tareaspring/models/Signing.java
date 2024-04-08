package com.example.tareaspring.models;

import com.example.tareaspring.utils.validators.anotations.DateRange;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@DateRange(before = "since", after = "until", message = "Until date must be greater then since date")
public class Signing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "player_id", referencedColumnName = "id")
    Player player;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    Team team;

    @Column(nullable = false)
    private LocalDate since;

    @Column(nullable = false)
    private LocalDate until;

    @Column(nullable = false)
    private Integer squadNumber;

    @Override
    public String toString() {
        return "Signing{" +
                "id=" + id +
                ", player_id=" + player.getId() +
                ", team_id=" + team.getId() +
                ", since=" + since +
                ", until=" + until +
                ", squadNumber=" + squadNumber +
                '}';
    }
}
