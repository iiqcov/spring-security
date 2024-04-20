package com.security.domain.auth.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenPairResponse {
    private String refreshToken;
    private String accessToken;

    @Builder
    public TokenPairResponse(String refreshToken, String accessToken){
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }
}
