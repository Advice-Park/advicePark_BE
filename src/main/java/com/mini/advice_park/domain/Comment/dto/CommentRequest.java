package com.mini.advice_park.domain.Comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentRequest {

    // TODO 최대입력 제한?

    @NotBlank(message = "내용을 입력해주세요")
    @Size(min = 2, message = "최소 2자 이상 작성해주세요.")
    private String content;

}
