package com.mini.advice_park.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * 400: Bad Request
     */
    BAD_REQUEST_ERROR(400, HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INVALID_ARGUMENT(400, HttpStatus.BAD_REQUEST, "요청에 유효하지 않은 인자입니다."),
    MISSING_REQUEST_PARAMETER(400, HttpStatus.BAD_REQUEST, "필수 요청 파라미터가 누락되었습니다."),
    INVALID_TOKEN(400, HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),

    IMAGE_UPLOAD_FAILED(400, HttpStatus.BAD_REQUEST, "이미지 업로드에 실패하였습니다."),
    IMAGE_DELETE_FAILED(400, HttpStatus.BAD_REQUEST, "이미지 삭제에 실패하였습니다."),

    ALREADY_LIKED(400, HttpStatus.BAD_REQUEST, "이미 좋아요를 누르셨습니다."),
    ALREADY_VOTED(400, HttpStatus.BAD_REQUEST, "이미 투표하셨습니다."),
    ALREADY_FAVORITE(400, HttpStatus.BAD_REQUEST, "이미 즐겨찾기에 추가된 게시글입니다."),


    /**
     * 401: Unauthorized
     */
    UNAUTHORIZED_ERROR(401, HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),

    /**
     * 403: Forbidden
     */
    FORBIDDEN_ERROR(403, HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    ACCESS_DENIED(403, HttpStatus.FORBIDDEN, "접근이 거부되었습니다."),
    NOT_MATCHED_USER(403, HttpStatus.FORBIDDEN, "일치하는 사용자가 없습니다."),
    REVOKE_FAILED(403, HttpStatus.FORBIDDEN, "토큰 해제에 실패하였습니다."),

    /**
     * 404: Not Found
     */
    NOT_FOUND_ERROR(404, HttpStatus.NOT_FOUND, "요청하신 페이지를 찾을 수 없습니다."),
    NOT_MATCHED_CODE(404, HttpStatus.NOT_FOUND, "일치하는 코드가 없습니다."),

    NOT_FOUND_USER(404, HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    NOT_FOUND_POST(404, HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    NOT_FOUND_COMMENT(404, HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
    NOT_FOUND_LIKE(404, HttpStatus.NOT_FOUND, "좋아요를 찾을 수 없습니다."),
    NOT_FOUND_VOTE(404, HttpStatus.NOT_FOUND, "투표를 찾을 수 없습니다."),
    NOT_FAVORITE(404, HttpStatus.NOT_FOUND, "즐겨찾기에 추가된 게시글이 아닙니다."),

    /**
     * 405: Method Not Allowed
     */
    METHOD_NOT_ALLOWED_ERROR(405, HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP Method 입니다."),

    /**
     * 500: Internal Server Error
     */
    INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생하였습니다."),
    DATA_BASE_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생하였습니다.");


    private final Integer code;
    private final HttpStatus status;
    private final String message;
}
