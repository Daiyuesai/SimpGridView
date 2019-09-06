package com.xiaozhanxiang.simplegridview.utils.leet;

import java.util.HashMap;

/**
 * author: dai
 * date:2019/9/4
 */
public class Solution {

    public int lengthOfLongestSubstring(String s) {
        int maxlength = 0;
        int length = 0;
        char[] chars = s.toCharArray();
        char aChar ;
        HashMap<Character,Integer> temp = new HashMap<>();
        for (int i = 0; i < chars.length; i++) {
           aChar = chars[i];
           if (temp.containsKey(aChar)) {
               if (length > maxlength) {
                   maxlength = length;
               }
               Integer index = temp.get(aChar);
               temp.clear();
               length = i - index;
               for (int j = index + 1; j <= i; j++) {
                   temp.put(chars[j],j);
               }
           }else {
               temp.put(aChar,i);
               length ++;
           }
        }
        if (length > maxlength) {
            maxlength = length;
        }
        return maxlength;
    }


//    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
//
//
//    }
}
