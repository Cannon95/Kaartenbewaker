package com.cannontm.kaartenbewaker.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobMessage {
    private JobType type;
    private String payload;
    private int priority;
}
