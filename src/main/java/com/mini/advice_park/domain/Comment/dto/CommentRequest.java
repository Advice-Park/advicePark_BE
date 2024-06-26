package com.mini.advice_park.domain.Comment.dto;

import com.mini.advice_park.domain.Comment.entity.CommentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentRequest {

    @NotBlank(message = "내용을 입력해주세요")
    @Size(min = 2, max = 300, message = "2자 ~ 300자 이내로 작성해주세요.")
    private String content;

    private CommentType commentType;

    public CommentRequest(String content, CommentType commentType) {
        this.content = content;
        this.commentType = commentType;
    }

}
