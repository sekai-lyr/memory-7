package com.sekai.picture.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Result<D> implements Serializable {

    @JsonProperty("isSuccess")
    private boolean success;
    private String code;
    private String message;
    private D data;

    public static <D> Result<D> ok(D data, String message) {
        Result<D> result = new Result<>();
        result.setSuccess(true);
        result.setCode("200");
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static <D> Result<D> fail(String message) {
        Result<D> result = new Result<>();
        result.setSuccess(false);
        result.setCode("400");
        result.setMessage(message);
        return result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }
}

