package com.kamingpan.infrastructure.util.sql;

import java.util.Calendar;
import java.util.Date;

/**
 * sql工具
 *
 * @author kamingpan
 * @since 2017-06-30
 */
public class SqlUtil {

    private static final String LIKE_FLAG = "%";

    private static final String ALL_LIKE_TEMPLATE = "%%%s%%";

    private static final String LEFT_LIKE_TEMPLATE = "%s%%";

    private static final String RIGHT_LIKE_TEMPLATE = "%%%s";

    public SqlUtil() {
    }

    /**
     * 参数左右匹配
     *
     * @param parameter 参数
     * @return 格式化参数
     */
    public static String like(final String parameter) {
        // return (null == parameter || parameter.isEmpty()) ? null : (LIKE_FLAG + parameter + LIKE_FLAG);
        return (null == parameter || parameter.isEmpty()) ? null : String.format(ALL_LIKE_TEMPLATE, parameter);
    }

    /**
     * 参数左匹配
     *
     * @param parameter 参数
     * @return 格式化参数
     */
    public static String leftLike(final String parameter) {
        // return (null == parameter || parameter.isEmpty()) ? null : (parameter + LIKE_FLAG);
        return (null == parameter || parameter.isEmpty()) ? null : String.format(LEFT_LIKE_TEMPLATE, parameter);
    }

    /**
     * 参数右匹配
     *
     * @param parameter 参数
     * @return 格式化参数
     */
    public static String rightLike(final String parameter) {
        // return (null == parameter || parameter.isEmpty()) ? null : (LIKE_FLAG + parameter);
        return (null == parameter || parameter.isEmpty()) ? null : String.format(RIGHT_LIKE_TEMPLATE, parameter);
    }

    /**
     * 获取某天的零点
     *
     * @param date 时间
     * @return 日期零点
     */
    public static Date getBeginTimeOfDay(Date date) {
        if (null == date) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0); // 设置小时为零点
        calendar.set(Calendar.MINUTE, 0); // 设置分钟为零分
        calendar.set(Calendar.SECOND, 0); // 设置秒为零秒
        calendar.set(Calendar.MILLISECOND, 0); // 设置毫秒为0毫秒

        return calendar.getTime();
    }

    /**
     * 获取某天的末点
     *
     * @param date 时间
     * @return 日期末点
     */
    public static Date getEndTimeOfDay(Date date) {
        if (null == date) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23); // 设置小时为23点
        calendar.set(Calendar.MINUTE, 59); // 设置分钟为59分
        calendar.set(Calendar.SECOND, 59); // 设置秒为59秒
        calendar.set(Calendar.MILLISECOND, 999); // 设置毫秒为999毫秒

        return calendar.getTime();
    }
}
