package com.kamingpan.infrastructure.util.sort;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 冒泡排序
 *
 * @author kamingpan
 * @since 2018-03-02
 */
@Slf4j
public class BubbleSort {

    /**
     * 冒泡排序
     * 对于长度为 n 的数组，冒泡排序需要经过 n(n-1)/2 次比较，最坏的情况下，即数组本身是倒序的情况下，需要经过 n(n-1)/2 次交换，因此
     * 冒泡排序的算法时间平均复杂度为O(n²)。空间复杂度为 O(1)
     *
     * @param list 排序数组
     * @param <T>  对象泛型
     */
    @SuppressWarnings("unchecked")
    public static <T extends Comparable> void sort(List<T> list) {
        // 获取排序数组长度
        int length = list.size();
        // 判断后续是否还需要排序
        boolean isSort;

        for (int i = 0; i < length - 1; i++) {
            // 赋值为不需要排序
            isSort = false;

            // 每次循环时，最后的已经排序过的值不再循环，因此 j < length - i
            for (int j = 1; j < length - i; j++) {
                // 如果前一个值比后一个值大，则数据交换
                if (list.get(j - 1).compareTo(list.get(j)) == 1) {
                    // 数据交换
                    Collections.swap(list, j - 1, j);
                    // 如果数据有交换，则置为后续需要排序
                    isSort = true;
                }
            }

            // 如果该次循环没有任何数据交换，则表示后续的数据已是正确排序状态，直接结束循环
            if (!isSort) {
                break;
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
        BubbleSort.sort(list);
        for (Integer value : list) {
            System.out.print(value + ", ");
        }
    }

}
