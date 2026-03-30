package com.cannontm.kaartenbewaker.player;


import java.util.UUID;

//https://docs.spring.io/spring-data/jpa/reference/repositories/projections.html
public interface PlayerNameId {
    UUID getID();
    String getName();
}
