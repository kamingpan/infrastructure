package com.kamingpan.infrastructure.util.date;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期工具类
 *
 * @author kamingpan
 * @since 2017-06-29
 */
@Slf4j
public class DateUtil {

    // 年月日时分秒毫秒
    public static final String DATE_FORMAT_SECOND_MILLISECOND = "yyyy-MM-dd HH:mm:ss.SSS";
    // 年月日时分秒
    public static final String DATE_FORMAT_SECOND = "yyyy-MM-dd HH:mm:ss";
    // 年月日时分
    public static final String DATE_FORMAT_MINUTE = "yyyy-MM-dd HH:mm";
    // 年月日
    public static final String DATE_FORMAT_DAY = "yyyy-MM-dd";
    // 年月
    public static final String DATE_FORMAT_MONTH = "yyyy-MM";

    // 年月日时分秒毫秒 - 纯数字
    public static final String DATE_FORMAT_SECOND_MILLISECOND_NUMBER = "yyyyMMddHHmmssSSS";
    // 年月日时分秒 - 纯数字
    public static final String DATE_FORMAT_SECOND_NUMBER = "yyyyMMddHHmmss";
    // 年月日 - 纯数字
    public static final String DATE_FORMAT_DAY_NUMBER = "yyyyMMdd";
    // 年月 - 纯数字
    public static final String DATE_FORMAT_MONTH_NUMBER = "yyyyMM";

    // 年月日 - 中文
    public static final String DATE_FORMAT_DAY_ZH_CN = "yyyy年MM月dd日";
    // 年月 - 中文
    public static final String DATE_FORMAT_MONTH_ZH_CN = "yyyy年MM月";

    // 一天的毫秒数
    public static final Long MILLIS_OF_ONE_DAY = 24L * 60L * 60L * 1000L;
    // 一小时的毫秒数
    public static final Long MILLIS_OF_ONE_HOUR = 8L * 60L * 60L * 1000L;

    /**
     * 字符串转日期，字符串的格式如"yyyy-MM-dd HH:mm:ss"
     *
     * @param dateString 日期字符串
     * @param format     字符串格式
     * @return 日期
     */
    public static Date stringToDate(String dateString, String format) {
        try {
            return new SimpleDateFormat(format).parse(dateString);
        } catch (ParseException exception) {
            log.warn("日期格式转换异常", exception);
        }
        return null;
    }

    /**
     * 日期转字符串："yyyy-MM-dd HH:mm:ss"
     *
     * @param date   日期
     * @param format 字符串格式
     * @return 日期字符串
     */
    public static String dateToString(Date date, String format) {
        return new SimpleDateFormat(format, Locale.CHINA).format(date);
    }

    /**
     * 日期转换到秒格式的字符串："yyyy-MM-dd HH:mm:ss"
     *
     * @param date 日期
     * @return 日期字符串
     */
    public static String dateToSecond(Date date) {
        return null == date ? null : dateToString(date, DATE_FORMAT_SECOND);
    }

    /**
     * 日期到秒，字符串转时间，字符串格式为"yyyy-MM-dd HH:mm:ss"
     *
     * @param dateString 日期字符串
     * @return 日期
     */
    public static Date secondToDate(String dateString) {
        try {
            return new SimpleDateFormat(DATE_FORMAT_SECOND).parse(dateString);
        } catch (ParseException exception) {
            log.warn("日期格式转换异常", exception);
        }
        return null;
    }

    /**
     * 日期转换到天格式的字符串："yyyy-MM-dd"
     *
     * @param date 日期
     * @return 日期字符串
     */
    public static String dateToDay(Date date) {
        return dateToString(date, DATE_FORMAT_DAY);
    }

    /**
     * 日期转换到天格式的字符串："yyyy-MM-dd"
     *
     * @param date 日期
     * @return 日期字符串
     */
    public static String dateToMonth(Date date) {
        return dateToString(date, DATE_FORMAT_MONTH);
    }

    /**
     * 日期转换到天格式的字符串："yyyyMMdd"
     *
     * @param date 日期
     * @return 日期字符串
     */
    public static String dateToDayNumber(Date date) {
        return dateToString(date, DATE_FORMAT_DAY_NUMBER);
    }

    /**
     * 日期转换到天格式的字符串："yyyy年MM月dd日"
     *
     * @param date 日期
     * @return 日期字符串
     */
    public static String dateToDayZh(Date date) {
        return dateToString(date, DATE_FORMAT_DAY_ZH_CN);
    }

    /**
     * 日期转换到分格式的字符串："yyyy-MM-dd HH:mm"
     *
     * @param date 日期
     * @return 日期字符串
     */
    public static String dateToMinute(Date date) {
        return dateToString(date, DATE_FORMAT_MINUTE);
    }

    /**
     * 日期格式转换（从字符串格式转成另外一种字符串格式）
     *
     * @param dateString 时间字符串
     * @param fromFormat 初始时间格式
     * @param toFormat   目标时间格式
     * @return 日期字符串
     */
    public static String stringToString(String dateString, String fromFormat, String toFormat) {
        Date date = stringToDate(dateString, fromFormat);
        return dateToString(date, toFormat);
    }

    /**
     * 将数字格式年、月、日，转成日期格式
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 日期
     */
    public static Date intToDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    /**
     * 将数字格式年、月、日，转成相应格式的日期字符串
     *
     * @param year   年
     * @param month  月
     * @param day    日
     * @param format 日期字符串格式
     * @return 日期字符串
     */
    public static String intToString(int year, int month, int day, String format) {
        Date date = intToDate(year, month, day);
        return dateToString(date, format);
    }

    /**
     * 根据时间获取上一个月的时间
     *
     * @param date 时间
     * @return 上一个月的时间
     */
    public static Date getLastMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        return calendar.getTime();
    }

    /**
     * 根据时间获取上一天的时间
     *
     * @param date 时间
     * @return 上一天的时间
     */
    public static Date getLastDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTime();
    }

    /**
     * 根据时间获取下一天的时间
     *
     * @param date 时间
     * @return 下一天的时间
     */
    public static Date getNextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 获取指定时间的年月日（即移除时分秒，目前仅适用于东八区）
     *
     * @param date 指定时间
     * @return 指定时间的年月日 日期
     */
    public static Date getYearMonthDay(Date date) {
        if (null == date) {
            return null;
        }

        return new Date(date.getTime() / MILLIS_OF_ONE_DAY * MILLIS_OF_ONE_DAY - (MILLIS_OF_ONE_HOUR * 8L));
    }

}
