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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public BaseResponse<CommentResponse> createComment(Long userId, Long postId, @Valid CommentRequest commentRequest) {

        // 유저 정보 가져오기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // 게시물 정보 가져오기
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        Comment comment = Comment.of(commentRequest.getContent(), user, post);
        commentRepository.save(comment);

        // 댓글 생성 후 CommentResponse로 변환
        CommentResponse commentResponse = new CommentResponse(
                comment.getCommentId(),
                user.getUserId(),
                post.getPostId(),
                comment.getContent(),
                0,
                comment.getCreatedTime());

        return new BaseResponse<>(HttpStatus.CREATED, "성공", commentResponse);
    }

    @Transactional(readOnly = true)
    public BaseResponse<List<CommentResponse>> getAllComments(Long postId) {

        // 게시물 정보 가져오기
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        // 게시물에 달린 댓글들 가져오기
        List<Comment> comments = commentRepository.findByPostId(postId);

        // 댓글들을 CommentResponse로 변환
        List<CommentResponse> commentResponses = comments.stream()
                .map(comment -> {
                    int likeCount = likeRepository.countByComment(comment);

                    return new CommentResponse(
                            comment.getCommentId(),
                            comment.getUser().getUserId(),
                            comment.getPost().getPostId(),
                            comment.getContent(),
                            0,
                            comment.getCreatedTime());
                }).collect(Collectors.toList());

        return new BaseResponse<>(HttpStatus.OK, "성공", commentResponses);
    }

    @Transactional
    public BaseResponse<Void> deleteComment(Long userId, Long postId, Long commentId) {

        // 유저 정보 가져오기
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        // 게시물에 달린 댓글 정보 가져오기
        Comment comment = commentRepository.findByCommentIdAndPost(commentId, post)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));

        if (!comment.getUser().getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        commentRepository.delete(comment);
        return new BaseResponse<>(HttpStatus.OK, "성공", null);
    }
}
