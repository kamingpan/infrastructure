package com.kamingpan.infrastructure.util.calendar;

import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;

/**
 * 阴历
 *
 * @author kamingpan
 * @since 2018-05-23
 */
@Slf4j
public class LunarCalendar {

    /**
     * 最小允许阴历月份
     */
    private static final int MIN_MONTH = 1;

    /**
     * 最大允许阴历月份
     */
    private static final int MAX_MONTH = 12;

    /**
     * 最小允许阴历日
     */
    private static final int MIN_DAY = 1;

    /**
     * 最大允许阴历日
     */
    private static final int MAX_DAY = 30;

    /**
     * 天干
     */
    private static final String[] GAN = {"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
    /**
     * 地支
     */
    private static final String[] ZHI = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
    /**
     * 大写中文数字
     */
    private static final String[] BIG_NUMBER = {"〇", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十"};
    /**
     * 生肖
     */
    private static final String[] ANIMAL = {"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};
    /**
     * 阴历月份
     */
    private static final String[] LUNAR_MONTH = {"正", "二", "三", "四", "五", "六", "七", "八", "九", "十", "冬", "腊"};

    // 阴历年
    private int year;

    // 阴历月
    private int month;

    // 阴历日
    private int day;

    // 是否闰月
    private boolean isLeapMonth;

    public LunarCalendar() {
    }

    public LunarCalendar(int year, int month, int day) {
        this.validateYear(year);
        this.validateMonth(month);
        this.validateDay(day);

        this.year = year;
        this.month = month;
        this.day = day;
    }

    public LunarCalendar(Date date) {
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

    public boolean isLeapMonth() {
        return isLeapMonth;
    }

    public void setLeapMonth(boolean leapMonth) {
        isLeapMonth = leapMonth;
    }

    /**
     * 判断两个阴历日期是否同一天
     *
     * @param lunarCalendar 被比较阴历日期
     * @return 是否同一天
     */
    public boolean equals(LunarCalendar lunarCalendar) {
        return 0 == this.compareTo(lunarCalendar);
    }

    /**
     * 比较两个阴历日期大小
     *
     * @param lunarCalendar 阴历日期
     * @return 比较结果（1：当前阴历日期大，0：两个比较阴历日期一样，-1：当前阴历日期小）
     */
    public int compareTo(LunarCalendar lunarCalendar) {
        Calendar thisLunar = Calendar.getInstance();
        thisLunar.set(this.getYear(), this.getMonth() + 1, this.getDay());

        Calendar compareLunar = Calendar.getInstance();
        compareLunar.set(lunarCalendar.getYear(), lunarCalendar.getMonth() + 1, lunarCalendar.getDay());
        return thisLunar.compareTo(compareLunar);
    }

    /**
     * 校验阴历年份
     */
    private void validateYear(int year) {
        if (year < ChineseCalendar.MIN_YEAR || year > ChineseCalendar.MAX_YEAR) {
            log.error("阴历年份只允许从{}到{}", ChineseCalendar.MIN_YEAR, ChineseCalendar.MAX_YEAR);
            throw new RuntimeException("阴历年份只允许从" + ChineseCalendar.MIN_YEAR + "年到" + ChineseCalendar.MAX_YEAR);
        }
    }

    /**
     * 校验阴历月份
     */
    private void validateMonth(int month) {
        if (month < LunarCalendar.MIN_MONTH || month > LunarCalendar.MAX_MONTH) {
            log.error("阴历月份只允许从{}到{}", LunarCalendar.MIN_MONTH, LunarCalendar.MAX_MONTH);
            throw new RuntimeException("阴历月份只允许从" + LunarCalendar.MIN_MONTH + "到" + LunarCalendar.MAX_MONTH);
        }
    }

    /**
     * 校验阴历日
     */
    private void validateDay(int day) {
        if (day < LunarCalendar.MIN_DAY || day > LunarCalendar.MAX_DAY) {
            log.error("阴历日只允许从{}到{}", LunarCalendar.MIN_DAY, LunarCalendar.MAX_DAY);
            throw new RuntimeException("阴历日只允许从" + LunarCalendar.MIN_DAY + "到" + LunarCalendar.MAX_DAY);
        }
    }

    /**
     * 根据年份获取天干地支甲子年
     *
     * @param lunarYear 阴历年份
     * @return 天干地支甲子年
     */
    public static String getGanZhi(int lunarYear) {
        return GAN[(lunarYear - 4) % 10] + ZHI[(lunarYear - 4) % 12];
    }

    /**
     * 根据年份获取生肖
     *
     * @param lunarYear 阴历年份
     * @return 生肖
     */
    public static String getAnimal(int lunarYear) {
        return ANIMAL[(lunarYear - 4) % 12];
    }

    /**
     * 根据年份获取年份大写
     *
     * @param lunarYear 阴历年份
     * @return 年份大写
     */
    public static String getBigNumberYear(int lunarYear) {
        StringBuilder stringBuilder = new StringBuilder();
        while (lunarYear != 0) {
            stringBuilder.insert(0, BIG_NUMBER[lunarYear % 10]);
            lunarYear /= 10;
        }
        return stringBuilder.toString();
    }

    /**
     * 根据阴历月份获取阴历月份描述
     *
     * @param lunarMonth  阴历月份
     * @param isLeapMonth 是否闰月
     * @return 阴历月份描述
     */
    public static String getLunarMonth(int lunarMonth, boolean isLeapMonth) {
        return (isLeapMonth ? "闰" : "") + LUNAR_MONTH[lunarMonth - 1] + "月";
    }

    /**
     * 根据阴历日获取阴历日描述
     *
     * @param lunarDay 阴历日
     * @return 阴历日描述
     */
    public static String getLunarDay(int lunarDay) {
        // 处理十位数
        String result = (lunarDay < 11) ? "初" : ((lunarDay < 20) ? "十" : ((lunarDay < 30) ? "廿" : "卅"));

        // 处理个位数
        result += (lunarDay % 10 != 0) ? BIG_NUMBER[lunarDay % 10] : "十";
        return result;
    }

    /**
     * 获取日期
     *
     * @return 获取当前农历日期
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
     * @return 获取当前农历时间戳
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

    public String toSimpleString() {
        return LunarCalendar.getBigNumberYear(this.getYear())
                + "(" + LunarCalendar.getAnimal(this.getYear()) + ")" + "年"
                + LunarCalendar.getLunarMonth(this.getMonth(), this.isLeapMonth())
                + LunarCalendar.getLunarDay(this.getDay());
    }

    @Override
    public String toString() {
        return LunarCalendar.getBigNumberYear(this.getYear()) + "/"
                + LunarCalendar.getGanZhi(this.getYear()) + "(" + LunarCalendar.getAnimal(this.getYear()) + ")" + "年"
                + LunarCalendar.getLunarMonth(this.getMonth(), this.isLeapMonth())
                + LunarCalendar.getLunarDay(this.getDay());
    }
}
