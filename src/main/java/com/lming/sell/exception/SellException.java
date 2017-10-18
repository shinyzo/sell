package com.lming.sell.exception;

import com.lming.sell.enums.ResultEnum;
import lombok.Getter;

/**
 * 异常处理
 */
@Getter
public class SellException extends RuntimeException {

    private Integer code;

    public SellException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public SellException(Integer code,String msg) {
        super(msg);
        this.code = code;
    }
}
