package com.hyuuny.fkproducts.support.error;

import com.hyuuny.fkproducts.support.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(FkProductsException.class)
    public ResponseEntity<ApiResponse<?>> handleCoreException(FkProductsException e) {
        switch (e.getErrorType().getLogLevel()) {
            case ERROR:
                log.error("CoreException : {}", e.getMessage(), e);
                break;
            case WARN:
                log.warn("CoreException : {}", e.getMessage(), e);
                break;
            default:
                log.info("CoreException : {}", e.getMessage(), e);
                break;
        }
        return new ResponseEntity<>(
                ApiResponse.error(e.getErrorType(), e.getData()),
                e.getErrorType().getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        log.error("Exception : {}", e.getMessage(), e);
        return ResponseEntity
                .status(ErrorType.DEFAULT_ERROR.getStatus())
                .body(ApiResponse.error(ErrorType.DEFAULT_ERROR));
    }
}

