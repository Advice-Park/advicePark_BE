package com.mini.advice_park.domain.gallery;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ImageResponse {

    private final Long imageId;
    private final Long postId;
    private final String originName;
    private final String storedImagePath;

    @Builder
    public ImageResponse(Long imageId, Long postId, String originName, String storedImagePath) {
        this.imageId = imageId;
        this.postId = postId;
        this.originName = originName;
        this.storedImagePath = storedImagePath;
    }

}
