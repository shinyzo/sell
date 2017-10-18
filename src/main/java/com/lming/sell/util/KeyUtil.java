package com.lming.sell.util;

import java.util.Random;

public class KeyUtil {

    /**
     * 单线程 生成唯一主键
     * 时间戳+6位随机数
     * @return
     */
    public static synchronized String genUniqueKey(){

        return  System.currentTimeMillis() + String.valueOf( new Random().nextInt(900000)+100000);
    }

    /**
     *单线程同步锁 生成订单号
     * 当前时间14位+6位随机数
     * @return
     */
    public static synchronized String getOrderId(){

       return DateUtil.getCurrentTime()+ String.valueOf( new Random().nextInt(900000)+100000);

    }



    public static void main(String[] args)
    {
        System.out.print(genUniqueKey());

        System.out.print(DateUtil.getCurrentTime());
    }

}
