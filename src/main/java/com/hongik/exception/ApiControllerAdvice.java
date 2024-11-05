package com.hongik.exception;

import com.hongik.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ApiResponse<Object>> handleException(AppException e) {
        log.error("appException = {}", e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(ApiResponse.of(
                        e.getErrorCode().getStatus(),
                        e.getMessage(),
                        null
                ));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiResponse<Object> bindException(BindException e) {
        log.error("bindException = {}", e.getMessage());
        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                null
        );
    }

    /**
     * RequestParam에 값이 들어오지 않으면 발생한다.
     * duplicate-nickname?nickname=1234인데, duplicate-nickname 이렇게만 왔을 때 발생함
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiResponse<Object> paramException(Exception e) {
        log.error("paramException = {}", e.getMessage());
        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                null
        );
    }

    /**
     * Get 요청을 보내야 하는데 Post 요청을 보냈을 때 발생한다. 405
     */
//    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> methodNotSupportedException(Exception e) {
        log.error("methodNotSupportedException = {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiResponse.of(
                        HttpStatus.METHOD_NOT_ALLOWED,
                        e.getMessage(),
                        null
                ));
    }


    /**
     * RequestParam Validation
     * RequestParam에 유효성 체크 실패하면 발생한다.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ApiResponse<Object> handlerMethodValidationException(HandlerMethodValidationException e) {
        log.error("HandlerMethodValidationException = {}", e.getMessage());
        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                e.getAllValidationResults().get(0).getResolvableErrors().get(0).getDefaultMessage(),
                null
        );
    }

    /**
     * Exception
     * 위 정의한 예외말고 다른 예외가 발생하면 터진다.
     */
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> exception(Exception e) {
        log.error("exception = {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.of(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        e.getMessage(),
                        null
                ));
    }

    /**
     * Admin이 아닐 때 발생하는 예외
     */
//    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthException(AuthorizationDeniedException e) {
        log.error("authorizationDeniedException = {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.of(
                        HttpStatus.FORBIDDEN,
                        e.getMessage(),
                        null
                ));
    }
}
