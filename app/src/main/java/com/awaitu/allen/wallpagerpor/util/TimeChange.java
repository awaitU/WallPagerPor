package com.awaitu.allen.wallpagerpor.util;

import java.math.BigDecimal;

public class TimeChange {
       //设置时间
       public static String setTime(long total) {
           long hour = total / (1000 * 60 * 60);
           long letf1 = total % (1000 * 60 * 60);
           long minute = letf1 / (1000 * 60);
           long left2 = letf1 % (1000 * 60);
           long second = left2 / 1000;

           return hour + "'" + minute + "'" + second + "''";
       }
       /** 
        * byte(字节)根据长度转成kb(千字节)和mb(兆字节) 
        *  
        * @param bytes 
        * @return 
        */  
       public static String bytes2kb(long bytes) {
           BigDecimal filesize = new BigDecimal(bytes);
           BigDecimal megabyte = new BigDecimal(1024 * 1024);
           float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)
                   .floatValue();  
           if (returnValue > 1)  
               return (returnValue + "MB");  
           BigDecimal kilobyte = new BigDecimal(1024);
           returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP)
                   .floatValue();  
           return (returnValue + "KB");  
       }  
}
