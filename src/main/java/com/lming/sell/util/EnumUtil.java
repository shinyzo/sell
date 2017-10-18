package com.lming.sell.util;

import com.lming.sell.enums.CodeEnum;

public class EnumUtil {

    public static <T extends CodeEnum> T getCodeEnum(Integer code, Class<T> enumClass)
    {
        for (T each : enumClass.getEnumConstants())
        {
            if(code.equals(each.getCode()))
            {
                return each;
            }
        }

        return null;
    }

}
