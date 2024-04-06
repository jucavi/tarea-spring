package com.example.tareaspring.models;

import com.example.tareaspring.utils.validators.DateRange;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "player_id", referencedColumnName = "id")
    Player player;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    Team team;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate since;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate until;

    //@NotNull // for csv
    @Min(value = 0, message = "Squad number should be greater or equal than 0")
    @Max(value = 99, message = "Squad number should be lesser or equal than 99")
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
