package com.mini.advice_park.controller;

import com.mini.advice_park.dto.exception.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @GetMapping("/health-check")
    public ResponseEntity<Void> checkHealthStatus() {

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
