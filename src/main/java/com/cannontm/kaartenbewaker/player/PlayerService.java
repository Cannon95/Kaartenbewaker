package com.cannontm.kaartenbewaker.player;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;

    public void save(Player player){
       playerRepository.save(player);
    }
    public Player getPlayer(String id){
        Optional<Player> player = playerRepository.findById(id);

        return player.orElse(null);
    }
    public String getPlayerNamefromID(UUID id){
        Optional<PlayerNameId> playerNameId = playerRepository.findById(id);
        if(playerNameId.isPresent()){
            return playerNameId.get().getName();
        }
        else return "Unknown Player";
    }
}



