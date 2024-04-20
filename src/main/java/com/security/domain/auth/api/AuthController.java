package com.security.domain.auth.api;

import com.security.domain.auth.application.AuthService;
import com.security.domain.auth.dto.request.KakaoAuthenticationCodeRequest;
import com.security.domain.auth.dto.response.TokenPairResponse;
import com.security.global.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final CookieUtil cookieUtil;

    @PostMapping("/code")
    public ResponseEntity<Void> authenticateCode(@RequestBody KakaoAuthenticationCodeRequest request) {
        TokenPairResponse response = authService.socialLogin(request);

        String accessToken = response.getAccessToken();
        String refreshToken = response.getRefreshToken();
        HttpHeaders tokenHeaders = cookieUtil.generateTokenCookies(accessToken, refreshToken);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .headers(tokenHeaders)
                .body(null);
    }
}

