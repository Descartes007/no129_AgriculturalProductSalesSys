package com.cow.horse.commcn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接口统一返回类
 */
//无参构造
@NoArgsConstructor
//有参构造
@AllArgsConstructor
@Data
public class Result {
    private String code;
    private String msg;
    private Object data;

    //返回成功信息
    public static Result success() {
        return new Result(Constants.CODE_200, "", null);
    }

    //成功返回+数据
    public static Result success(Object data) {
        return new Result(Constants.CODE_200, "", data);
    }

    //错误返回状态码 无数据
    public static Result error(String code, String msg) {
        return new Result(code, msg, null);
    }

    //错误返回状态码 无数据
    public static Result error() {
        return new Result(Constants.CODE_500, "系统出错", null);
    }

}
