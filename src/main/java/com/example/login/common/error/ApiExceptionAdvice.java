package com.example.login.common.error;

import com.example.login.common.api.ApiResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class ApiExceptionAdvice {
    @ExceptionHandler({ApiException.class})
    public ResponseEntity<ApiResult> exceptionHandler(HttpServletRequest request, final ApiException e) {

        ApiExceptionEntity apiExceptionEntity = ApiExceptionEntity.builder()
                .errorCode(e.getError().getCode())
                .errorMessage(e.getError().getMessage())
                .build();

        //e.printStackTrace();

        return ResponseEntity
                .status(e.getError().getStatus())
                .body(ApiResult.builder()
                        .resultYn("N")
                        .message("error")
                        .exception(apiExceptionEntity)
                        .build());
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ApiResult> exceptionHandler(HttpServletRequest request, final RuntimeException e) {

        ApiExceptionEntity apiExceptionEntity = ApiExceptionEntity.builder()
                .errorCode(ExceptionEnum.RUNTIME_EXCEPTION.getCode())
                .errorMessage(e.getMessage())
                .build();

        e.printStackTrace();

        return ResponseEntity
                .status(ExceptionEnum.RUNTIME_EXCEPTION.getStatus())
                .body(ApiResult.builder()
                        .resultYn("N")
                        .message("error")
                        .exception(apiExceptionEntity)
                        .build());
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ApiResult> exceptionHandler(HttpServletRequest request, final AccessDeniedException e) {

        ApiExceptionEntity apiExceptionEntity = ApiExceptionEntity.builder()
                .errorCode(ExceptionEnum.ACCESS_DENIED_EXCEPTION.getCode())
                .errorMessage(e.getMessage())
                .build();

        e.printStackTrace();

        return ResponseEntity
                .status(ExceptionEnum.ACCESS_DENIED_EXCEPTION.getStatus())
                .body(ApiResult.builder()
                        .resultYn("N")
                        .message("error")
                        .exception(apiExceptionEntity)
                        .build());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiResult> exceptionHandler(HttpServletRequest request, final Exception e) {

        ApiExceptionEntity apiExceptionEntity = ApiExceptionEntity.builder()
                .errorCode(ExceptionEnum.INTERNAL_SERVER_ERROR.getCode())
                .errorMessage(e.getMessage())
                .build();

        e.printStackTrace();

        return ResponseEntity
                .status(ExceptionEnum.INTERNAL_SERVER_ERROR.getStatus())
                .body(ApiResult.builder()
                        .resultYn("N")
                        .message("error")
                        .exception(apiExceptionEntity)
                        .build());
    }

}
