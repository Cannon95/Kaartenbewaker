package com.cannontm.kaartenbewaker.auth;

import com.cannontm.kaartenbewaker.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final WebClient webClient;

    @Value("${ubi.username}")
    private String email;

    @Value("${ubi.password}")
    private String password;

    @Value("${ubi.user-agent}")
    private String userAgent;


    private final AuthTokenService authTokenService;


    public Mono<Void> ubilogin(){
        if(email == null || password == null || userAgent == null){
            throw new NullPointerException("those fields should not be null!!!");
        }

        String basicAuth = Base64.getEncoder().encodeToString((email + ":" + password).getBytes());

        return webClient.post()
                .uri("https://public-ubiservices.ubi.com/v3/profiles/sessions")
                .header("Content-Type", "application/json")
                .header("Ubi-AppId", "86263886-327a-4328-ac69-527f0d20a237")
                .header("Authorization", "Basic " + basicAuth)
                .header("User-Agent", userAgent)
                .retrieve()
                .bodyToMono(UbiAuthDTO.class)
                .map(resp -> {
                    if (resp.ticket() == null) {
                        throw new RuntimeException("No Ubisoft Ticket!");
                    }

                    AuthToken authToken = AuthToken.builder()
                            .id(0)
                            .ticket(resp.ticket())
                            .build();
                    authTokenService.save(authToken);
                    return resp.ticket();
                }).then();
    }

    public Mono<Void> nadeologin(TokenAudience audience){

                return Mono.fromCallable(() -> authTokenService.getToken().getTicket())
                .flatMap(ticket -> webClient.post()
                        .uri("https://prod.trackmania.core.nadeo.online/v2/authentication/token/ubiservices")
                        .header("Content-Type", "application/json")
                        .header("Authorization", "ubi_v1 t=" + ticket)
                        .header("User-Agent", userAgent)
                        .bodyValue(Map.of("audience", audience.getName()))
                        .retrieve()
                        .bodyToMono(NadeoAuthDTO.class)
                        .doOnNext(response -> log.info("Nadeo token acquired: {}", response.accessToken()))
                        .doOnError(e -> log.error("Failed Nadeo authentication", e))
                ).flatMap(response -> {
                    long expiresAt = JwtUtils.getExp(response.refreshToken());
                    long refreshableAt = JwtUtils.getRat(response.accessToken()) + 1200L;
                    AuthToken authToken;
                    if(audience == TokenAudience.CORE){
                        authToken = AuthToken.builder()
                                .id(0)
                                .accessTokenCore(response.accessToken())
                                .refreshTokenCore(response.refreshToken())
                                .expiresAtCore(expiresAt)
                                .refreshableAtCore(refreshableAt)
                                .build();
                    }
                    else if(audience == TokenAudience.LIVE){
                        authToken = AuthToken.builder()
                                .id(0)
                                .accessTokenLive(response.accessToken())
                                .refreshTokenLive(response.refreshToken())
                                .expiresAtLive(expiresAt)
                                .refreshableAtLive(refreshableAt)
                                .build();
                    }
                    else {
                        throw new IllegalArgumentException("audience must be CORE or LIVE");
                    }

                    authTokenService.save(authToken);
                    return Mono.empty();
                });

    }
    public Mono<Void> refresh(TokenAudience audience, String token){
        return webClient.post()
                .uri("https://prod.trackmania.core.nadeo.online/v2/authentication/token/refresh")
                .header("Authorization", "nadeo_v1 t=" + token)
                .header("User-Agent", userAgent)
                .retrieve()
                .bodyToMono(NadeoAuthDTO.class)
                .doOnNext(response -> log.info("Nadeo token acquired: {}", response.accessToken()))
                .doOnError(e -> log.error("Failed Nadeo authentication", e))
                .flatMap(response -> {
                    long expiresAt = JwtUtils.getExp(response.refreshToken());
                    long refreshableAt = JwtUtils.getRat(response.accessToken()) + 1200L;
                    //AuthToken authToken = new AuthToken(0, response.accessToken(), response.refreshToken(), expiresAt, refreshableAt);
                    AuthToken authToken;
                    if(audience == TokenAudience.CORE){
                        authToken = AuthToken.builder()
                                .id(0)
                                .accessTokenCore(response.accessToken())
                                .refreshTokenCore(response.refreshToken())
                                .expiresAtCore(expiresAt)
                                .refreshableAtCore(refreshableAt)
                                .build();
                    }
                    else if(audience == TokenAudience.LIVE){
                        authToken = AuthToken.builder()
                                .id(0)
                                .accessTokenLive(response.accessToken())
                                .refreshTokenLive(response.refreshToken())
                                .expiresAtLive(expiresAt)
                                .refreshableAtLive(refreshableAt)
                                .build();
                    }
                    else {
                        throw new IllegalArgumentException("audience must be CORE or LIVE");
                    }

                    authTokenService.save(authToken);
                    return Mono.empty();
                });

    }

}
