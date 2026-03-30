package com.cannontm.kaartenbewaker.auth;

import com.cannontm.kaartenbewaker.dto.JobMessage;
import com.cannontm.kaartenbewaker.dto.JobType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthScheduler {

    private final AuthTokenService authTokenService;

    public Flux<JobMessage> produceJobs(){
        return Flux.interval(Duration.ofSeconds(1), Duration.ofSeconds(60))
                .concatMap(tick -> Mono.fromCallable(authTokenService::getToken)
                        .subscribeOn(Schedulers.boundedElastic())
                        .flatMapMany(token -> {

                            long now = Instant.now().getEpochSecond();
                            List<JobMessage> jobs = new ArrayList<>();

                            if(now >= token.getExpiresAtLive() || now >= token.getExpiresAtCore()){
                                log.info("Token expired → LOGIN");
                                return Flux.just(new JobMessage(JobType.LOGIN, "", 9));
                            }

                            if(now >= token.getRefreshableAtLive()){
                                log.info("Token Needs Refreshing → LIVE");
                                jobs.add(new JobMessage(JobType.REFRESH_LIVE, token.getRefreshTokenLive(), 9));
                            }
                            if(now >= token.getExpiresAtCore()){
                                log.info("Token Needs Refreshing → CORE");
                                jobs.add(new JobMessage(JobType.REFRESH_CORE, token.getRefreshTokenLive(), 9));
                            }

                            log.info("now: {}", now);
                            if (jobs.isEmpty()) {
                                log.info("Token still valid: {}s", token.getRefreshableAtLive() - now);
                                return Flux.empty();
                            }

                            return Flux.fromIterable(jobs);

                        })
                        .switchIfEmpty(Flux.just(new JobMessage(JobType.LOGIN, "", 9))));
    }
}


