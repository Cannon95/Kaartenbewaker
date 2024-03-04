package com.cannontm.kaartenbewakertm.player;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface PlayerRepository extends ReactiveCrudRepository<Player, String> {
}
