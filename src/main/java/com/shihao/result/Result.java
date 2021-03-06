package com.shihao.result;

public class Result<T> {

    private int code;//状态码
    private String msg;//异常信息
    private T t;//数据信息

    /**
     *  成功时候的调用
     * */
    public static  <T> Result<T> success(T data){
        return new Result<T>(data);
    }

    /**
     *  失败时候的调用
     * */
    public static  <T> Result<T> error(CodeMsg codeMsg){
        return new Result<T>(codeMsg);
    }


    private Result(CodeMsg codeMsg){
        if(codeMsg!=null){
            this.code = codeMsg.getCode();
            this.msg = codeMsg.getMsg();
        }
    }

    private Result(T t) {
        this.t = t;
    }

    private Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
