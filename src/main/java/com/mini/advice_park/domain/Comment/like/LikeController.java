package com.mini.advice_park.domain.Comment.like;

import com.mini.advice_park.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/comment/{commentId}/like")
@Tag(name = "좋아요 API", description = "좋아요 API")
public class LikeController {

    private final LikeService likeService;

    /**
     * 좋아요 등록
     */
    @Operation(summary = "좋아요 등록", description = "댓글에 좋아요를 등록")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "좋아요 등록 성공"),
            @ApiResponse(responseCode = "400", description = "이미 좋아요한 댓글입니다"),
            @ApiResponse(responseCode = "401", description = "로그인이 필요합니다"),
            @ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없습니다")
        }
    )
    @PostMapping("")
    public ResponseEntity<BaseResponse<Void>> createLike(@PathVariable("commentId") Long commentId,
                                                         HttpServletRequest httpServletRequest) {

        BaseResponse<Void> response = likeService.createLike(commentId, httpServletRequest);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    /**
     * 좋아요 상태 반환
     */
    @Operation(summary = "좋아요 상태 확인", description = "댓글에 좋아요 상태를 확인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좋아요 상태 확인 성공"),
            @ApiResponse(responseCode = "401", description = "로그인이 필요합니다"),
            @ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없습니다")
        }
    )
    @GetMapping("")
    public ResponseEntity<BaseResponse<Boolean>> checkLikeStatus(@PathVariable("commentId") Long commentId,
                                                                 HttpServletRequest httpServletRequest) {

        boolean isLiked = likeService.isLiked(commentId, httpServletRequest);

        return ResponseEntity.ok().body(new BaseResponse<>(HttpStatus.OK, "좋아요 상태 확인 성공", isLiked));
    }

    /**
     * 좋아요 삭제
     */
    @Operation(summary = "좋아요 삭제", description = "댓글에 등록된 좋아요를 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좋아요 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "로그인이 필요합니다"),
            @ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없습니다"),
            @ApiResponse(responseCode = "404", description = "좋아요를 찾을 수 없습니다")
        }
    )
    @DeleteMapping("")
    public ResponseEntity<BaseResponse<Void>> deleteLike(@PathVariable("commentId") Long commentId,
                                                         HttpServletRequest httpServletRequest) {

        BaseResponse<Void> response = likeService.deleteLike(commentId, httpServletRequest);

        return ResponseEntity.status(response.getCode()).body(response);
    }

}
