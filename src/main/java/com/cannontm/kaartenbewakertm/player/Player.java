package com.cannontm.kaartenbewakertm.player;

import com.cannontm.kaartenbewakertm.trackrecords.TrackRecord;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "players")
public class Player {
    @Id
    private String uid;
    private String name;
    private List<TrackRecord> trackRecords;

}
