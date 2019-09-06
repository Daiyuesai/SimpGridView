package com.xiaozhanxiang.simplegridview.utils.sort;

import java.util.Arrays;

/**
 * author: dai
 * date:2019/8/31
 * 冒泡排序
 */
public class BubbleSort implements IArraySort {
    @Override
    public int[] sort(int[] sourceArray) {
        if (sourceArray == null ) return null;
        int [] arr = Arrays.copyOf(sourceArray,sourceArray.length);
        for (int i = 0; i < arr.length; i++) {
            boolean flag = false;  //标志位，如果一轮循环没有发生交换，则表明排序已完成
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr,j,j+1);
                    flag = true;
                }
            }

            if (!flag) {
                break;
            }
        }
        return arr;
    }


    public void swap(int[] arr,int i, int j){
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
