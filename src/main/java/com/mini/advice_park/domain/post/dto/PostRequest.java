package com.mini.advice_park.domain.post.dto;

import com.mini.advice_park.domain.post.entity.Category;
import com.mini.advice_park.domain.post.entity.PostVoteOption;
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
    @Size(min = 3, max = 25, message = "3~25자 범위로 작성해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요")
    @Size(min = 10, max = 1000, message = "10~1000자 범위로 작성해주세요.")
    private String contents;

    private Category category;
    private PostVoteOption postVoteOption;

    private List<MultipartFile> imageFiles;

    public PostRequest(String title,
                       String contents,
                       Category category,
                       PostVoteOption postVoteOption,
                       List<MultipartFile> imageFiles) {
        this.title = title;
        this.contents = contents;
        this.category = category;
        this.postVoteOption = postVoteOption;
        this.imageFiles = imageFiles;
    }
}
