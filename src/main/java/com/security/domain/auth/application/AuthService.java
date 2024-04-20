package com.security.domain.auth.application;

import com.security.domain.auth.dto.KakaoInfo;
import com.security.domain.auth.dto.request.KakaoAuthenticationCodeRequest;
import com.security.domain.auth.dto.request.KakaoTokenRequest;
import com.security.domain.auth.dto.response.KakaoTokenResponse;
import com.security.domain.auth.dto.response.TokenPairResponse;
import com.security.domain.member.dao.MemberRepository;
import com.security.domain.member.domain.Member;
import com.security.domain.member.domain.Profile;
import com.security.global.common.SecurityConstants;
import com.security.infra.config.feign.KakaoTokenClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final KakaoTokenClient kakaoTokenClient;
    private final KakaoInfo kakaoInfo;
    private final MemberRepository memberRepository;
    private final JwtTokenService jwtTokenService;

    private final JwtDecoder jwtDecoder = buildDecoder();

    public TokenPairResponse socialLogin(KakaoAuthenticationCodeRequest request){
        KakaoTokenResponse kakaoTokenResponse = kakaoTokenClient.getToken(
                KakaoTokenRequest.newInstance(kakaoInfo, request.getCode()).toString()
        );

        log.info("Token Type: {}", kakaoTokenResponse.getToken_type());
        log.info("Access Token: {}", kakaoTokenResponse.getAccess_token());
        log.info("Expires In: {}", kakaoTokenResponse.getExpires_in());
        log.info("Refresh Token: {}", kakaoTokenResponse.getRefresh_token());
        log.info("Refresh Token Expires In: {}", kakaoTokenResponse.getRefresh_token_expires_in());
        log.info("Scope: {}", kakaoTokenResponse.getScope());
        log.info("ID Token: {}", kakaoTokenResponse.getId_token());

        Jwt jwt = jwtDecoder.decode(kakaoTokenResponse.getId_token());
        OidcIdToken oidcIdToken = new OidcIdToken(kakaoTokenResponse.getId_token(), jwt.getIssuedAt(), jwt.getExpiresAt(), jwt.getClaims());

        // 발행자 검증
        String issuer = oidcIdToken.getIssuer().toString();
        if (!"https://kauth.kakao.com".equals(issuer)) {
            throw new IllegalArgumentException("Invalid issuer");
        }

        // 토큰 만료 검증
        Instant expiration = oidcIdToken.getExpiresAt();
        if (expiration.isBefore(Instant.now())) {
            throw new IllegalArgumentException("Token is expired");
        }

        // 카카오에서 발급한 토큰인지 검증
        String clientId = oidcIdToken.getAudience().get(0);
        if (!kakaoInfo.getClientId().equals(clientId)) {
            throw new IllegalArgumentException("Invalid client ID");
        }

        String email = oidcIdToken.getEmail();
        Optional<Member> optimalMember = memberRepository.findByProfileEmail(email);
        Member member = optimalMember
                .orElseGet(() -> saveMember(oidcIdToken));

        setAuthentication(member);

        return getLoginResponse(member);
    }

    private void setAuthentication(Member member) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(member.getProfile().getEmail()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(member, null, authorities);

        log.info("Creating Authentication object for user: {}", member.getProfile().getEmail());
        log.info("Authorities: {}", authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Member saveMember(OidcIdToken oidcIdToken) {
        Profile profile = Profile.builder()
                .nickname(oidcIdToken.getClaim("nickname"))
                .profileUrl(oidcIdToken.getClaim("picture"))
                .email(oidcIdToken.getEmail())
                .build();

        Member member = Member.builder()
                .profile(profile)
                .build();
        return memberRepository.save(member);
    }

    private TokenPairResponse getLoginResponse(Member member) {
        String accessToken = jwtTokenService.createAccessToken(member.getProfile());
        String refreshToken = jwtTokenService.createRefreshToken(member);

        return TokenPairResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private JwtDecoder buildDecoder() {
        String jwkUrl = SecurityConstants.KAKAO_JWK_SET_URL;
        return NimbusJwtDecoder.withJwkSetUri(jwkUrl).build();
    }
}

