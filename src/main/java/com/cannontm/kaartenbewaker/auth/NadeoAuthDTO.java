package com.cannontm.kaartenbewaker.auth;

import com.fasterxml.jackson.annotation.JsonProperty;


public record NadeoAuthDTO(
        @JsonProperty("accessToken") String accessToken,
        @JsonProperty("refreshToken") String refreshToken
) {}
