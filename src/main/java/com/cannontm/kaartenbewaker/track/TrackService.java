package com.cannontm.kaartenbewaker.track;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrackService {

    private final TrackRepository trackRepository;

    public List<Track> getOutdatedTracks(){
        return trackRepository.findOutdatedTracks();
    }

}
