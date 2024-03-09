package com.mini.advice_park.domain.post;

import com.mini.advice_park.domain.Image.Image;
import com.mini.advice_park.domain.Image.ImageS3Service;
import com.mini.advice_park.domain.post.dto.PostRequest;
import com.mini.advice_park.domain.post.dto.PostResponse;
import com.mini.advice_park.domain.post.entity.Post;
import com.mini.advice_park.domain.user.UserRepository;
import com.mini.advice_park.domain.user.UserService;
import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.global.common.BaseResponse;
import com.mini.advice_park.global.exception.CustomException;
import com.mini.advice_park.global.exception.ErrorCode;
import com.mini.advice_park.global.exception.ImageUploadException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ImageS3Service imageS3Service;
    private final UserService userService;
    private final UserRepository userRepository;

    /**
     * 질문글 등록
     */
    @Transactional
    public BaseResponse<PostResponse> createPost(PostRequest postRequest,
                                                 List<MultipartFile> imageFiles,
                                                 @AuthenticationPrincipal User loginUser) {
        try {
            // 이미지 업로드와 관련된 로직은 그대로 유지됩니다.
            Post post = Post.of(postRequest);
            List<Image> uploadedImages = imageS3Service.uploadMultipleImagesForPost(imageFiles, post);
            uploadedImages.forEach(post::addImage);

            // 로그인된 사용자의 정보를 사용하여 게시물 작성자 정보를 설정합니다.
            post.setUser(loginUser);

            // 게시물 저장
            postRepository.save(post);

            // 성공 응답 반환
            return new BaseResponse<>(HttpStatus.CREATED.value(), "등록 성공", PostResponse.from(post));
        } catch (IOException e) {
            // 이미지 업로드 실패 시
            return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), ErrorCode.IMAGE_UPLOAD_FAILED.getMessage(), null);
        } catch (DataAccessException e) {
            // 데이터베이스 에러
            return new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.DATA_BASE_ERROR.getMessage(), null);
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
    public BaseResponse<PostResponse> getPostById(Long postId) {
        try {
            Optional<Post> optionalPost = postRepository.findById(postId);
            if (optionalPost.isPresent()) {
                Post post = optionalPost.get();
                PostResponse postResponse = PostResponse.from(post);
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
    public BaseResponse<Void> deletePost(Long postId) {
        try {
            Optional<Post> optionalPost = postRepository.findById(postId);
            if (optionalPost.isPresent()) {
                Post post = optionalPost.get();

                // 첨부된 이미지 확인 및 삭제
                List<Image> images = post.getImages();
                if (!images.isEmpty()) {
                    imageS3Service.deleteImages(images);
                }

                // 게시물 삭제
                postRepository.deleteById(postId);

                return new BaseResponse<>(HttpStatus.OK.value(), "삭제 성공", null);
            } else {
                return new BaseResponse<>(HttpStatus.NOT_FOUND.value(), "삭제할 게시물이 존재하지 않습니다.", null);
            }
        } catch (Exception e) {
            return new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "삭제 실패", null);
        }
    }

    /**
     * 게시물 소유자 확인
     */
    public boolean isPostOwner(Long postId, Long userId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        return postOptional.map(post -> post.getUser().getUserId().equals(userId)).orElse(false);
    }

}
