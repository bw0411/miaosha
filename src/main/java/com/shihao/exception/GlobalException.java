package com.shihao.exception;

import com.shihao.result.CodeMsg;

public class GlobalException extends RuntimeException {
    private CodeMsg codeMsg;

    public CodeMsg getCodeMsg() {
        return codeMsg;
    }

    public GlobalException(CodeMsg codeMsg){
        super(codeMsg.toString());
        this.codeMsg = codeMsg;
    }
}
