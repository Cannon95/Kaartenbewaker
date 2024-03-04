package com.cannontm.kaartenbewakertm.trackrecords;

import com.cannontm.kaartenbewakertm.player.Player;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.sound.midi.Track;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "track_records")
public class TrackRecord {
    @Id
    private String recordid;
    private Player player;
    private Track track;
    private String region;
    private Integer pos;
    private Integer score;
}
