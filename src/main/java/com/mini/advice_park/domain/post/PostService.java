package com.mini.advice_park.domain.post;

import com.mini.advice_park.domain.Image.Image;
import com.mini.advice_park.domain.Image.ImageS3Service;
import com.mini.advice_park.domain.post.dto.PostRequest;
import com.mini.advice_park.domain.post.dto.PostResponse;
import com.mini.advice_park.domain.post.entity.Post;
import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.global.util.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
            postRepository.save(post);

            // 이미지 업로드
            List<Image> uploadedImages = imageS3Service.uploadMultipleImagesForPost(imageFiles, post);
            uploadedImages.forEach(post::addImage);

            // 저장된 게시물로부터 PostResponse 생성
            PostResponse postResponse = PostResponse.from(post);

            return new BaseResponse<>(HttpStatus.CREATED.value(), "등록 성공", postResponse);

        } catch (Exception e) {
            return new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "등록 실패", null);
        }
    }

    /**
     * 질문글 전체 조회
     */

    /**
     * 질문글 삭제
     */
}
