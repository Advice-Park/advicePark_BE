package com.mini.advice_park.domain.Comment.like;

import com.mini.advice_park.global.common.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment/{commentId}/like")
public class LikeController {

    private final LikeService likeService;

    /**
     * 좋아요 등록
     */
    @PostMapping("")
    public ResponseEntity<BaseResponse<Void>> createLike(@PathVariable("commentId") Long commentId,
                                                         HttpServletRequest httpServletRequest) {

        BaseResponse<Void> response = likeService.createLike(commentId, httpServletRequest);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    /**
     * 좋아요 상태 반환
     */
    @GetMapping("")
    public ResponseEntity<BaseResponse<Boolean>> checkLikeStatus(@PathVariable("commentId") Long commentId,
                                                                 HttpServletRequest httpServletRequest) {

        // 좋아요 서비스 호출하여 상태 확인
        boolean isLiked = likeService.isLiked(commentId, httpServletRequest);

        return ResponseEntity.ok().body(new BaseResponse<>(HttpStatus.OK, "좋아요 상태 확인 성공", isLiked));
    }

    /**
     * 좋아요 삭제
     */
    @DeleteMapping("")
    public ResponseEntity<BaseResponse<Void>> deleteLike(@PathVariable("commentId") Long commentId,
                                                         HttpServletRequest httpServletRequest) {

        BaseResponse<Void> response = likeService.deleteLike(commentId, httpServletRequest);

        return ResponseEntity.status(response.getCode()).body(response);
    }

}
