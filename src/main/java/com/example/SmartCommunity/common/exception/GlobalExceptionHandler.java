package com.example.SmartCommunity.common.exception;

import cn.dev33.satoken.exception.NotLoginException;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.exception.UploadFileException;
import com.example.SmartCommunity.common.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.access.AccessDeniedException;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理访问不存在的对象的异常
     *
     * @param e NoSuchElementException 访问一个不存在的对象异常
     */
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleUserNotFoundException(NoSuchElementException e) {
        return ApiResponse.error(HttpStatus.NOT_FOUND, e.getMessage(), null);
    }

    /**
     * 处理数据库操作相关异常
     *
     * @param e DataAccessException 数据访问异常
     */
    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleDatabaseException(DataAccessException e) {
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "数据库操作失败：" + e.getMessage(), null);
    }

    /**
     * 处理参数校验失败异常（手动抛出的非法参数异常）
     *
     * @param e IllegalArgumentException 非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        return ApiResponse.error(HttpStatus.BAD_REQUEST, e.getMessage(), null);
    }

    /**
     * 处理 @Valid 注解引发的参数校验异常（主要针对 DTO 入参）
     *
     * @param e MethodArgumentNotValidException 方法参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleValidationException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ApiResponse.error(HttpStatus.BAD_REQUEST, errorMessage, null);
    }

    /**
     * 处理 @Validated 注解引发的参数校验异常（主要针对 URL 参数）
     *
     * @param e ConstraintViolationException 约束违反异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleConstraintViolationException(ConstraintViolationException e) {
        String errorMessage = e.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + "：" + violation.getMessage())
                .collect(Collectors.joining("; "));
        return ApiResponse.error(HttpStatus.BAD_REQUEST, errorMessage, null);
    }

    /**
     * 处理运行时发生的异常，如文件上传不成功等
     *
     * @param e RuntimeException 未知异常
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleGenericException(RuntimeException e) {
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<Void> handleAccessDeniedException(AccessDeniedException ex) {
        return ApiResponse.error(HttpStatus.FORBIDDEN, ex.getMessage(),null);
    }

    /**
     * 处理所有未知异常，作为兜底处理，防止程序崩溃
     *
     * @param e Exception 未知异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleGenericException(Exception e) {
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "发生错误：" + e.getMessage(), null);
    }

    @ExceptionHandler(NoApiKeyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleNoApiKeyException(NoApiKeyException e) {
        return ApiResponse.error(HttpStatus.BAD_REQUEST, "未提供有效的API Key" + e.getMessage(), null);
    }

    @ExceptionHandler(UploadFileException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleUploadFileException(UploadFileException e) {
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "文件上传失败：" + e.getMessage(), null);
    }

    @ExceptionHandler(ApiException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleApiException(ApiException e) {
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "调用 DashScope API 失败：" + e.getMessage(), null);
    }

    @ExceptionHandler(NotLoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<Void> handleNotLoginException() {
        return ApiResponse.error(HttpStatus.UNAUTHORIZED,"未登录或登录信息已过期，请登录后重试",null);
    }
}
