package com.mini.advice_park.domain.mypage;

import com.mini.advice_park.domain.post.dto.PostResponse;
import com.mini.advice_park.global.common.BaseResponse;
import com.mini.advice_park.global.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    /**
     * 등록 질문글 조회
     */
    @GetMapping("/api/post/mypage")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<BaseResponse<List<PostResponse>>> getPostByCurrentUser(Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String providerId = userDetails.getUsername();
        List<PostResponse> posts = myPageService.getPostsByCurrentUser(providerId).getResult();

        return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, "성공", posts));
    }

    /**
     * 내가 작성한 댓글 모두 조회
     */

}
