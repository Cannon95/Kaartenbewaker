package com.cannontm.kaartenbewaker.outboundhttp;

import com.cannontm.kaartenbewaker.dto.JobMessage;
import com.cannontm.kaartenbewaker.dto.JobType;
import com.cannontm.kaartenbewaker.track.TrackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiScheduler {

    private final TrackService trackService;

    public Flux<JobMessage> produceJobs(){
       return Flux.interval(Duration.ofSeconds(15),Duration.ofSeconds(5))
                .concatMap(tick -> Mono.fromCallable(trackService::getOutdatedTracks)
                        .subscribeOn(Schedulers.boundedElastic())
                        .flatMapMany(Flux::fromIterable)
                        .flatMapIterable(track -> List.of(
                                new JobMessage(JobType.PROVINCE_RECORDS, track.getId() + "", 1)
                        )));

    }



}
