package com.example.login.common.api;

import com.example.login.common.error.ApiExceptionEntity;
import lombok.*;

@Getter
@NoArgsConstructor
@ToString
public class ApiResult{
    private String resultYn;
    private String message;
    private ApiExceptionEntity exception;

    @Builder
    public ApiResult(String resultYn, String message, ApiExceptionEntity exception) {
        this.resultYn = resultYn;
        this.message = message;
        this.exception = exception;
    }
}
