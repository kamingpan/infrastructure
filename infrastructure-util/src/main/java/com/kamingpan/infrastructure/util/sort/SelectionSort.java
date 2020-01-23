package com.kamingpan.infrastructure.util.sort;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 选择排序
 *
 * @author kamingpan
 * @since 2018-03-02
 */
@Slf4j
public class SelectionSort {

    /**
     * 选择排序
     * 对于时间复杂度而言，选择排序序冒泡排序一样都需要遍历 n(n-1)/2 次,但是相对于冒泡排序来说每次遍历只需要交换一次元素，这对于计算机执行来说有一定的优化。但是选择排序也是名副其实的慢性子，即使是有序数组，也需要进行 n(n-1)/2 次比较，所以其时间复杂度为O(n²)。
     * 时间平均复杂度为O(n²)，选择排序空间复杂度为 O(1)
     *
     * @param list 排序数组
     * @param <T>  对象泛型
     */
    @SuppressWarnings("unchecked")
    public static <T extends Comparable> void sort(List<T> list) {
        // 获取排序数组长度
        int length = list.size();

        // 因为前面的都换过位置后，最后一位已经是最大值，无需再循环一次，因此 i < length - 1
        for (int i = 0; i < length - 1; i++) {
            // 每次和i对比，都是先用i的下一个，依次递增直最后一个值，因此 j = i + 1
            for (int j = i + 1; j < length; j++) {
                // 如果前值比后值大，则数据交换
                if (list.get(i).compareTo(list.get(j)) == 1) {
                    // 数据交换
                    Collections.swap(list, i, j);
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
        SelectionSort.sort(list);
        for (Integer value : list) {
            System.out.print(value + ", ");
        }
    }

}
