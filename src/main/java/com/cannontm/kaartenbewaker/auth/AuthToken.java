package com.cannontm.kaartenbewaker.auth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


@Table(name = "auth_token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthToken {


    @Id
    private Integer id;

    private String ticket;

    @Column(name = "access_token_live")
    private String accessTokenLive;

    @Column(name = "refresh_token_live")
    private String refreshTokenLive;

    @Column(name = "access_token_core")
    private String accessTokenCore;

    @Column(name = "refresh_token_core")
    private String refreshTokenCore;

    @Column(name = "expires_at_live")
    Long expiresAtLive;

    @Column(name = "refreshable_at_live")
    Long refreshableAtLive;

    @Column(name = "expires_at_core")
    Long expiresAtCore;

    @Column(name = "refreshable_at_core")
    Long refreshableAtCore;
}
