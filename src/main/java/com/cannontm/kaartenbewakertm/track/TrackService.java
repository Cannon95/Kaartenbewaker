package com.cannontm.kaartenbewakertm.track;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TrackService {

    TrackRepository trackRepository;

    public void saveTrack(Track track){
        if(track != null && !track.uid.equals("")){
            trackRepository.save(track);
        }
    }

    public Mono<Track> getTrackfromUid(String uid){
        Mono<Track> track = trackRepository.findById(uid);


    }



}
