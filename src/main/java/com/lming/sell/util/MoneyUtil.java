package com.lming.sell.util;

public class MoneyUtil {

    private static final Double MONEY_RANGE= 0.01;

    /**
     * 校验两个金额是否相等，精度0.01
     * @param d1
     * @param d2
     * @return
     */
    public static boolean equals(Double d1,Double d2)
    {
        Double diff = Math.abs(d1-d2);
        if(diff < MONEY_RANGE)
        {
            return true;
        }
        return false;
    }

}
