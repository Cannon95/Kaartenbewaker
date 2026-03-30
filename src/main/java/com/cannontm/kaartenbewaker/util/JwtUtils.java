package com.cannontm.kaartenbewaker.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.Base64;

public class JwtUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static long getExp(String jwt) {
        return getClaimAsInstant(jwt, "exp");
    }

    public static long getRat(String jwt) {
        return getClaimAsInstant(jwt, "rat");
    }

    private static long getClaimAsInstant(String jwt, String claim) {
        try {
            String[] parts = jwt.split("\\.");
            if (parts.length < 2) throw new IllegalArgumentException("Invalid JWT");

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            JsonNode node = objectMapper.readTree(payloadJson);
            return node.get(claim).asLong();

        } catch (Exception e) {
            throw new RuntimeException("Failed to decode JWT claim: " + claim, e);
        }
    }
}
