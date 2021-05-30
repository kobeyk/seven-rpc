package com.appleyk.rpc.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

/**
 * 日志（各种级别）记录工具类
 */
public class LoggerUtils {
    private static Logger logger = LoggerFactory.getLogger(LoggerUtils.class);
    public static void info(String message) {
        logger.info(message);
    }
    public static void info(String message, Object ... append) {
        FormattingTuple formattingTuple = MessageFormatter.arrayFormat(message, append);
        String format = formattingTuple.getMessage();
        logger.info(format);
    }
    public static void warn(String message) {
        logger.warn(message);
    }
    public static void warn(String message, Object ... append) {
        FormattingTuple formattingTuple = MessageFormatter.arrayFormat(message, append);
        String format = formattingTuple.getMessage();
        logger.warn(format);
    }
    public static void debug(String message) {
        logger.debug(message);
    }
    public static void debug(String message, Object ... append) {
        FormattingTuple formattingTuple = MessageFormatter.arrayFormat(message, append);
        String format = formattingTuple.getMessage();
        logger.debug(format);
    }
    public static void error(String message, Exception ex) {
        logger.error(message, ex);
    }
    public static void error(Integer errCode, String message) {
        logger.error("错误码：" + errCode + "，错误消息：" + message);
    }
    public static void error(String message) {
        logger.error("错误消息：" + message);
    }
    public static void error(Integer errCode, String message, Exception ex) {
        logger.error("错误码：" + errCode + "，错误消息：" + message + ",异常信息：" + ex.getMessage());
    }
    public static void error(String message, Object ... append) {
        FormattingTuple formattingTuple = MessageFormatter.arrayFormat(message, append);
        String format = formattingTuple.getMessage();
        logger.error(format);
    }
}
