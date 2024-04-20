package com.security.global.util;

import jakarta.servlet.http.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    public HttpHeaders generateTokenCookies(String accessToken, String refreshToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.SET_COOKIE, createCookie("accessToken", accessToken));
        httpHeaders.add(HttpHeaders.SET_COOKIE, createCookie("refreshToken", refreshToken));
        return httpHeaders;
    }

    private String createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // HTTP를 통한 테스트를 위해 Secure 설정을 false로 변경
        cookie.setPath("/");
        return cookie.getName() + "=" + cookie.getValue() + "; SameSite=None"; // 쿠키의 이름과 값을 반환
    }
}
