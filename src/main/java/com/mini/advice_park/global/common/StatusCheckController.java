package com.mini.advice_park.global.common;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "StatusCheck", description = "서버 상태 체크")
public class StatusCheckController {

    @GetMapping("/health-check")
    public ResponseEntity<Void> checkHealthStatus() {

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
