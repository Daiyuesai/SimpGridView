package com.xiaozhanxiang.simplegridview.utils.sort;

import java.util.Arrays;

/**
 * author: dai
 * date:2019/9/3
 * 希尔排序
 */
public class ShellSort implements IArraySort {
    @Override
    public int[] sort(int[] sourceArray) {
        int [] arr = Arrays.copyOf(sourceArray,sourceArray.length);
        int gap = 1;
        while (gap < arr.length) {
            gap = gap * 3 + 1;
        }
        //gap 大于数组长度 这次排序没有意义
        gap = (int) Math.floor(gap/3);

        while (gap >0){

            for (int i = gap; i < arr.length; i++) {
                int temp = arr[i];
                int j = i - gap;

                while (j >= 0  && temp < arr[j]) {
                    arr[j + gap] = arr[j];
                    j -= gap;
                }

                arr[j + gap] = temp;
            }
            gap = (int) Math.floor(gap/3);
        }

        return arr;
    }
}
