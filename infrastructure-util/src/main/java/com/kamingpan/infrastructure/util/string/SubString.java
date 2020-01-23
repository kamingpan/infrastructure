package com.kamingpan.infrastructure.util.string;

/**
 * 截取字符串
 *
 * @author kamingpan
 * @since 2016-03-01
 */
public class SubString {

    private static final String ASTERISK = "*";

    /**
     * 截取字符串，从倒序的指定字符到字符串结束
     *
     * @param string 字符串
     * @param index  指定位置下标
     * @return 截取的字符串
     */
    public static String getSubString(String string, int index) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (i < index) {
            int lastFirst = string.lastIndexOf('/');
            result.insert(0, string.substring(lastFirst));
            string = string.substring(0, lastFirst);
            i++;
        }
        return result.substring(1);
    }


    /**
     * 将指定首和尾个数外的字符替换为星号
     *
     * @param string      截取的字符串
     * @param beginNumber 保留开头字符个数
     * @param endNumber   保留结尾字符个数
     * @return 替换后的字符串
     */
    public static String replaceAsterisk(String string, int beginNumber, int endNumber) {
        return replace(string, beginNumber, endNumber, SubString.ASTERISK);
    }

    /**
     * 将指定首和尾个数外的字符替换为指定字符
     *
     * @param string        截取的字符串
     * @param beginNumber   保留开头字符个数
     * @param endNumber     保留结尾字符个数
     * @param replaceString 指定替换的字符串
     * @return 替换后的字符串
     */
    public static String replace(String string, int beginNumber, int endNumber, String replaceString) {
        return replace(string, beginNumber, endNumber, replaceString, -1);
    }

    /**
     * 将指定首和尾个数外的字符替换为一定个数的指定字符
     *
     * @param string        截取的字符串
     * @param beginNumber   保留开头字符个数
     * @param endNumber     保留结尾字符个数
     * @param replaceNumber 替换指定字符的个数(负数时则替换相等个数)
     * @param replaceString 指定替换的字符串
     * @return 替换后的字符串
     */
    public static String replace(String string, int beginNumber, int endNumber, String replaceString, int replaceNumber) {
        StringBuilder stringBuilder = new StringBuilder(string.substring(0, beginNumber));
        if (replaceNumber >= 0) {
            for (int i = 0; i < replaceNumber; i++) {
                stringBuilder.append(replaceString);
            }
        } else {
            for (int i = 0; i < string.length() - endNumber - beginNumber; i++) {
                stringBuilder.append(replaceString);
            }
        }
        stringBuilder.append(string.substring(string.length() - endNumber, string.length()));
        return stringBuilder.toString();
    }
}
