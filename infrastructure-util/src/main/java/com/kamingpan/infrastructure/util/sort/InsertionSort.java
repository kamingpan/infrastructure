package com.kamingpan.infrastructure.util.sort;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 插入排序
 *
 * @author kamingpan
 * @since 2018-03-02
 */
@Slf4j
public class InsertionSort {

    /**
     * 插入排序
     *
     * @param list 排序数组
     * @param <T>  对象泛型
     */
    @SuppressWarnings("unchecked")
    public static <T extends Comparable> void sort(List<T> list) {
        int length = list.size();

        // 第一个值没必要单独比较，因此直接用数组的第二个值开始，所以 i = 1
        for (int i = 1; i < length; i++) {
            // 设置内循环为从第一位到i为止，所以 j = i - 1
            for (int j = i - 1; j >= 0; j--) {
                T value = list.get(j + 1);
                // 如果前一个比后一个大，则交换，保证小的在前面，以完成排序
                if (value.compareTo(list.get(j)) == -1) {
                    Collections.swap(list, j, j + 1);
                }
            }
        }
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<Integer>();
        Random random = new Random();
        for (int i = 0; i < random.nextInt(20); i++) {
            Integer value = random.nextInt(100);
            list.add(value);
            System.out.print(value + ", ");
        }

        System.out.println();
        InsertionSort.sort(list);
        for (Integer value : list) {
            System.out.print(value + ", ");
        }
    }

}
