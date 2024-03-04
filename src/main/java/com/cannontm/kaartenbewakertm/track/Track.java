package com.cannontm.kaartenbewakertm.track;

import com.cannontm.kaartenbewakertm.trackrecords.TrackRecord;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Table(name = "tracks")
public class Track {
    @Id
    String uid;
    private String name;
    private String author;
    private Integer authorTime;
    private Integer goldTime;
    private Integer silverTime;
    private Integer bronzeTime;
    private List<TrackRecord> trackRecords;

}
