package com.cannontm.kaartenbewaker.auth;

public enum TokenAudience {
    CORE(""),
    LIVE("");

    private final String name;

    TokenAudience(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
