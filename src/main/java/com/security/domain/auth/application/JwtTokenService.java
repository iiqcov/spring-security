package com.security.domain.auth.application;

import com.security.domain.auth.dao.RefreshTokenRepository;
import com.security.domain.auth.domain.RefreshToken;
import com.security.domain.member.domain.Member;
import com.security.domain.member.domain.Profile;
import com.security.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    public String createAccessToken(Profile profile) {
        return jwtUtil.generateAccessToken(profile);
    }

    public String createRefreshToken(Member member) {
        String token = jwtUtil.generateRefreshToken(member.getId());
        RefreshToken refreshToken =
                RefreshToken.builder()
                        .member(member)
                        .refreshToken(token)
                        .build();
        refreshTokenRepository.save(refreshToken);
        return token;
    }
}

