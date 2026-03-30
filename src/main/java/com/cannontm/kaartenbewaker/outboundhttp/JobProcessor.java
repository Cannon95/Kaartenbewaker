package com.cannontm.kaartenbewaker.outboundhttp;

import com.cannontm.kaartenbewaker.auth.AuthScheduler;
import com.cannontm.kaartenbewaker.auth.AuthService;
import com.cannontm.kaartenbewaker.auth.TokenAudience;
import com.cannontm.kaartenbewaker.dto.DiscordMessageDTO;
import com.cannontm.kaartenbewaker.dto.JobMessage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class JobProcessor {
    private final ApiScheduler apiScheduler;
    private final AuthScheduler authScheduler;
    private final AuthService authService;
    private final ApiService apiService;

    @PostConstruct
    public void processJobs(){
        Flux.merge(apiScheduler.produceJobs(), authScheduler.produceJobs())
                .concatMap(message -> consumeJobs(message)
                        .delayElements(Duration.ofMillis(2000)))
                .subscribe();


    }


    Flux<DiscordMessageDTO> consumeJobs(JobMessage message) {
        switch (message.getType()) {
            case LOGIN -> authService.ubilogin();
            case REFRESH_CORE -> authService.refresh(TokenAudience.CORE ,message.getPayload());
            case REFRESH_LIVE -> authService.refresh(TokenAudience.LIVE ,message.getPayload());
            case MAP_RECORDS -> apiService.getMapLeaderboard(message.getPayload());
            //case CLUB_RECORDS -> apiService.get
        }
        return null;
    }
}
