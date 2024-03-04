package com.mini.advice_park.domain.like;

import com.mini.advice_park.domain.oauth2.domain.OAuth2UserPrincipal;
import com.mini.advice_park.domain.user.UserRepository;
import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.global.common.BaseResponse;
import com.mini.advice_park.global.exception.CustomException;
import com.mini.advice_park.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment/{commentId}/like")
public class LikeController {

    private final LikeService likeService;
    private final UserRepository userRepository;

    /**
     * 좋아요 등록
     */
    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<BaseResponse<Void>> createLike(@PathVariable("commentId") Long commentId,
                                                         Authentication authentication) {
        // OAuth2UserPrincipal 클래스를 이용하여 유저 정보를 가져옴
        OAuth2UserPrincipal principal = (OAuth2UserPrincipal) authentication.getPrincipal();

        // OAuth2로 인증된 사용자의 이메일 주소를 사용하여 검색
        String email = principal.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // 사용자 아이디로 좋아요 등록
        likeService.createLike(user.getUserId(), commentId);

        return ResponseEntity.ok().body(new BaseResponse<>(HttpStatus.CREATED, "성공", null));
    }

    /**
     * 좋아요 상태 반환
     */
    @GetMapping("")
    public ResponseEntity<BaseResponse<Boolean>> checkLikeStatus(@PathVariable("commentId") Long commentId,
                                                                 Authentication authentication) {
        // OAuth2UserPrincipal 클래스를 이용하여 유저 정보를 가져옴
        OAuth2UserPrincipal principal = (OAuth2UserPrincipal) authentication.getPrincipal();

        // OAuth2로 인증된 사용자의 이메일 주소를 사용하여 검색
        String email = principal.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // 좋아요 서비스 호출하여 상태 확인
        boolean isLiked = likeService.isLiked(user.getUserId(), commentId);
        return ResponseEntity.ok().body(new BaseResponse<>(HttpStatus.CREATED, "성공", isLiked));
    }

    /**
     * 좋아요 삭제
     */
    @DeleteMapping("")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<BaseResponse<Void>> deleteLike(@PathVariable("commentId") Long commentId,
                                                         Authentication authentication) {
        // OAuth2UserPrincipal 클래스를 이용하여 유저 정보를 가져옴
        OAuth2UserPrincipal principal = (OAuth2UserPrincipal) authentication.getPrincipal();

        // OAuth2로 인증된 사용자의 이메일 주소를 사용하여 검색
        String email = principal.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        likeService.deleteLike(user.getUserId(), commentId);
        return ResponseEntity.ok().body(new BaseResponse<>(HttpStatus.CREATED, "성공", null));
    }
}