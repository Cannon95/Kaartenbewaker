package com.cannontm.kaartenbewaker.score;

import com.cannontm.kaartenbewaker.track.Track;
import com.cannontm.kaartenbewaker.player.Player;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import lombok.*;
import org.springframework.data.annotation.Id;


import java.time.Instant;


@Table(name = "score")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Score {
    @Id
    private Long id;

    private Integer score;
    @Column(name = "time_stamp")
    private Long timestamp;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "track_id", nullable = false)
    private Track track;
}
