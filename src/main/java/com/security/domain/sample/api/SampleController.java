package com.security.domain.sample.api;

import com.security.domain.sample.dto.SampleResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {
    private final String SAMPLE_MESSEGE = "SAMPLE SUCCESS";

    @GetMapping("/sample")
    public ResponseEntity<SampleResponse> getSample(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(SampleResponse.builder()
                        .message(SAMPLE_MESSEGE)
                        .build());
    }
}
