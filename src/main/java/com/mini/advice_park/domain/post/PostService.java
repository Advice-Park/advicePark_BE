package com.mini.advice_park.domain.post;

import com.mini.advice_park.domain.Comment.CommentRepository;
import com.mini.advice_park.domain.Comment.entity.Comment;
import com.mini.advice_park.domain.Comment.like.Like;
import com.mini.advice_park.domain.Comment.like.LikeRepository;
import com.mini.advice_park.domain.Image.Image;
import com.mini.advice_park.domain.Image.ImageS3Service;
import com.mini.advice_park.domain.favorite.entity.UserPostFavorite;
import com.mini.advice_park.domain.favorite.UserPostFavoriteRepository;
import com.mini.advice_park.domain.post.dto.PostRequest;
import com.mini.advice_park.domain.post.dto.PostResponse;
import com.mini.advice_park.domain.post.entity.Post;
import com.mini.advice_park.domain.user.service.AuthService;
import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.global.common.BaseResponse;
import com.mini.advice_park.global.exception.CustomException;
import com.mini.advice_park.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final AuthService authService;
    private final PostRepository postRepository;
    private final ImageS3Service imageS3Service;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final UserPostFavoriteRepository favoriteRepository;

    /**
     * 사용자와 게시물 소유자 비교
     */
    private boolean isCurrentUserPostOwner(Post post, User user) {
        return post.getUser().equals(user);
    }

    /**
     * 질문글 등록
     */
    @Transactional
    public BaseResponse<PostResponse> createPost(PostRequest postRequest,
                                                 List<MultipartFile> imageFiles,
                                                 HttpServletRequest httpServletRequest) {
        try {
            User user = authService.getCurrentUser(httpServletRequest);

            Post post = Post.builder()
                    .title(postRequest.getTitle())
                    .contents(postRequest.getContents())
                    .category(postRequest.getCategory())
                    .postVoteOption(postRequest.getPostVoteOption())
                    .user(user)
                    .build();

            post = postRepository.save(post);

            List<Image> uploadedImages = imageS3Service.uploadMultipleImagesForPost(imageFiles, post);

            uploadedImages.forEach(post::addImage);

            return new BaseResponse<>(HttpStatus.CREATED.value(), "질문글 등록 성공", PostResponse.from(post));

        } catch (IOException e) {
            return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), ErrorCode.IMAGE_UPLOAD_FAILED.getMessage(), null);

        } catch (DataAccessException e) {
            return new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.DATA_BASE_ERROR.getMessage(), null);

        } catch (CustomException e) {
            return new BaseResponse<>(e.getErrorCode().getStatus(), e.getErrorCode().getMessage(), null);
        }
    }

    /**
     * 질문글 전체 조회
     */
    @Transactional(readOnly = true)
    public BaseResponse<List<PostResponse>> getAllPosts() {

        try {
            List<Post> posts = postRepository.findAll();
            List<PostResponse> postResponses = posts.stream()
                    .map(PostResponse::from)
                    .collect(Collectors.toList());

            return new BaseResponse<>(HttpStatus.OK.value(), "조회 성공", postResponses);

        } catch (Exception e) {
            return new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "조회 실패", null);
        }
    }

    /**
     * 특정 질문글 조회
     */
    @Transactional
    public BaseResponse<PostResponse> getPostById(Long postId) {

        try {
            Optional<Post> optionalPost = postRepository.findById(postId);

            if (optionalPost.isPresent()) {
                Post post = optionalPost.get();
                PostResponse postResponse = PostResponse.from(post);

                post.increaseViewCount();
                postRepository.save(post);

                return new BaseResponse<>(HttpStatus.OK.value(), "조회 성공", postResponse);

            } else {
                return new BaseResponse<>(HttpStatus.NOT_FOUND.value(), "질문글을 찾을 수 없습니다.", null);
            }

        } catch (Exception e) {
            return new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "조회 실패", null);
        }
    }

    /**
     * 질문글 삭제
     */
    @Transactional
    public BaseResponse<Void> deletePost(Long postId, HttpServletRequest httpServletRequest) {

        try {
            User user = authService.getCurrentUser(httpServletRequest);

            Optional<Post> optionalPost = postRepository.findById(postId);
            if (optionalPost.isPresent()) {
                Post post = optionalPost.get();

                if (isCurrentUserPostOwner(post, user)) {

                    List<Image> images = post.getImages();
                    if (!images.isEmpty()) {
                        imageS3Service.deleteImages(images);
                    }

                    List<Comment> comments = commentRepository.findByPost(post);
                    commentRepository.deleteAll(comments);

                    List<Like> commentLikes = likeRepository.findByCommentIn(comments);
                    likeRepository.deleteAll(commentLikes);

                    List<UserPostFavorite> favorites = favoriteRepository.findByPost(post);
                    favoriteRepository.deleteAll(favorites);

                    postRepository.deleteById(postId);

                    return new BaseResponse<>(HttpStatus.NO_CONTENT.value(), "삭제 성공", null);

                } else {
                    return new BaseResponse<>(HttpStatus.FORBIDDEN.value(), "삭제할 권한이 없습니다.", null);
                }

            } else {
                return new BaseResponse<>(HttpStatus.NOT_FOUND.value(), "삭제할 게시물이 존재하지 않습니다.", null);
            }

        } catch (DataAccessException e) {
            return new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.DATA_BASE_ERROR.getMessage(), null);

        } catch (CustomException e) {
            return new BaseResponse<>(e.getErrorCode().getStatus(), e.getErrorCode().getMessage(), null);
        }
    }

}
