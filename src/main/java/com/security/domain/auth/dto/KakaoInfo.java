package com.security.domain.auth.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "oauth2.kakao")
public class KakaoInfo {
    private String baseUrl;
    private String clientId;
    private String clientSecret;
    private String redirectUri;
}
