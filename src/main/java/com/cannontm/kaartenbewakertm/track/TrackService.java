package com.cannontm.kaartenbewakertm.track;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TrackService {

    TrackRepository trackRepository;

    public void saveTrack(Mono<Track> track){
        track.flatMap(track1 -> trackRepository.save(track1)).subscribe();
    }

    public Mono<Track> getTrackfromUid(String uid){
        return trackRepository.findById(uid).flatMap(track -> {
            if(track == null)return Mono.empty();
            else return Mono.just(track);
        });


    }



}
