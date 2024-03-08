package com.mini.advice_park.domain.Comment;

import com.mini.advice_park.domain.Comment.dto.CommentRequest;
import com.mini.advice_park.domain.Comment.dto.CommentResponse;
import com.mini.advice_park.domain.oauth2.domain.OAuth2UserPrincipal;
import com.mini.advice_park.domain.user.UserRepository;
import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.global.common.BaseResponse;
import com.mini.advice_park.global.exception.CustomException;
import com.mini.advice_park.global.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;
    private final UserRepository userRepository;

    /**
     * 리뷰 등록
     */
    @PostMapping("/{postId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<BaseResponse<CommentResponse>> createComment(@PathVariable("postId") Long postId,
                                                                      @Valid @RequestBody CommentRequest commentRequest,
                                                                      Authentication authentication) {

        // OAuth2UserPrincipal 클래스를 이용하여 유저 정보를 가져옴
        OAuth2UserPrincipal principal = (OAuth2UserPrincipal) authentication.getPrincipal();

        // OAuth2로 인증된 사용자의 이메일 주소를 사용하여 검색
        String email = principal.getUsername();

        // User 객체를 이용하여 사용자 정보를 가져옴
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // 사용자 아이디로 리뷰 등록
        CommentResponse commentResponse = commentService.createComment(user.getUserId(), postId, commentRequest).getResult();

        return ResponseEntity.ok(new BaseResponse<>(HttpStatus.CREATED, "성공", commentResponse));
    }

    /**
     * 모든 리뷰 조회
     */
    @GetMapping("")
    public ResponseEntity<BaseResponse<List<CommentResponse>>> getAllComments(@PathVariable("postId") Long postId) {
        List<CommentResponse> comments = commentService.getAllComments(postId).getResult();
        return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, "성공", comments));
    }

    /**
     * 리뷰 삭제
     */
    @DeleteMapping("/{postId}/{commentId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<BaseResponse<Void>> deleteComment(@PathVariable("postId") Long postId,
                                                           @PathVariable("commentId") Long commentId,
                                                           Authentication authentication) {
        // OAuth2UserPrincipal 클래스를 이용하여 유저 정보를 가져옴
        OAuth2UserPrincipal principal = (OAuth2UserPrincipal) authentication.getPrincipal();

        // OAuth2로 인증된 사용자의 이메일 주소를 사용하여 검색
        String email = principal.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        commentService.deleteComment(user.getUserId(), postId, commentId);

        return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, "삭제 성공", null));
    }

}
