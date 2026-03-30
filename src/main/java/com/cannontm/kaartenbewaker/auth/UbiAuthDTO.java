package com.cannontm.kaartenbewaker.auth;
import com.fasterxml.jackson.annotation.JsonProperty;


public record UbiAuthDTO(@JsonProperty("ticket") String ticket) {
}
