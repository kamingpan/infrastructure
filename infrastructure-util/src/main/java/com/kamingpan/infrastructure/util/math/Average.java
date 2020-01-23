package com.kamingpan.infrastructure.util.math;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 计算平均数工具
 *
 * @author kamingpan
 * @since 2018-10-13
 */
@Slf4j
public class Average {

    private static final int SCALE = 8;

    /**
     * 算数平均数
     * An = (a1 + a2 + ... + an) / n
     *
     * @param bigDecimals 参数集合
     * @return 平均数
     */
    public static BigDecimal arithmeticMean(BigDecimal... bigDecimals) {
        // 判断数组是否为空，如果为空，则返回0
        if (null == bigDecimals || bigDecimals.length <= 0) {
            return BigDecimal.ZERO;
        }

        // 返回平均数
        return Average.arithmeticMean(Arrays.asList(bigDecimals));
    }

    /**
     * 算数平均数
     * An = (a1 + a2 + ... + an) / n
     *
     * @param bigDecimals 参数集合
     * @return 平均数
     */
    public static BigDecimal arithmeticMean(List<BigDecimal> bigDecimals) {
        // 定义结果为0
        BigDecimal result = BigDecimal.ZERO;

        // 判断数组是否为空，如果为空，则返回0
        if (null == bigDecimals || bigDecimals.isEmpty()) {
            return result;
        }

        // 遍历数组相加获得结果
        for (BigDecimal numericalValue : bigDecimals) {
            result = result.add(numericalValue);
        }

        // 除以数量，获得平均值
        return result.divide(BigDecimal.valueOf(bigDecimals.size()), Average.SCALE, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 几何平均数
     * Gn = (a1 * a2 * ... * an) ^ (1 / n)
     *
     * @param bigDecimals 参数集合
     * @return 平均数
     */
    public static BigDecimal geometricMean(BigDecimal... bigDecimals) {
        // 判断数组是否为空，如果为空，则返回0
        if (null == bigDecimals || bigDecimals.length <= 0) {
            return BigDecimal.ZERO;
        }

        // 返回平均数
        return Average.geometricMean(Arrays.asList(bigDecimals));
    }

    /**
     * 几何平均数
     * Gn = (a1 * a2 * ... * an) ^ (1 / n)
     *
     * @param bigDecimals 参数集合
     * @return 平均数
     */
    public static BigDecimal geometricMean(List<BigDecimal> bigDecimals) {
        // 判断数组是否为空，如果为空，则返回0
        if (null == bigDecimals || bigDecimals.isEmpty()) {
            return BigDecimal.ZERO;
        }

        // 定义结果为1
        BigDecimal result = BigDecimal.ONE;
        // 次方值（1 / 数量）
        BigDecimal power = result.divide(BigDecimal.valueOf(bigDecimals.size()), Average.SCALE, BigDecimal.ROUND_HALF_UP);

        // 遍历数组相乘获得结果
        for (BigDecimal numericalValue : bigDecimals) {
            result = result.multiply(numericalValue);
        }

        // 开“数量”次方，获得平均值
        return BigDecimal.valueOf(Math.pow(result.doubleValue(), power.doubleValue()));
    }

    /**
     * 调和平均数
     * Hn = n / (1/a1 + 1/a2 + ... + 1/an)
     *
     * @param bigDecimals 参数集合
     * @return 平均数
     */
    public static BigDecimal harmonicMean(BigDecimal... bigDecimals) {
        // 判断数组是否为空，如果为空，则返回0
        if (null == bigDecimals || bigDecimals.length <= 0) {
            return BigDecimal.ZERO;
        }

        // 返回平均数
        return Average.harmonicMean(Arrays.asList(bigDecimals));
    }

    /**
     * 调和平均数
     * Hn = n / (1/a1 + 1/a2 + ... + 1/an)
     *
     * @param bigDecimals 参数集合
     * @return 平均数
     */
    public static BigDecimal harmonicMean(List<BigDecimal> bigDecimals) {
        // 定义结果为0
        BigDecimal result = BigDecimal.ZERO;

        // 判断数组是否为空，如果为空，则返回0
        if (null == bigDecimals || bigDecimals.isEmpty()) {
            return BigDecimal.ZERO;
        }

        // 定义计算值为1
        BigDecimal one = BigDecimal.ONE;

        // 遍历数组，计算倒数并相加，获得倒数和
        for (BigDecimal numericalValue : bigDecimals) {
            result = result.add(one.divide(numericalValue, Average.SCALE, BigDecimal.ROUND_HALF_UP));
        }

        // 数量除以倒数和，获得平均值
        return BigDecimal.valueOf(bigDecimals.size()).divide(result, Average.SCALE, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 平方平均数
     * Qn = √ [(a1^2 + a2^2 + ... + an^2) / n]
     *
     * @param bigDecimals 参数集合
     * @return 平均数
     */
    public static BigDecimal squareMean(BigDecimal... bigDecimals) {
        // 判断数组是否为空，如果为空，则返回0
        if (null == bigDecimals || bigDecimals.length <= 0) {
            return BigDecimal.ZERO;
        }

        // 返回平均数
        return Average.squareMean(Arrays.asList(bigDecimals));
    }

    /**
     * 平方平均数
     * Qn = √ [(a1^2 + a2^2 + ... + an^2) / n]
     *
     * @param bigDecimals 参数集合
     * @return 平均数
     */
    public static BigDecimal squareMean(List<BigDecimal> bigDecimals) {
        // 定义结果为0
        BigDecimal result = BigDecimal.ZERO;

        // 判断数组是否为空，如果为空，则返回0
        if (null == bigDecimals || bigDecimals.isEmpty()) {
            return BigDecimal.ZERO;
        }

        // 遍历数组，计算平方并相加，获得平方和
        for (BigDecimal numericalValue : bigDecimals) {
            result = result.add(numericalValue.multiply(numericalValue));
        }

        // 除以数量
        result = result.divide(BigDecimal.valueOf(bigDecimals.size()), Average.SCALE, BigDecimal.ROUND_HALF_UP);

        // 计算平方根，获得平均值
        return BigDecimal.valueOf(Math.sqrt(result.doubleValue()));
    }

    /**
     * 加权平均数
     * Wn = (a1^2 + a2^2 + ... + an^2) / (a1 + a2 + ... + an) - 未知数据权重
     * Wn = (a1*w1 + a2*w2 + ... +an*wn) / (w1 + w2 + ... + wn) - 已知数据权重
     *
     * @param bigDecimals 参数集合
     * @return 平均数
     */
    public static BigDecimal weightedMean(BigDecimal... bigDecimals) {
        // 判断数组是否为空，如果为空，则返回0
        if (null == bigDecimals || bigDecimals.length <= 0) {
            return BigDecimal.ZERO;
        }

        // 返回平均数
        return Average.weightedMean(Arrays.asList(bigDecimals));
    }

    /**
     * 加权平均数
     * Wn = (a1^2 + a2^2 + ... + an^2) / (a1 + a2 + ... + an) - 未知数据权重
     * Wn = (a1*w1 + a2*w2 + ... +an*wn) / (w1 + w2 + ... + wn) - 已知数据权重
     *
     * @param bigDecimals 参数集合
     * @return 平均数
     */
    public static BigDecimal weightedMean(List<BigDecimal> bigDecimals) {
        // 定义结果为0
        BigDecimal result = BigDecimal.ZERO;

        // 判断数组是否为空，如果为空，则返回0
        if (null == bigDecimals || bigDecimals.isEmpty()) {
            return BigDecimal.ZERO;
        }

        // 定义和值
        BigDecimal and = BigDecimal.ZERO;

        // 遍历数组，计算平方并相加，获得平方和
        for (BigDecimal numericalValue : bigDecimals) {
            and = and.add(numericalValue);
            result = result.add(numericalValue.multiply(numericalValue));
        }

        // 除以和，获得平均值
        return result.divide(and, Average.SCALE, BigDecimal.ROUND_HALF_UP);
    }

    public static void main(String[] args) {
        BigDecimal[] arrays = new BigDecimal[10];
        Random random = new Random();

        for (int i = 0; i < arrays.length; i++) {
            double decimal = (double) random.nextInt(100) / 100;
            arrays[i] = new BigDecimal(random.nextInt(100) + decimal);
            System.out.print(arrays[i].toString() + ", ");
        }

        System.out.println();

        System.out.println("算数平均数：" + Average.arithmeticMean(arrays));
        System.out.println("几何平均数：" + Average.geometricMean(arrays));
        System.out.println("调和平均数：" + Average.harmonicMean(arrays));
        System.out.println("平方平均数：" + Average.squareMean(arrays));
        System.out.println("加权平均数：" + Average.weightedMean(arrays));
    }

}
