package com.oa.generalos.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer code;
    private String message;
    private T data;
    
    public static final Integer SUCCESS_CODE = 200;
    public static final Integer ERROR_CODE = 500;
    public static final Integer UNAUTHORIZED_CODE = 401;
    public static final Integer FORBIDDEN_CODE = 403;
    public static final Integer NOT_FOUND_CODE = 404;
    public static final Integer BAD_REQUEST_CODE = 400;
    
    public Result() {
    }
    
    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    public static <T> Result<T> success() {
        return new Result<>(SUCCESS_CODE, "操作成功");
    }
    
    public static <T> Result<T> success(T data) {
        return new Result<>(SUCCESS_CODE, "操作成功", data);
    }
    
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(SUCCESS_CODE, message, data);
    }
    
    public static <T> Result<T> error() {
        return new Result<>(ERROR_CODE, "操作失败");
    }
    
    public static <T> Result<T> error(String message) {
        return new Result<>(ERROR_CODE, message);
    }
    
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message);
    }
    
    public static <T> Result<T> unauthorized(String message) {
        return new Result<>(UNAUTHORIZED_CODE, message);
    }
    
    public static <T> Result<T> forbidden(String message) {
        return new Result<>(FORBIDDEN_CODE, message);
    }
    
    public static <T> Result<T> notFound(String message) {
        return new Result<>(NOT_FOUND_CODE, message);
    }
    
    public static <T> Result<T> badRequest(String message) {
        return new Result<>(BAD_REQUEST_CODE, message);
    }
}
