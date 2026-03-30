package com.cannontm.kaartenbewaker.player;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayerRepository extends CrudRepository<Player, String> {
    Optional<PlayerNameId> findById(UUID id);
}
