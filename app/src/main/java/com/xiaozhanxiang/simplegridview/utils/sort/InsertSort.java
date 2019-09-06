package com.xiaozhanxiang.simplegridview.utils.sort;

import java.util.Arrays;

/**
 * author: dai
 * date:2019/8/31
 * 插入排序
 */
public class InsertSort implements IArraySort {
    @Override
    public int[] sort(int[] sourceArray) {
        int [] arr = Arrays.copyOf(sourceArray,sourceArray.length);

        for (int i = 1; i < arr.length ; i++) {
            int temp = arr[i] ;// 记录要插入的数据
            int j = i;

            while (j > 0 && temp < arr[j -1]) {
                arr[j] = arr[j - 1];
                j --;
            }

            if (j != i) {
                arr[j]  = temp;
            }
        }
        return arr;
    }
}
