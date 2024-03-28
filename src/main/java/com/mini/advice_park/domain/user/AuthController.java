package com.mini.advice_park.domain.user;

import com.mini.advice_park.domain.user.dto.SignUpRequest;
import com.mini.advice_park.domain.user.dto.UserInfo;
import com.mini.advice_park.domain.user.service.AuthService;
import com.mini.advice_park.domain.user.service.UserService;
import com.mini.advice_park.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "인증 API", description = "인증 API")
public class AuthController {

    private final UserService userService;

    @Operation(summary = "일반 회원가입", description = "일반 회원가입 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "회원가입 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/signup")
    public void signUp(@RequestBody SignUpRequest request) {
        userService.signUp(request);
    }

    @Operation(summary = "유저정보 반환(인증필요X)", description = "유저정보 반환 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저정보 반환 성공"),
            @ApiResponse(responseCode = "400", description = "유저정보 반환 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<BaseResponse<UserInfo>> getUserInfo(
            @Parameter(description = "유저 ID", required = true, example = "1")
            @PathVariable Long userId) {

        UserInfo userInfo = userService.getUserInfo(userId);
        return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK.value(), "유저 정보 조회 성공", userInfo));
    }

    @Operation(summary = "현재 로그인한 사용자 정보 조회", description = "현재 로그인한 사용자 정보 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "현재 로그인한 사용자 정보 조회 성공"),
            @ApiResponse(responseCode = "400", description = "현재 로그인한 사용자 정보 조회 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/currentUserInfo")
    public ResponseEntity<BaseResponse<UserInfo>> getCurrentUserInfo(HttpServletRequest httpServletRequest) {
        UserInfo userInfo = userService.getCurrentUserInfo(httpServletRequest);
        return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK.value(), "현재 로그인한 사용자 정보 조회 성공", userInfo));
    }
    
}
