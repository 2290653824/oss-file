package com.zj.ossfileupload.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Result<T> {
    private int code;
    private String message;
    private T data;


    // 静态方法，返回成功结果
    public static <T> Result<T> success() {
        return new Result<>(200, "Success",null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "Success",data);
    }

    // 静态方法，返回失败结果
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }
}