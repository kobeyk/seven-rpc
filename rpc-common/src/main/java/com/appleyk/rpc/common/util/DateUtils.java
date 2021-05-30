package com.appleyk.rpc.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>日期格式化工具类/p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @date created on  下午10:03 2021/5/22
 */
public class DateUtils {
    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";// 时间格式
    public static String date2Str(Date date){
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_FORMAT);
        return format.format(date);
    }
    public static String dateNow2Str(){
       return date2Str(new Date());
    }
    public static void main(String[] args) {
        System.out.println(date2Str(new Date()));
    }
}
