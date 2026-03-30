package com.cannontm.kaartenbewaker.dto;

import com.cannontm.kaartenbewaker.player.Player;

public record DiscordMessageDTO(
        Player player,
        int score,
        String diff,
        int pos,
        int previous_pos,
        Player player_up,
        Player player_down
) {}
