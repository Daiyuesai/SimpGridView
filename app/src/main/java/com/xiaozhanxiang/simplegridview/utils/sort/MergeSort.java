package com.xiaozhanxiang.simplegridview.utils.sort;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * author: dai
 * date:2019/9/4
 * 归并排序实现
 */
public class MergeSort implements IArraySort {
    @Override
    public int[] sort(int[] sourceArray) {
        int[] arr = Arrays.copyOf(sourceArray,sourceArray.length);
        if (arr.length < 2) {
            return arr;
        }
        int middle = (int) Math.floor(arr.length/2);
        int [] left = Arrays.copyOfRange(sourceArray,0,middle);
        int [] right = Arrays.copyOfRange(sourceArray,middle,arr.length);
        return merge(sort(left),sort(right));
    }


    private int [] merge(int[] leftArr,int [] rightArr){
        int[] arr = new int[leftArr.length + rightArr.length];
        int leftIndex = 0;
        int rightIndex = 0;

        for (int i = 0; i < arr.length; i++) {
            if (leftIndex >= leftArr.length) {
                arr[i] = rightArr[rightIndex];
                rightIndex++;
                continue;
            }

            if (rightIndex >= rightArr.length) {
                arr[i] = leftArr[leftIndex];
                leftIndex++;
                continue;
            }
            if (leftArr[leftIndex] > rightArr[rightIndex]) {
                arr[i] = rightArr[rightIndex];
                rightIndex++;
            }else {
                arr[i] = leftArr[leftIndex];
                leftIndex++;
            }
        }
        return arr;
    }
}
