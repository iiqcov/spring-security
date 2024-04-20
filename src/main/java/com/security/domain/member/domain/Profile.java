package com.security.domain.member.domain;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class Profile {
    private String nickname;
    private String profileUrl;
    private String email;

    @Builder
    public Profile(String nickname, String profileUrl, String email){
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.email = email;
    }
}
