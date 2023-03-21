package com.example.blogsearchapi.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "1400","입력값이 유효하지 않습니다."),
    KAKAO_CLIENT_ERR(HttpStatus.BAD_REQUEST, "2400", "카카오 통신 에러(클라이언트 오류)"),
    NAVER_CLIENT_ERR(HttpStatus.BAD_REQUEST, "3400", "네이버 통신 에러(클라이언트 오류)"),
    NAVER_SERVER_ERR(HttpStatus.INTERNAL_SERVER_ERROR, "3500", "네이버 통신 에러(서버 오류)"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "9999","Internal Server error")
    ;


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
