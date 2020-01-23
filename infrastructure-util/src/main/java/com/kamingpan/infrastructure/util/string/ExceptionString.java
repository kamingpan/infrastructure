package com.kamingpan.infrastructure.util.string;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常信息堆栈载(stack load)格式字符串
 *
 * @author kamingpan
 * @since 2016-03-24
 */
public class ExceptionString {

    /**
     * 通过输出流获取详细异常信息
     *
     * @param exception 异常
     * @return 详细异常信息
     */
    public static String formatByWriter(Exception exception) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter, true);
        exception.printStackTrace(printWriter);
        printWriter.flush();
        stringWriter.flush();
        return stringWriter.toString();
    }

    /**
     * 获取详细异常信息(不推荐使用)
     *
     * @param exception 异常
     * @return 详细异常信息
     */
    @Deprecated
    public static String formatByStackTrace(Exception exception) {
        StringBuilder result = new StringBuilder("");
        StackTraceElement[] stackTraceElements = exception.getStackTrace();
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            result.append("\tat ").append(stackTraceElement).append("\r\n");
        }
        return result.toString();
    }

    /**
     * 通过输出流获取详细异常信息
     *
     * @param exception 异常
     * @return 详细异常信息
     */
    public static String formatByOutputStream(Exception exception) {
        String result = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(outputStream);
            exception.printStackTrace(printStream);
            result = new String(outputStream.toByteArray());
            printStream.close();
            outputStream.close();
        } catch (Exception e) {
        }
        return result;
    }
}
