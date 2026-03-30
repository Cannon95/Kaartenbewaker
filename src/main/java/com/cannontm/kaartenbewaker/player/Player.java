package com.cannontm.kaartenbewaker.player;

import com.cannontm.kaartenbewaker.score.Score;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;


import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;


@Table(name = "player")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Player {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;
    private String name;

    @Column(name = "last_updated")
    private OffsetDateTime lastUpdated = OffsetDateTime.now();

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Score> scores;
}

