package com.cannontm.kaartenbewakertm.apiservice;

import com.cannontm.kaartenbewakertm.track.Track;
import com.cannontm.kaartenbewakertm.trackrecords.TrackRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ApiCallService {

    private List<String> queue = new ArrayList<>();
    @Value("${UBI_LOGIN}")
    private String ubiUser;
    @Value("${UBI_PW}")
    private String ubiPassword;
    private String ubiTicket;
    private String accesToken;
    private String refreshToken;

    @Scheduled(fixedRate = 5000)
    public void RunApiCalls(){
        if(queue.isEmpty())
        {

        }
        else if(queue.get(0).equals("ubi"))
        {
            UbiLogin ul = getUbiLogin();
            ubiTicket = ul.ticket();
        }
        else if(queue.get(0).equals("nadeo"))
        {
            NadeoLogin nl = getNadeoLogin();
            accesToken = nl.accessToken();
            refreshToken = nl.refreshToken();

        }
        else if(queue.get(0).startsWith("map"))
        {
            String[] details = queue.get(0).split(":");

        }
        else if(queue.get(0).equals("usernames"))
        {

        }


    }


    public void testing(){
        URI url = URI.create("https://jsonplaceholder.typicode.com/todos/1");
        try {
             WebClient.create().get()
                    .uri(url)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, this::handleErrors)
                    .onStatus(HttpStatusCode::is5xxServerError, this::handleServerErrors)
                    .bodyToMono(new ParameterizedTypeReference<Map<String,Object>>() {})
                    .flatMap(l -> {
                        System.out.println(l.get("title"));
                        return Mono.just("");
                    })
                    .subscribe();


        }
        catch(Exception e){
            System.out.println("application stopped because of an error on nadeo login: " + e.getMessage());
            e.printStackTrace();
            System.exit(99);
        }

    }

    public MapLBResponse getMapLB(){
        return null;
    }


    public List<TrackRecord> getRecords(String trackUid){
        URI url = URI.create("https://live-services.trackmania.nadeo.live/api/token/leaderboard/group/Personal_Best/map/" + trackUid +  "/top?length=100&onlyWorld=true&offset=0");
        try {
            return WebClient.create().get()
                    .uri(url)
                    .headers(httpHeaders -> {
                        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                        httpHeaders.set("Authorization", "nadeo_v1 t=" + accesToken);
                        httpHeaders.set("User-Agent","Kaartenbewakers-alpha, wrs on team maps, test, java springboot / " + ubiUser);
                    })
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, this::handleErrors)
                    .onStatus(HttpStatusCode::is5xxServerError, this::handleServerErrors)
                    .bodyToMono(MapLBResponse0.class)
                    .map(MapLBResponse0::tops)
                    .map(MapLBResponse1::top)
                    .map(mapLBResponses -> {
                        List<TrackRecord> trList = new ArrayList<>();
                        for (MapLBResponse responses : mapLBResponses) {
                            Track track =
                            TrackRecord r = TrackRecord.builder()
                                    .
                                    .build();
                        }
                        return trList;
                    })
                    .block();
        }
        catch(Exception e){
            System.out.println("application stopped because of an error on retrieving records: " + e.getMessage());
            e.printStackTrace();
            System.exit(99);
            return null;
        }
    }

    public NadeoLogin getNadeoLogin() {
        if(ubiTicket == null || ubiTicket.equals("")){
            System.out.println("application stopped because of an error on nadeo login: Ubisoft Ticket is not defined");
            System.exit(99);
            return null;
        }
        URI url = URI.create("https://prod.trackmania.core.nadeo.online/v2/authentication/token/ubiservices");
        try {
            return WebClient.create().get()
                    .uri(url)
                    .headers(httpHeaders -> {
                        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                        httpHeaders.set("Authorization", "ubi_v1 t=" + ubiTicket);
                        httpHeaders.set("User-Agent","Kaartenbewakers-alpha, wrs on team maps, test, java springboot / " + ubiUser);
                    })
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, this::handleErrors)
                    .onStatus(HttpStatusCode::is5xxServerError, this::handleServerErrors)
                    .bodyToMono(NadeoLogin.class)
                    .map(nadeo -> {
                        String aToken = nadeo.accessToken();
                        String rToken = nadeo.refreshToken();
                        return new NadeoLogin(nadeo.accessToken(), nadeo.refreshToken());
                    })
                    .block();
        }
        catch(Exception e){
            System.out.println("application stopped because of an error on nadeo login: " + e.getMessage());
            e.printStackTrace();
            System.exit(99);
            return null;
        }
    }

    private UbiLogin getUbiLogin(){
        URI url = URI.create("https://public-ubiservices.ubi.com/v3/profiles/sessions");
        try {
            return WebClient.create()
                    .get()
                    .uri(url)
                    .headers(httpHeaders -> {
                        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                        httpHeaders.setBasicAuth(ubiUser, ubiPassword);
                        httpHeaders.set("Ubi-AppId", "86263886-327a-4328-ac69-527f0d20a237");
                        httpHeaders.set("User-Agent","Kaartenbewakers-alpha, wrs on team maps, test, java springboot / " + ubiUser);
                    }
                    )
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, this::handleErrors)
                    .onStatus(HttpStatusCode::is5xxServerError, this::handleServerErrors)
                    .bodyToMono(UbiLogin.class)
                    .map(ubi -> {
                            String ticket = ubi.ticket();
                            return new UbiLogin(ticket);
                    }).block();
        }
        catch(Exception e){
            System.out.println("application stopped because of an error on ubilogin: " + e.getMessage());
            e.printStackTrace();
            System.exit(99);
            return null;
        }
    }
    private Mono<? extends Throwable> handleServerErrors(ClientResponse clientResponse) {
        return Mono.error(new Exception("error" + clientResponse.statusCode() + ": " + clientResponse.logPrefix()));
    }

    private Mono<? extends Throwable> handleErrors(ClientResponse clientResponse) {
        return Mono.error(new Exception("error" + clientResponse.statusCode() + ": " + clientResponse.logPrefix()));
    }

}
