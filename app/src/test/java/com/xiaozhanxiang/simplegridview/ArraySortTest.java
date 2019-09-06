package com.xiaozhanxiang.simplegridview;

import android.util.Log;

import com.xiaozhanxiang.simplegridview.utils.leet.Solution;
import com.xiaozhanxiang.simplegridview.utils.sort.BubbleSort;
import com.xiaozhanxiang.simplegridview.utils.sort.IArraySort;
import com.xiaozhanxiang.simplegridview.utils.sort.InsertSort;
import com.xiaozhanxiang.simplegridview.utils.sort.MergeSort;
import com.xiaozhanxiang.simplegridview.utils.sort.ShellSort;

import org.junit.Test;

/**
 * author: dai
 * date:2019/8/31
 */
public class ArraySortTest {
    private static final String TAG = "ArraySortTest";
    @Test
    public void sort() {
        int [] array = {1,4,23,2,45,-12,-45,100,2,45,98,-76,-877,56};

        IArraySort sort = new MergeSort();

        int[] sortArray = sort.sort(array);
        StringBuilder buffer = new StringBuilder();
        for (int value : sortArray) {
            buffer.append(" ").append(value);
        }
        System.out.print("sort: " + buffer.toString());


        byte h = (byte) 0xff;
        byte l = 33;
        int hi = h;
        int li = l & 0xff;
        int data = (hi << 8) | li;

        System.out.print("sort: " + data);
    }



}
