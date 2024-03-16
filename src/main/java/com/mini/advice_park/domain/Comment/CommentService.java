package com.mini.advice_park.domain.Comment;

import com.mini.advice_park.domain.Comment.dto.CommentRequest;
import com.mini.advice_park.domain.Comment.dto.CommentResponse;
import com.mini.advice_park.domain.Comment.entity.Comment;
import com.mini.advice_park.domain.Comment.like.LikeRepository;
import com.mini.advice_park.domain.post.PostRepository;
import com.mini.advice_park.domain.post.entity.Post;
import com.mini.advice_park.domain.user.UserRepository;
import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.global.common.BaseResponse;
import com.mini.advice_park.global.exception.CustomException;
import com.mini.advice_park.global.exception.ErrorCode;
import com.mini.advice_park.global.security.filter.JwtAuthorizationFilter;
import com.mini.advice_park.global.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final JwtUtil jwtUtil;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    /**
     * 댓글 등록
     */
    @Transactional
    public BaseResponse<CommentResponse> createComment(Long postId,
                                                       @Valid CommentRequest commentRequest,
                                                       HttpServletRequest httpServletRequest) {

        try {

            String token = JwtAuthorizationFilter.resolveToken(httpServletRequest);
            if (!StringUtils.hasText(token) || !jwtUtil.validateToken(token)) {
                throw new CustomException(ErrorCode.UNAUTHORIZED_ERROR);
            }

            String email = jwtUtil.getEmail(token);
            User loginUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_ERROR));

            Comment comment = Comment.builder()
                    .content(commentRequest.getContent())
                    .user(loginUser)
                    .post(postRepository.findById(postId)
                            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST)))
                    .build();

            comment = commentRepository.save(comment);

            return new BaseResponse<>(HttpStatus.CREATED, "성공", CommentResponse.from(comment));

        } catch (DataAccessException e) {
            return new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.DATA_BASE_ERROR.getMessage(), null);

        } catch (CustomException e) {
            return new BaseResponse<>(e.getErrorCode().getStatus(), e.getErrorCode().getMessage(), null);
        }
    }

    /**
     * 특정 게시물의 모든 댓글 조회
     */
    @Transactional(readOnly = true)
    public BaseResponse<List<CommentResponse>> getAllComments(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        List<Comment> comments = commentRepository.findByPostId(postId);

        List<CommentResponse> commentResponses = comments.stream()
                .map(comment -> CommentResponse.from(comment))
                .collect(Collectors.toList());

        return new BaseResponse<>(HttpStatus.OK, "성공", commentResponses);
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    public BaseResponse<Void> deleteComment(Long postId, Long commentId, HttpServletRequest httpServletRequest) {

        try {

            String token = JwtAuthorizationFilter.resolveToken(httpServletRequest);
            if (!StringUtils.hasText(token) || !jwtUtil.validateToken(token)) {
                throw new CustomException(ErrorCode.UNAUTHORIZED_ERROR);
            }

            String email = jwtUtil.getEmail(token);
            User loginUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_ERROR));

            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

            Comment comment = commentRepository.findByCommentIdAndPost(commentId, post)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));

            if (!comment.getUser().equals(loginUser)) {
                throw new CustomException(ErrorCode.UNAUTHORIZED_ERROR);
            }

            commentRepository.delete(comment);

            return new BaseResponse<>(HttpStatus.OK, "성공", null);

        } catch (CustomException e) {
            return new BaseResponse<>(e.getErrorCode().getStatus(), e.getErrorCode().getMessage(), null);
        }
    }

}
