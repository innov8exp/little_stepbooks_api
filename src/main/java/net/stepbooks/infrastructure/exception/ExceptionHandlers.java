package net.stepbooks.infrastructure.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.MessageFormat;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus
    public final ResponseEntity<ErrorResponseDto> handleBizExceptions(BusinessException ex, HttpServletRequest request,
                                                                      HttpServletResponse response) {
        log.warn("请求发生了预期异常，出错的 url [{}]，出错的描述为 [{}]", request.getRequestURL().toString(), ex.getMessage());
        ErrorCode errorCode = ex.getErrorCode();
        ErrorResponseDto responseDto = ErrorResponseDto.builder()
                .status(errorCode.getStatus())
                .message(errorCode.getMessage())
                .build();
        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public final ResponseEntity<ErrorResponseDto> handleAuthException(AuthenticationException ex, HttpServletRequest request) {
        log.warn("请求发生了预期异常，出错的 url [{}]，出错的描述为 [{}]", request.getRequestURL().toString(), ex.getMessage());
        ErrorResponseDto responseDto = ErrorResponseDto.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<ErrorResponseDto> handleForbidException(AccessDeniedException ex, HttpServletRequest request) {
        log.warn("请求发生了预期异常，出错的 url [{}]，出错的描述为 [{}]", request.getRequestURL().toString(), ex.getMessage());
        ErrorResponseDto responseDto = ErrorResponseDto.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.error(MessageFormat.format("请求发生了非预期异常，出错的 url [{0}]，出错的描述为 [{1}]",
                request.getRequestURL().toString(), ex.getMessage()), ex);
        ErrorResponseDto responseDto = ErrorResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(HttpStatus.BAD_REQUEST.getReasonPhrase()).build();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            responseDto.addError(fieldName, errorMessage);
        });
        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public final ResponseEntity<ErrorResponseDto> handleDatabindException(ConstraintViolationException ex, HttpServletRequest request,
                                                                          HttpServletResponse response) {
        log.error(MessageFormat.format("请求发生了非预期异常，出错的 url [{0}]，出错的描述为 [{1}]",
                request.getRequestURL().toString(), ex.getMessage()), ex);
        ErrorResponseDto responseDto = ErrorResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(HttpStatus.BAD_REQUEST.getReasonPhrase()).build();
        for (ConstraintViolation<?> cv : ex.getConstraintViolations()) {
            responseDto.addError(cv.getPropertyPath().toString(), cv.getMessage());
        }
        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public final ResponseEntity<ErrorResponseDto> handleAllExceptions(Exception ex, HttpServletRequest request,
                                                                      HttpServletResponse response) {
        log.error(MessageFormat.format("请求发生了非预期异常，出错的 url [{0}]，出错的描述为 [{1}]",
                request.getRequestURL().toString(), ex.getMessage()), ex);
        ErrorResponseDto responseDto = ErrorResponseDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }

}
