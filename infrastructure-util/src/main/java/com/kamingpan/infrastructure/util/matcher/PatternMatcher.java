package com.kamingpan.infrastructure.util.matcher;

import java.util.regex.Pattern;

/**
 * 正则表达式匹配工具
 *
 * @author kamingpan
 * @since 2016-08-25
 */
public class PatternMatcher {

    /**
     * 匹配正则表达式
     *
     * @param regex 正则表达式
     * @param value 比较参数
     * @return 验证结果
     */
    public static boolean match(String regex, String value) {
        // 如果正则表达式为空，则返回不匹配
        if (null == regex) {
            return false;
        }

        // 如果正则表达式不为空，但比较参数为空，也直接返回不匹配
        if (!regex.isEmpty() && (null == value || (value = value.trim()).isEmpty())) {
            return false;
        }

        // 正则匹配
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(value).find();
    }

    /**
     * 匹配正则表达式（区分大小写）
     *
     * @param regex 正则表达式
     * @param flags flags
     * @param value 比较参数
     * @return 验证结果
     */
    public static boolean match(String regex, int flags, String value) {
        // 如果正则表达式为空，则返回不匹配
        if (null == regex) {
            return false;
        }

        // 如果正则表达式不为空，但比较参数为空，也直接返回不匹配
        if (!regex.isEmpty() && (null == value || (value = value.trim()).isEmpty())) {
            return false;
        }

        // 正则匹配
        Pattern pattern = Pattern.compile(regex, flags);
        return pattern.matcher(value).find();
    }

