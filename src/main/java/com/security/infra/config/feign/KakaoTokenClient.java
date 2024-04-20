package com.security.infra.config.feign;

import com.security.domain.auth.dto.response.KakaoTokenResponse;
import com.security.global.config.feign.KakaoFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "kakaoTokenClient", url = "https://kauth.kakao.com", configuration = KakaoFeignConfig.class)
@Component
public interface KakaoTokenClient {
    @PostMapping(value = "/oauth/token")
    KakaoTokenResponse getToken(@RequestBody String KakaoTokenRequest);
}
