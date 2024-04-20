package com.security.infra.config.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String accessTokenSecret;
    private String refreshTokenSecret;
    private Long accessTokenExpirationTime;
    private Long refreshTokenExpirationTime;
    private String issuer;

    public Long getAccessTokenExpirationMilliTime() {
        return accessTokenExpirationTime * 1000;
    }

    public Long getRefreshTokenExpirationMilliTime() {
        return refreshTokenExpirationTime * 1000;
    }
}
