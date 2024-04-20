package com.security.domain.auth.dto.request;

import com.security.domain.auth.dto.KakaoInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class KakaoTokenRequest {

    private String code;
    private String client_id;
    private String redirect_uri;
    private String client_secret;
    private final String grant_type = "authorization_code";

    public static KakaoTokenRequest newInstance(KakaoInfo kakaoInfo, String code) {
        return KakaoTokenRequest.builder()
                .client_id(kakaoInfo.getClientId())
                .client_secret(kakaoInfo.getClientSecret())
                .redirect_uri(kakaoInfo.getRedirectUri())
                .code(code)
                .build();
    }

    @Override
    public String toString() {
        return
                "grant_type=" + grant_type + '&' +
                        "client_id=" + client_id + '&' +
                        "redirect_uri=" + redirect_uri + '&' +
                        "code=" + code + '&' +
                        "client_secret=" + client_secret;
    }
}
