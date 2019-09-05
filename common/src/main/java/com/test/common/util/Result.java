package com.test.common.util;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Result implements Serializable {

    private int code;
    private String msg;
    private Object data;


    private Result(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Result ok() {
        return new Result(200, "ok", null);
    }

    public static Result error(String msg) {
        return new Result(404, msg, null);
    }

    public static Result error() {
        return new Result(404, null, null);
    }

    public static Result ok(Object data) {
        return new Result(200, "ok", data);
    }

}
