package com.mini.advice_park.domain.Comment.like;

import com.mini.advice_park.domain.Comment.CommentRepository;
import com.mini.advice_park.domain.Comment.entity.Comment;
import com.mini.advice_park.domain.user.UserRepository;
import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.domain.user.service.AuthService;
import com.mini.advice_park.global.common.BaseResponse;
import com.mini.advice_park.global.exception.CustomException;
import com.mini.advice_park.global.exception.ErrorCode;
import com.mini.advice_park.global.security.filter.JwtAuthorizationFilter;
import com.mini.advice_park.global.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final AuthService authService;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    /**
     * 좋아요 등록
     */
    @Transactional
    public BaseResponse<Void> createLike(Long commentId, HttpServletRequest httpServletRequest) {

        User user = authService.getCurrentUser(httpServletRequest);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));

        if (likeRepository.findByUserAndComment(user, comment).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_LIKED);
        }

        // 댓글의 좋아요 카운트 증가
        comment.incrementLikeCount();
        likeRepository.save(new Like(user, comment));

        return new BaseResponse<>(HttpStatus.CREATED, "좋아요 등록 성공", null);
    }

    /**
     * 좋아요 상태 반환
     */
    @Transactional(readOnly = true)
    public boolean isLiked(Long commentId, HttpServletRequest httpServletRequest) {

        User user = authService.getCurrentUser(httpServletRequest);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));

        return likeRepository.findByUserAndComment(user, comment).isPresent();
    }

    /**
     * 좋아요 삭제
     */
    @Transactional
    public BaseResponse<Void> deleteLike(Long commentId, HttpServletRequest httpServletRequest) {

        User user = authService.getCurrentUser(httpServletRequest);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));

        if (!likeRepository.findByUserAndComment(user, comment).isPresent()) {
            throw new CustomException(ErrorCode.NOT_FOUND_LIKE);
        }

        comment.decrementLikeCount();
        likeRepository.deleteByUserAndComment(user, comment);

        return new BaseResponse<>(HttpStatus.OK, "좋아요 삭제 성공", null);
    }

}
