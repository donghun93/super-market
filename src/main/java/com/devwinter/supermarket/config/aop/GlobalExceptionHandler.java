package com.devwinter.supermarket.config.aop;

import com.devwinter.supermarket.common.exception.SuperMarketException;
import com.devwinter.supermarket.common.response.ErrorArgumentResponse;
import com.devwinter.supermarket.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SuperMarketException.class)
    public ResponseEntity<ErrorResponse> superMarketException(SuperMarketException e) {
        log.error("superMarketException : ", e);

        return ResponseEntity.ok().body(new ErrorResponse(e.getErrorCode(), e.getErrorMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorArgumentResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("methodArgumentNotValidException : ", e);

        Map<String, String> errorMaps = e.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        i1 -> i1.getField(),
                        i2 -> i2.getDefaultMessage())
                );


        return ResponseEntity.ok().body(new ErrorArgumentResponse("ERROR-100", "유효성 검사 실패", errorMaps));
    }
}
