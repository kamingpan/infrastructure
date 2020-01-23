package com.kamingpan.infrastructure.util.calendar;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 阳历
 *
 * @author kamingpan
 * @since 2018-05-23
 */
@Slf4j
public class SolarCalendar {

    /**
     * 最小允许阳历月份
     */
    private static final int MIN_MONTH = 1;

    /**
     * 最大允许阳历月份
     */
    private static final int MAX_MONTH = 12;

    /**
     * 最小允许阳历日
     */
    private static final int MIN_DAY = 1;

    /**
     * 最大允许阳历日
     */
    private static final int MAX_DAY = 31;

    // 阳历年
    private int year;

    // 阳历月
    private int month;

    // 阳历日
    private int day;

    public SolarCalendar() {
    }

    public SolarCalendar(int year, int month, int day) {
        this.validateYear(year);
        this.validateMonth(month);
        this.validateDay(day);

        this.year = year;
        this.month = month;
        this.day = day;
    }

    public SolarCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (null != date) {
            calendar.setTime(date);
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        this.validateYear(year);
        this.validateMonth(month);
        this.validateDay(day);

        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.validateYear(year);
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.validateMonth(month);
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.validateDay(day);
        this.day = day;
    }

    /**
     * 校验阳历年份
     */
    private void validateYear(int year) {
        if (year < ChineseCalendar.MIN_YEAR || year > ChineseCalendar.MAX_YEAR) {
            log.error("阳历年份只允许从{}到{}", ChineseCalendar.MIN_YEAR, ChineseCalendar.MAX_YEAR);
            throw new RuntimeException("阳历年份只允许从" + ChineseCalendar.MIN_YEAR + "到" + ChineseCalendar.MAX_YEAR);
        }
    }

    /**
     * 校验阳历月份
     */
    private void validateMonth(int month) {
        if (month < SolarCalendar.MIN_MONTH || month > SolarCalendar.MAX_MONTH) {
            log.error("阳历月份只允许从{}到{}", SolarCalendar.MIN_MONTH, SolarCalendar.MAX_MONTH);
            throw new RuntimeException("阳历月份只允许从" + SolarCalendar.MIN_MONTH + "到" + SolarCalendar.MAX_MONTH);
        }
    }

    /**
     * 校验阳历日
     */
    private void validateDay(int day) {
        if (day < SolarCalendar.MIN_DAY || day > SolarCalendar.MAX_DAY) {
            log.error("阳历日只允许从{}到{}", SolarCalendar.MIN_DAY, SolarCalendar.MAX_DAY);
            throw new RuntimeException("阳历日只允许从" + SolarCalendar.MIN_DAY + "到" + SolarCalendar.MAX_DAY);
        }
    }

    /**
     * 判断两个阳历日期是否同一天
     *
     * @param solarCalendar 被比较阳历日期
     * @return 是否同一天
     */
    public boolean equals(SolarCalendar solarCalendar) {
        return 0 == this.compareTo(solarCalendar);
    }

    /**
     * 比较两个阳历日期大小（1：当前阳历日期大，0：两个比较阳历日期一样，-1：当前阳历日期小）
     *
     * @param solarCalendar 阳历日期
     * @return 比较结果
     */
    public int compareTo(SolarCalendar solarCalendar) {
        Calendar compareLunar = Calendar.getInstance();
        compareLunar.set(solarCalendar.getYear(), solarCalendar.getMonth() + 1, solarCalendar.getDay());

        Calendar thisLunar = Calendar.getInstance();
        thisLunar.set(this.getYear(), this.getMonth() + 1, this.getDay());
        return thisLunar.compareTo(compareLunar);
    }

    /**
     * 获取日期
     *
     * @return 获取当前新历日期
     */
    public Date getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(this.year, this.month - 1, this.day);

        // 将时分秒,毫秒域清零
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取时间戳
     *
     * @return 获取当前新历时间戳
     */
    public long getTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(this.year, this.month - 1, this.day);

        // 将时分秒,毫秒域清零
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    @Override
    public String toString() {
        return new SimpleDateFormat("yyyy年MM月dd日").format(this.getDate());
    }

    public String toString(String format) {
        return new SimpleDateFormat(format).format(this.getDate());
    }
}
