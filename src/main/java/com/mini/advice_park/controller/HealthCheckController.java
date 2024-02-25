package com.mini.advice_park.controller;

import com.mini.advice_park.dto.exception.ResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthCheckController {
    @GetMapping("/health")
    public ResponseDto<?> healthCheck() {
        return ResponseDto.ok("health check");
    }
}
