package com.security.domain.sample.dto;

import lombok.*;

@Getter
public class SampleResponse {
    private final String message;

    @Builder
    public SampleResponse (String message) {
        this.message = message;
    }
}
