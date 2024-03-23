package com.mini.advice_park.domain.user;

import com.mini.advice_park.domain.user.dto.SignUpRequest;
import com.mini.advice_park.domain.user.dto.UserInfo;
import com.mini.advice_park.domain.user.service.UserService;
import com.mini.advice_park.global.common.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    /**
     * 일반 회원가입 컨트롤러
     */
    @PostMapping("/signup")
    public void signUp(@RequestBody SignUpRequest request) {
        userService.signUp(request);
    }

    /**
     * 특정 유저 정보 조회
     */
    @GetMapping("/{userId}")
    public ResponseEntity<BaseResponse<UserInfo>> getUserInfo(@PathVariable Long userId) {
        UserInfo userInfo = userService.getUserInfo(userId);
        return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK.value(), "유저 정보 조회 성공", userInfo));
    }

    /**
     * 현재 로그인한 사용자 정보 조회
     */
    @GetMapping("/currentUserInfo")
    public ResponseEntity<BaseResponse<UserInfo>> getCurrentUserInfo(HttpServletRequest httpServletRequest) {
        UserInfo userInfo = userService.getCurrentUserInfo(httpServletRequest);
        return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK.value(), "현재 로그인한 사용자 정보 조회 성공", userInfo));
    }
    
}
