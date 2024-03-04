package com.mini.advice_park.domain.like;

import com.mini.advice_park.domain.Comment.CommentRepository;
import com.mini.advice_park.domain.Comment.entity.Comment;
import com.mini.advice_park.domain.user.UserRepository;
import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.global.exception.CustomException;
import com.mini.advice_park.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final UserRepository userRepository;
    private final LikeRepository reviewLikeRepository;
    private final CommentRepository commentRepository;

    /**
     * 좋아요 등록
     */
    @Transactional
    public void createLike(Long userId, Long commentId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));

        reviewLikeRepository.findByUserAndComment(user, comment)
                .ifPresent(like -> { throw new CustomException(ErrorCode.ALREADY_LIKED); });

        reviewLikeRepository.save(new Like(user, comment));
    }

    /**
     * 좋아요 상태 반환
     */
    @Transactional(readOnly = true)
    public boolean isLiked(Long userId, Long commentId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));

        return reviewLikeRepository.findByUserAndComment(user, comment).isPresent();
    }

    /**
     * 좋아요 삭제
     */
    @Transactional
    public void deleteLike(Long userId, Long commentId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));

        Like like = reviewLikeRepository.findByUserAndComment(user, comment)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_LIKE));

        reviewLikeRepository.delete(like);
    }

}
