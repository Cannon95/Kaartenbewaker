package com.cannontm.kaartenbewaker.outboundhttp;

import com.cannontm.kaartenbewaker.auth.AuthTokenService;
import com.cannontm.kaartenbewaker.player.Player;
import com.cannontm.kaartenbewaker.score.Score;
import com.cannontm.kaartenbewaker.dto.JobMessage;
import com.cannontm.kaartenbewaker.player.PlayerService;
import com.cannontm.kaartenbewaker.track.Track;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiService {

    private final WebClient webClient;
    private final AuthTokenService authTokenService;
    private final PlayerService playerService;

    @Value("${ubi.user-agent}")
    private String userAgent;



    public Mono<Void> getMapLeaderboard(String mapId){
        return get(
            "https://live-services.trackmania.nadeo.live/api/token/leaderboard/group/Personal_Best/map/" + mapId + "/top?length=100&onlyWorld=true&offset=0",
                JsonNode.class
        ).then();
        //schrijf code die map data ophaal
    }

    public  Mono<Void> getScorePosition(String mapId, int score){

        Map<String, Object> mapItem = Map.of(
                "mapUid", mapId,
                "groupUid", "Personal_Best"
        );

        Map<String, Object> body = Map.of(
                "maps", List.of(mapItem)
        );

        return Mono.fromCallable(authTokenService::getToken)
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(token -> webClient
                        .post()
                        .uri("https://live-services.trackmania.nadeo.live/api/token/leaderboard/group/map?scores[" + mapId + "]=" + score)
                        .header("Authorization", "nadeo_v1 t=" + token)
                        .header("User-Agent", userAgent)
                        .bodyValue(body)
                        .retrieve()
                        .bodyToMono(JsonNode.class)
                        .map(jsonNode ->
                                Objects.requireNonNull(StreamSupport.stream(jsonNode.get(0).get("zones").spliterator(), false)
                                        .filter(z -> z.get("zoneName").asText().equals("Utrecht"))
                                        .findFirst()
                                        .map(z -> z.get("ranking").get("position").asInt())
                                        .orElse(null)))
                        .onErrorMap(error -> new IllegalArgumentException("No Integer Found: " + error))
                        .then()
                );

    }

    public <T> Mono<T> get(String uri, Class<T> cls){

        return Mono.fromCallable(authTokenService::getToken)
                        .subscribeOn(Schedulers.boundedElastic())
                        .flatMap(token -> webClient
                                .get()
                                .uri(uri)
                                .header("Authorization", "nadeo_v1 t=" + token)
                                .header("User-Agent", userAgent)
                                .retrieve()
                                .bodyToMono(cls)
                        );

    }

}
