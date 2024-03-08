package com.mini.advice_park.domain.Image;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mini.advice_park.domain.post.entity.Post;
import com.mini.advice_park.global.exception.CustomException;
import com.mini.advice_park.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageS3Service {

    private final AmazonS3 amazonS3;
    private final ImageRepository imageRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    /**
     * 게시물 이미지 다중 업로드
     */
    public List<Image> uploadMultipleImagesForPost(List<MultipartFile> multipartFiles, Post post) throws IOException {
        if (multipartFiles == null || multipartFiles.isEmpty()) {
            return Collections.emptyList();
        }

        return multipartFiles.stream()
                .map(multipartFile -> {
                    return uploadSingleImage(multipartFile, post);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 게시물 이미지 단일 업로드
     */
    private Image uploadSingleImage(MultipartFile multipartFile, Post post) {
        String storedImagePath = uploadFileToS3(multipartFile);
        String originName = multipartFile.getOriginalFilename();

        if (post != null) {
            throw new CustomException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }

        Image image = Image.builder()
                .originName(originName)
                .storedImagePath(storedImagePath)
                .post(post)
                .build();

        return imageRepository.save(image);
    }

    /**
     * 이미지 업로드
     */
    private String uploadFileToS3(MultipartFile image) {
        String originName = image.getOriginalFilename();
        String ext = originName.substring(originName.lastIndexOf("."));
        String changedName = UUID.randomUUID().toString() + ext;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(image.getContentType());
        metadata.setContentLength(image.getSize());

        try {
            amazonS3.putObject(new PutObjectRequest(bucketName, changedName, image.getInputStream(), metadata));
        } catch (IOException e) {
            throw new RuntimeException("이미지 S3 업로드 실패", e);
        }
        return amazonS3.getUrl(bucketName, changedName).toString();
    }

    /**
     * 이미지 삭제
     */
    public void deleteImages(List<Image> images) {
        if (images != null && !images.isEmpty()) {
            List<String> imageKeys = images.stream()
                    .map(Image::getStoredImagePath)
                    .collect(Collectors.toList());

            imageKeys.forEach(imageKey -> amazonS3.deleteObject(bucketName, imageKey));
            imageRepository.deleteAll(images);
        }
    }

    private String extractFileName(String fileUrl) {
        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    }

}
