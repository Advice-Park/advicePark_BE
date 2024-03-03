package com.mini.advice_park.domain.post.dto;

import com.mini.advice_park.domain.post.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostRequest {

    @NotBlank(message = "제목을 입력해주세요")
    @Size(min = 2, message = "최소 2자 이상 작성해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요")
    @Size(min = 2, message = "최소 2자 이상 작성해주세요.")
    private String contents;

    private Category category;

    private boolean option;

    private List<MultipartFile> imageFiles;

    public PostRequest(String title,
                       String contents,
                       Category category,
                       boolean option,
                       List<MultipartFile> imageFiles) {
        this.title = title;
        this.contents = contents;
        this.category = category;
        this.option = option;
        this.imageFiles = imageFiles;
    }
}
