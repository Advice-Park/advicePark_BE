package com.mini.advice_park.domain.gallery;

import com.mini.advice_park.domain.Image.Image;
import com.mini.advice_park.domain.Image.ImageRepository;
import com.mini.advice_park.domain.post.PostRepository;
import com.mini.advice_park.domain.post.dto.PostResponse;
import com.mini.advice_park.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;

    // 각 포스트 ID당 모든 이미지를 반환하는 메서드
    public BaseResponse<List<ImageResponse>> getAllImagesByPostIds() {
        // 전체 포스트 ID를 가져오는 로직
        List<Long> postIds = getAllPostIds();

        // 각 포스트 ID당 모든 이미지를 가져옴
        List<Image> images = imageRepository.findAllImagesByPostIds(postIds);

        // Image 엔티티를 ImageResponse DTO로 변환
        List<ImageResponse> imageResponses = images.stream()
                .map(this::convertToImageResponse)
                .collect(Collectors.toList());

        // BaseResponse 생성
        return new BaseResponse<>(HttpStatus.OK, "이미지 반환 성공", imageResponses);
    }

    private List<Long> getAllPostIds() {
        // 데이터베이스에서 전체 포스트 ID를 가져오는 로직
        return postRepository.findAllPostIds();
    }

    private ImageResponse convertToImageResponse(Image image) {
        // Image 엔티티를 ImageResponse DTO로 변환하는 로직
        return ImageResponse.builder()
                .imageId(image.getImageId())
                .postId(image.getPost().getPostId())
                .originName(image.getOriginName())
                .storedImagePath(image.getStoredImagePath())
                .build();
    }

}
