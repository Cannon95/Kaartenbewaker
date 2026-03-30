package com.cannontm.kaartenbewaker.track;

import com.cannontm.kaartenbewaker.score.Score;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;


import java.time.OffsetDateTime;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "track")
public class Track {
    @Id
    private Long id;

    private String name;

    @Column(name = "last_updated")
    private OffsetDateTime lastUpdated = OffsetDateTime.now();

    @Column(name = "frequency_mins")
    private Integer frequencyMins = 30;

    @OneToMany(mappedBy = "track", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private Set<Score> scores;
}
