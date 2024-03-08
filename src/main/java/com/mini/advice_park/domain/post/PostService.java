package com.mini.advice_park.domain.post;

import com.mini.advice_park.domain.Image.Image;
import com.mini.advice_park.domain.Image.ImageS3Service;
import com.mini.advice_park.domain.post.dto.PostRequest;
import com.mini.advice_park.domain.post.dto.PostResponse;
import com.mini.advice_park.domain.post.entity.Post;
import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ImageS3Service imageS3Service;

    /**
     * 질문글 등록
     */
    @Transactional
    public BaseResponse<PostResponse> createPost(PostRequest postRequest,
                                                 List<MultipartFile> imageFiles,
                                                 User currentUser) {
        try {
            // 게시물 생성
            Post post = Post.of(postRequest, currentUser);

            // 이미지 업로드
            List<Image> uploadedImages = imageS3Service.uploadMultipleImagesForPost(imageFiles, post);
            uploadedImages.forEach(post::addImage);

            // 저장된 게시물로부터 PostResponse 생성
            PostResponse postResponse = PostResponse.from(post);

            // 저장
            postRepository.save(post);

            return new BaseResponse<>(HttpStatus.CREATED.value(), "등록 성공", postResponse);

        } catch (Exception e) {
            // TODO 오류처리 명확하게 하기
            return new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "등록 실패", null);
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