    /**
     * 验证邮箱
     *
     * @param value 邮箱
     * @return 验证结果
     */
    public static boolean isEmail(String value) {
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        return PatternMatcher.match(check, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * 验证手机号码
     *
     * @param value 手机号码
     * @return 验证结果
     */
    public static boolean isMobile(String value) {
        String check = "^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\\d{8})$";
        return PatternMatcher.match(check, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * 验证座机号码
     *
     * @param value 座机号码
     * @return 验证结果
     */
    public static boolean isTelephone(String value) {
        String check = "^\\d{3,4}-?\\d{7,9}$";
        return PatternMatcher.match(check, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * 验证手机号码
     *
     * @param value 手机号码
     * @return 验证结果
     */
    public static boolean isPhone(String value) {
        String check = "^[1][0-9]{10}$";
        return PatternMatcher.match(check, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * 验证电话号码 包括移动电话和座机
     *
     * @param value 号码
     * @return 验证结果
     */
    public static boolean isPhoneOrTelephone(String value) {
        return PatternMatcher.isPhone(value) || PatternMatcher.isTelephone(value);
    }

    /**
     * 验证内容是否为英文字母、数字和下划线
     *
     * @param value 内容
     * @return 验证结果
     */
    public static boolean isGeneral(String value) {
        String regex = "^\\w+$";
        return PatternMatcher.match(regex, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * 验证内容是否为英文字母、数字和下划线
     *
     * @param value 内容
     * @param min   最小长度
     * @param max   最大长度
     * @return 验证结果
     */
    public static boolean isGeneral(String value, int min, int max) {
        String regex = "^\\w{" + min + "," + max + "}$";
        return PatternMatcher.match(regex, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * 验证生日
     *
     * @param value 生日
     * @return 验证结果
     */
    public static boolean isBirthday(String value) {
        String regex = "(\\d{4})(/|-|\\.)(\\d{1,2})(/|-|\\.)(\\d{1,2})$";

        if (PatternMatcher.match(regex, Pattern.CASE_INSENSITIVE, value)) {
            int year = Integer.parseInt(value.substring(0, 4));
            int month = Integer.parseInt(value.substring(5, 7));
            int day = Integer.parseInt(value.substring(8, 10));

            if (month < 1 || month > 12)
                return false;

            if (day < 1 || day > 31)
                return false;

            if ((month == 4 || month == 6 || month == 9 || month == 11) && day == 31)
                return false;

            if (month == 2) {
                boolean isLeap = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
                if (day > 29 || (day == 29 && !isLeap))
                    return false;
            }
            return true;
        }

        return false;
    }

    /**
     * 验证身份证号码
     *
     * @param value 身份证号码
     * @return 验证结果
     */
    public static boolean isIdentityCard(String value) {
        String regex = "(^\\d{15}$)|(^\\d{17}([0-9]|X)$)";
        return PatternMatcher.match(regex, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * 验证邮政编码
     *
     * @param value 邮政编码
     * @return 验证结果
     */
    public static boolean isZipCode(String value) {
        String regex = "^[0-9]{6}$";
        return PatternMatcher.match(regex, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * 验证货币
     *
     * @param value 货币
     * @return 验证结果
     */
    public static boolean isMoney(String value) {
        String regex = "^(\\d+(?:\\.\\d{1,2})?)$";
        return PatternMatcher.match(regex, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * 验证数字
     *
     * @param value 数字
     * @return 验证结果
     */
    public static boolean isNumber(String value) {
        String regex = "^(\\+|\\-)?\\d+$";
        return PatternMatcher.match(regex, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * 验证数字
     *
     * @param value 数字
     * @param min   长度
     * @param max   长度
     * @return 验证结果
     */
    public static boolean isNumber(String value, int min, int max) {
        String regex = "^(\\+|\\-)?\\d{" + min + "," + max + "}$";
        return PatternMatcher.match(regex, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * 验证正整数
     *
     * @param value 正整数
     * @return 验证结果
     */
    public static boolean isPositiveNumber(String value) {
        String regex = "^\\d+$";
        return PatternMatcher.match(regex, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * 验证正整数
     *
     * @param value 正整数
     * @param min   最小长度
     * @param max   最大长度
     * @return 验证结果
     */
    public static boolean isPositiveNumber(String value, int min, int max) {
        String regex = "^\\d{" + min + "," + max + "}$";
        return PatternMatcher.match(regex, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * 验证中文
     *
     * @param value 中文
     * @return 验证结果
     */
    public static boolean isChinese(String value) {
        String regex = "^[\\u2E80-\\u9FFF]+$";
        return PatternMatcher.match(regex, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * 验证中文
     *
     * @param value 中文
     * @param min   最小长度
     * @param max   最大长度
     * @return 验证结果
     */
    public static boolean isChinese(String value, int min, int max) {
        String regex = "^[\\u2E80-\\u9FFF]{" + min + "," + max + "}$";
        return PatternMatcher.match(regex, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * 验证内容是否为中文、英文字母、数字和下划线
     *
     * @param value 内容
     * @return 验证结果
     */
    public static boolean isString(String value) {
        String regex = "^[\\u0391-\\uFFE5\\w]+$";
        return PatternMatcher.match(regex, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * 验证内容是否为中文、英文字母、数字和下划线
     *
     * @param value 内容
     * @param min   最小长度
     * @param max   最大长度
     * @return 验证结果
     */
    public static boolean isString(String value, int min, int max) {
        String regex = "^[\\u0391-\\uFFE5\\w]{" + min + "," + max + "}$";
        return PatternMatcher.match(regex, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * 验证uuid
     *
     * @param value uuid
     * @return 验证结果
     */
    public static boolean isUUID(String value) {
        String regex = "^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$";
        return PatternMatcher.match(regex, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * 验证链接地址
     *
     * @param value 链接地址
     * @return 验证结果
     */
    public static boolean isUrl(String value) {
        String regex = "^((https?|ftp):\\/\\/)?(((([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(%[\\da-f]{2})|[!\\$&'\\(\\)\\*\\+,;=]|:)*@)?(((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]))|((([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.)+(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.?)(:\\d*)?)(\\/((([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(%[\\da-f]{2})|[!\\$&'\\(\\)\\*\\+,;=]|:|@)+(\\/(([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(%[\\da-f]{2})|[!\\$&'\\(\\)\\*\\+,;=]|:|@)*)*)?)?(\\?((([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(%[\\da-f]{2})|[!\\$&'\\(\\)\\*\\+,;=]|:|@)|[\\uE000-\\uF8FF]|\\/|\\?)*)?(\\#((([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(%[\\da-f]{2})|[!\\$&'\\(\\)\\*\\+,;=]|:|@)|\\/|\\?)*)?$";
        return PatternMatcher.match(regex, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * 验证时间格式
     *
     * @param value 时间
     * @return 验证结果
     */
    public static boolean isDateTime(String value) {
        String regex = "^(\\d{4})(/|-|\\.|年)(\\d{1,2})(/|-|\\.|月)(\\d{1,2})(日)?(\\s+\\d{1,2}(:|时)\\d{1,2}(:|分)?(\\d{1,2}(秒)?)?)?$";
        return PatternMatcher.match(regex, Pattern.CASE_INSENSITIVE, value);
    }

}
