package com.mini.advice_park.domain.gallery;

import com.mini.advice_park.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image")
public class ImageController {

    private final ImageService imageService;

    // 각 포스트 ID당 등록되어 있는 모든 이미지를 조회
    @GetMapping("")
    public ResponseEntity<BaseResponse<List<ImageResponse>>> getAllImagesByPostIds() {
        BaseResponse<List<ImageResponse>> response = imageService.getAllImagesByPostIds();
        return ResponseEntity.status(response.getCode()).body(response);
    }

}
