package com.mini.advice_park.domain.oauth2;

import com.mini.advice_park.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    private final RevokeService revokeService;

    @Operation(summary = "구글 연동 해제", description = "구글 연동 해제")
    @DeleteMapping("/google")
    public ResponseEntity<BaseResponse<Void>> revokeGoogleAccount(@RequestHeader("Authorization") String accessToken) {
        BaseResponse<Void> response = revokeService.deleteGoogleAccount(accessToken);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "네이버 연동 해제", description = "네이버 연동 해제")
    @DeleteMapping("/naver")
    public ResponseEntity<BaseResponse<Void>> revokeNaverAccount(@RequestHeader("Authorization") String accessToken) {
        BaseResponse<Void> response = revokeService.deleteNaverAccount(accessToken);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "카카오 연동 해제", description = "카카오 연동 해제")
    @DeleteMapping("/kakao")
    public ResponseEntity<BaseResponse<Void>> revokeKakaoAccount(@RequestHeader("Authorization") String accessToken) {
        BaseResponse<Void> response = revokeService.deleteKakaoAccount(accessToken);
        return ResponseEntity.ok(response);
    }

}
