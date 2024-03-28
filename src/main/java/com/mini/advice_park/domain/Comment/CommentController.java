package com.mini.advice_park.domain.Comment;

import com.mini.advice_park.domain.Comment.dto.CommentRequest;
import com.mini.advice_park.domain.Comment.dto.CommentResponse;
import com.mini.advice_park.domain.oauth2.domain.OAuth2UserPrincipal;
import com.mini.advice_park.domain.user.UserRepository;
import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.global.common.BaseResponse;
import com.mini.advice_park.global.exception.CustomException;
import com.mini.advice_park.global.exception.ErrorCode;
import com.mini.advice_park.global.security.jwt.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
@Tag(name = "댓글 API", description = "댓글 API")
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 등록
     */
    @PostMapping("/{postId}")
    public ResponseEntity<BaseResponse<CommentResponse>> createComment(@PathVariable("postId") Long postId,
                                                                       @Valid @RequestBody CommentRequest commentRequest,
                                                                       HttpServletRequest httpServletRequest) {

        BaseResponse<CommentResponse> response = commentService.createComment(postId, commentRequest, httpServletRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponse<>(response.getCode(), response.getMessage(), response.getResult()));
    }

    /**
     * 특정 질문글의 모든 댓글 조회
     */
    @GetMapping("/{postId}")
    public ResponseEntity<BaseResponse<List<CommentResponse>>> getAllComments(@PathVariable("postId") Long postId) {
        List<CommentResponse> comments = commentService.getAllComments(postId).getResult();
        return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, "성공", comments));
    }


    /**
     * 댓글 삭제
     */
    @DeleteMapping("/{postId}/{commentId}")
    public ResponseEntity<BaseResponse<Void>> deleteComment(@PathVariable("postId") Long postId,
                                                            @PathVariable("commentId") Long commentId,
                                                            HttpServletRequest httpServletRequest) {

        BaseResponse<Void> response = commentService.deleteComment(postId, commentId, httpServletRequest);

        return ResponseEntity.status(response.getCode()).body(response);
    }

}
