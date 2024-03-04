package com.cannontm.kaartenbewakertm.player;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private PlayerRepository playerRepository;

    public void SavePlayers(List<Player> players){
        if(players != null && !players.isEmpty()){
            playerRepository.saveAll(players);
        }
    }

    public Flux<Player> getListOfTheUnnamed(){
        Flux<Player> players = playerRepository.findAll();
        return players.filter(player -> player.getName() == null);
    }

}
