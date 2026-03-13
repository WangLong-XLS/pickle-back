package com.pickle.utils.rest;


import lombok.Data;

@Data
public class RestBean<T>{
    private Integer code;
    private String message;
    private T data;

    public RestBean(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    //返回成功
    public static <T> RestBean<T> success(){
        return new RestBean<>(201, "操作成功", null);
    }

    public static <T> RestBean<T> success(T data){
        return new RestBean<>(201, "操作成功", data);
    }

    public static <T> RestBean<T> success(String message){
        return new RestBean<T>(201, message, null);
    }

    public static <T> RestBean<T> success(T data, String message){
        return new RestBean<T>(201, message, data);
    }


    //返回失败
    public static <T> RestBean<T> fail(){
        return new RestBean<T>(501, "操作失败", null);
    }

    public static <T> RestBean<T> fail(Integer code){
        return new RestBean<T>(code, "操作失败", null);
    }

    public static <T> RestBean<T> fail(String message){
        return new RestBean<T>(501, message, null);
    }

    public static <T> RestBean<T> fail(Integer code, String message){
        return new RestBean<T>(code, message, null);
    }
}