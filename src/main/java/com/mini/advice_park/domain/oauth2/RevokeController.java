package com.mini.advice_park.domain.oauth2;

import com.mini.advice_park.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth2/revoke")
@Tag(name = "연동 해제 API", description = "연동 해제 API")
public class RevokeController {

    /**
     * 계정 연결 해제
     */
    private final RevokeService revokeService;

    @DeleteMapping("/google")
    public ResponseEntity<BaseResponse<Void>> revokeGoogleAccount(@RequestHeader("Authorization") String accessToken) {
        BaseResponse<Void> response = revokeService.deleteGoogleAccount(accessToken);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/naver")
    public ResponseEntity<BaseResponse<Void>> revokeNaverAccount(@RequestHeader("Authorization") String accessToken) {
        BaseResponse<Void> response = revokeService.deleteNaverAccount(accessToken);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/kakao")
    public ResponseEntity<BaseResponse<Void>> revokeKakaoAccount(@RequestHeader("Authorization") String accessToken) {
        BaseResponse<Void> response = revokeService.deleteKakaoAccount(accessToken);
        return ResponseEntity.ok(response);
    }

}
