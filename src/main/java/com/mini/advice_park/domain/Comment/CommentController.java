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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "댓글 작성", description = "질문글에 댓글을 작성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "댓글 작성 성공"),
            @ApiResponse(responseCode = "400", description = "댓글 작성 실패"),
            @ApiResponse(responseCode = "401", description = "로그인이 필요합니다"),
            @ApiResponse(responseCode = "404", description = "질문글을 찾을 수 없습니다"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/{postId}")
    public ResponseEntity<BaseResponse<CommentResponse>> createComment(
            @Parameter(description = "질문글 ID", required = true, example = "1")
            @PathVariable("postId") Long postId,
            @Valid @RequestBody CommentRequest commentRequest,
            HttpServletRequest httpServletRequest) {

        BaseResponse<CommentResponse> response = commentService.createComment(postId, commentRequest, httpServletRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponse<>(response.getCode(), response.getMessage(), response.getResult()));
    }

    @Operation(summary = "댓글 조회", description = "질문글에 작성된 댓글을 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 조회 성공"),
            @ApiResponse(responseCode = "404", description = "질문글을 찾을 수 없습니다"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{postId}")
    public ResponseEntity<BaseResponse<List<CommentResponse>>> getAllComments(
            @Parameter(description = "질문글 ID", required = true, example = "1")
            @PathVariable("postId") Long postId) {
        List<CommentResponse> comments = commentService.getAllComments(postId).getResult();
        return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, "성공", comments));
    }

    @Operation(summary = "댓글 삭제", description = "질문글에 작성된 댓글을 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "로그인이 필요합니다"),
            @ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없습니다"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/{postId}/{commentId}")
    public ResponseEntity<BaseResponse<Void>> deleteComment(
            @Parameter(description = "질문글 ID", required = true, example = "1")
            @PathVariable("postId") Long postId,
            @Parameter(description = "댓글 ID", required = true, example = "1")
            @PathVariable("commentId") Long commentId,
            HttpServletRequest httpServletRequest) {

        BaseResponse<Void> response = commentService.deleteComment(postId, commentId, httpServletRequest);

        return ResponseEntity.status(response.getCode()).body(response);
    }

}
