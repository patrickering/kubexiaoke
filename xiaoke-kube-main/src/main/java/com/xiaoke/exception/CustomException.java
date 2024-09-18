package com.xiaoke.exception;

import com.xiaoke.common.core.enums.ResultStatusEnum;
import lombok.Data;

@Data
public class CustomException extends RuntimeException{
    private int code;
    private String message;

    public CustomException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public CustomException(ResultStatusEnum resultStatusEnum) {
        this.code = resultStatusEnum.getCode();
        this.message = resultStatusEnum.getMessage();
    }
}
