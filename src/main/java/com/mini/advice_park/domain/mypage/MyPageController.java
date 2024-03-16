package com.mini.advice_park.domain.mypage;

import com.mini.advice_park.domain.Comment.dto.CommentResponse;
import com.mini.advice_park.domain.post.PostService;
import com.mini.advice_park.domain.post.dto.PostResponse;
import com.mini.advice_park.global.common.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    /**
     * 내가 작성한 질문글 모두 조회
     */
    @GetMapping("/api/mypage/post")
    public ResponseEntity<BaseResponse<List<PostResponse>>> getPostByCurrentUser(HttpServletRequest httpServletRequest) {

        BaseResponse<List<PostResponse>> response = myPageService.getPostsByCurrentUser(httpServletRequest);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    /**
     * 내가 작성한 댓글 모두 조회
     */
    @GetMapping("/api/mypage/comment")
    public ResponseEntity<BaseResponse<List<CommentResponse>>> getCommentsByCurrentUser(HttpServletRequest httpServletRequest) {

        BaseResponse<List<CommentResponse>> response = myPageService.getCommentsByCurrentUser(httpServletRequest);

        return ResponseEntity.status(response.getCode()).body(response);
    }

}
