package com.mini.advice_park.domain.Comment;

import com.mini.advice_park.domain.Comment.dto.CommentRequest;
import com.mini.advice_park.domain.Comment.dto.CommentResponse;
import com.mini.advice_park.domain.Comment.entity.Comment;
import com.mini.advice_park.domain.post.PostRepository;
import com.mini.advice_park.domain.post.entity.Post;
import com.mini.advice_park.domain.user.service.AuthService;
import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.global.common.BaseResponse;
import com.mini.advice_park.global.exception.CustomException;
import com.mini.advice_park.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final AuthService authService;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    /**
     * 게시물에서 댓글 조회
     */
    private Comment getComment(Long postId, Long commentId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        return commentRepository.findByCommentIdAndPost(commentId, post)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));
    }

    /**
     * 게시물의 댓글 수 감소
     */
    private void decreaseCommentCount(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        post.decreaseCommentCount();
        postRepository.save(post);
    }

    /**
     * 댓글 등록
     */
    @Transactional
    public BaseResponse<CommentResponse> createComment(Long postId,
                                                       @Valid CommentRequest commentRequest,
                                                       HttpServletRequest httpServletRequest) {

        try {
            User user = authService.getCurrentUser(httpServletRequest);

            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

            Comment comment = Comment.builder()
                    .content(commentRequest.getContent())
                    .user(user)
                    .post(post)
                    .build();

            comment = commentRepository.save(comment);

            post.increaseCommentCount();
            postRepository.save(post);

            return new BaseResponse<>(HttpStatus.CREATED, "댓글 등록 성공", CommentResponse.from(comment));

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
            User user = authService.getCurrentUser(httpServletRequest);

            Comment comment = getComment(postId, commentId);

            if (!comment.getUser().equals(user)) {
                throw new CustomException(ErrorCode.UNAUTHORIZED_ERROR);
            }

            commentRepository.delete(comment);

            decreaseCommentCount(postId);

            return new BaseResponse<>(HttpStatus.NO_CONTENT, "댓글 삭제 성공", null);

        } catch (CustomException e) {
            return new BaseResponse<>(e.getErrorCode().getStatus(), e.getErrorCode().getMessage(), null);
        }
    }

}
