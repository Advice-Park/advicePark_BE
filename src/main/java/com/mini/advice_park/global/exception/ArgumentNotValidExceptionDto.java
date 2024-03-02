package com.mini.advice_park.global.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.Getter;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ArgumentNotValidExceptionDto extends ExceptionDto {

    /**
     * 유효성검사 에러 처리 클래스
     */
    public final Map<String, String> errorFields;

    public ArgumentNotValidExceptionDto(final MethodArgumentNotValidException exception) {
        super(ErrorCode.INVALID_ARGUMENT);
        this.errorFields = new HashMap<>();
        exception.getBindingResult()
                .getAllErrors().forEach(e -> this.errorFields.put(((FieldError) e).getField(), e.getDefaultMessage()));
    }

    public ArgumentNotValidExceptionDto(final ConstraintViolationException exception) {
        super(ErrorCode.INVALID_ARGUMENT);
        this.errorFields = new HashMap<>();
        exception.getConstraintViolations()
                .forEach(e -> this.errorFields.put(e.getPropertyPath().toString(), e.getMessage()));
    }
}
