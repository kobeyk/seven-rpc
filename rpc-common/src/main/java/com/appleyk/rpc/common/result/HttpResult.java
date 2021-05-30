package com.appleyk.rpc.common.result;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
public class HttpResult {
    private int status;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date timestamp;

    public HttpResult() {
        this(200, "");
    }

    public HttpResult(int status, String message) {
        this(status, message, (Object)null);
    }

    public HttpResult(HttpResult.Builder builder) {
        this(builder.status, builder.message, builder.data);
    }

    public HttpResult(int status, String message, Object data) {
        this.timestamp = new Date();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static HttpResult ok() {
        return (new HttpResult.Builder()).status(HttpResult.ResultCode.SUCCESS.getCode()).build();
    }

    public static HttpResult ok(String message) {
        return (new HttpResult.Builder()).status(HttpResult.ResultCode.SUCCESS.getCode()).message(message).build();
    }

    public static HttpResult ok(Object data) {
        return (new Builder()).status(ResultCode.SUCCESS.getCode()).message("成功").data(data).build();
    }

    public static HttpResult ok(String message, Object data) {
        return (new HttpResult.Builder()).status(HttpResult.ResultCode.SUCCESS.getCode()).message(message).data(data).build();
    }

    public static HttpResult fail(int status, String message, Object data) {
        return (new HttpResult.Builder()).status(status).message(message).data(data).build();
    }

    public static HttpResult fail(int status, String message) {
        return (new HttpResult.Builder()).status(status).message(message).build();
    }

    public static HttpResult.Builder builder() {
        return new HttpResult.Builder();
    }

    public static class Builder {
        private int status;
        private String message;
        private Object data;

        public Builder() {
        }

        public HttpResult.Builder status(int status) {
            this.status = status;
            return this;
        }

        public HttpResult.Builder message(String message) {
            this.message = message;
            return this;
        }

        public HttpResult.Builder data(Object data) {
            this.data = data;
            return this;
        }

        public HttpResult build() {
            return new HttpResult(this);
        }
    }

    public static enum ResultCode {
        SUCCESS(200, "成功"),
        MISSING_FIELD(10000, "字段缺失"),
        INVALID_FIELD(10001, "无效字段"),
        RESOURCE_NOT_FOUND(10101, "实体不存在"),
        RESOURCE_ALREADY_EXIST(10102, "实体已存在"),
        RESOURCE_UNAVAILABLE(10103, "实体不可用"),
        RESOURCE_UNOPERATED(10104, "资源不可被操作"),
        ACCESS_DENIED(10201, "拒绝访问"),
        IDENTIFICATION_FAILED(10202, "用户认证失败"),
        ILLEGAL_AUTH_INFO(10203, "非法的用户认证信息"),
        TOKEN_EXPIRED(10204, "令牌过期"),
        UNAUTHORIZED(10205, "权限不足"),
        FILE_NOT_FOUND(10301, "文件不存在"),
        JSON_PARSE_ERROR(10302, "JSON解析失败"),
        SERVICE_UNAVAILABLE(10401, "服务不可用"),
        TIME_OUT(10402, "服务超时");

        private final Integer code;
        private final String description;

        private ResultCode(Integer code, String description) {
            this.code = code;
            this.description = description;
        }

        public Integer getCode() {
            return this.code;
        }

        public String getDescription() {
            return this.description;
        }
    }
}
