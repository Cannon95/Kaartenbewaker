package com.cannontm.kaartenbewakertm.apiservice;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MapLBResponse {
    private String accountId;
    private String name;
    private Integer position;
    private Integer score;

}
