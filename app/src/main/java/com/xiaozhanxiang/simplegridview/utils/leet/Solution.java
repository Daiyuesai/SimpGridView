package com.xiaozhanxiang.simplegridview.utils.leet;

import android.util.ArraySet;

import com.tencent.bugly.BuglyStrategy;
import com.xiaozhanxiang.simplegridview.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IllegalFormatCodePointException;
import java.util.List;

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

    /**
     * 寻找两个有序数列得中位数
     * @param A 有序数组A
     * @param B 有序数组B
     * @return  中位数
     */
    public double findMedianSortedArrays(int[] A, int[] B) {
        int m = A.length;
        int n = B.length;

        if (m > n ) { //交换数组 保证 m <= n
            int[] temp = A; A = B; B = temp;
            int t = m; m = n ; n = t;
        }
        int min = 0,max = m;
        int halfNumb = (m + n + 1)/2;
        int i ;
        int  j ;
        while (min <= max) {
            i = (min + max) /2;
            j = halfNumb - i;
           if (i < max && B[j - 1] > A[i] ) {
               min = i+1;
           }else if (i > 0 && A[i -1] > B[j]) {
               max = i-1;
           }else {
                int maxLeft = 0;
                if (i == 0){
                    maxLeft = B[j -1];
                }else if (j == 0){
                    maxLeft = A[i -1];
                }else {
                    maxLeft = Math.max(A[i -1],B[j -1]);
                }
                if (((m + n) & 1) != 0)return maxLeft;
                int minRight ;
                if (i == m )minRight = B[j];
                else if (j == n)minRight = A[i];
                else minRight = Math.min(A[i], B[j]);
                return (maxLeft + minRight)/2.0;
           }
        }
        return 0;
    }

    /**
     * 查找最长回文子字串
     * @param s
     * @return
     */
    public static String longestPalindrome(String s) {
        if (s == null || s.length() == 0)return "";
        int len1 , len2,len,start = 0,end = 0;
        for (int i = 0; i < s.length(); i++) {
            len1 = expandAroundCenter(s,i,i);
            len2 = expandAroundCenter(s, i ,i+1);
            len = Math.max(len1,len2);
            if (len > end - start) {
                start = i - (len -1)/2;
                end = i + len /2;
            }
        }
        return s.substring(start,end + 1);

    }


    private static int expandAroundCenter(String s,int left,int right) {
        int L = left, R = right;
        while (L >= 0 && R < s.length() && s.charAt(L) == s.charAt(R)) {
            L --;
            R ++;
        }
        return R -L -1;
    }

    /**
     * Z 字形变换 实现
     * @param s
     * @param numRows
     * @return
     */
    public  static  String convert(String s, int numRows) {
        if (s == null || s.length() <= 1 || numRows == 1) return s;

        int clom = s.length() ;
        char[] [] chars = new char[numRows][clom];
        int i = 0,j = 0;
        for (int k = 0; k < s.length(); k++) {

            chars[i][j] = s.charAt(k);
            if (i == numRows -1) {
                i--;
                j++;
            }else {
                if (j % (numRows - 1) == 0) {
                    i ++;
                }else {
                    i --;
                    j ++;
                }
            }
        }
        StringBuilder builder = new StringBuilder();
        char c;
        for (int k = 0; k < numRows; k++) {
            for (int l = 0; l < clom; l++) {
                 c = chars[k][l];
                 if (c != 0) {
                     builder.append(c);
                 }
            }
        }
        return builder.toString();
    }


    public int reverse(int x) {
        int rev = 0;
        int pop;
        while (x != 0) {
            pop = x%10;
            x /= 10;
            if (rev > Integer.MAX_VALUE/10 ||(rev == Integer.MAX_VALUE /10 && pop > Integer.MAX_VALUE %10))return 0;
            if (rev < Integer.MIN_VALUE /10 || (rev == Integer.MIN_VALUE /10  && pop < Integer.MIN_VALUE % 10)) return 0;
            rev = rev * 10 + pop;
        }

        return rev;
    }


    public boolean isPalindrome(int x) {
        if (x < 0)return false;
        int o = x;
        int rev = 0;
        int pop;
        while (x != 0) {
            pop = x%10;
            x /= 10;
            if (rev > Integer.MAX_VALUE/10 ||(rev == Integer.MAX_VALUE /10 && pop > Integer.MAX_VALUE %10))return false;
            rev = rev * 10 + pop;
        }
        return o == rev;
    }


    /**
     * 实现简单的正则匹配规则
     * @param s
     * @param p
     * @return
     */
    public static boolean isMatch(String s, String p) {
        if (p.isEmpty())return s.isEmpty();
        boolean firstMatch = (!s.isEmpty() && (s.charAt(0) == p.charAt(0) || p.charAt(0) == '.'));
        if (p.length() >= 2 && p.charAt(1) == '*') {
            return isMatch(s,p.substring(2)) || (firstMatch && isMatch(s.substring(1),p));
        }else {
            return firstMatch && isMatch(s.substring(1),p.substring(1));
        }
    }

    /**
     * 计算最大的区域
     * @param height
     * @return
     */
    public int maxArea(int[] height) {
        int l = 0, r = height.length -1, maxArea = 0;

        while (l < r) {
            maxArea = Math.max(maxArea,Math.min(height[l],height[r])*(r -l));
            if (height[l] > height[r]) {
                r --;
            }else {
                l++;
            }
        }
        return maxArea;
    }

    /**
     * 阿拉伯数组转罗马数字
     * @param num
     * @return
     */
    public static String intToRoman(int num) {
        String [] roman = {"M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"};
        int [] numbs  = {1000,900,500,400,100,90,50,40,10,9,5,4,1};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < roman.length; i++) {
            while (num >= numbs[i]){
                builder.append(roman[i]);
                num -= numbs[i];
            }
        }
        return builder.toString();
    }

    /**
     * 罗马数字转整型
     * @param s
     * @return
     */
    public int romanToInt(String s) {
        HashMap<Character,Integer> romanInt = new HashMap<>();
        romanInt.put('M',1000);
        romanInt.put('D',500);
        romanInt.put('C',100);
        romanInt.put('L',50);
        romanInt.put('X',10);
        romanInt.put('V',5);
        romanInt.put('I',1);
        int num = 0;
        int temp;
        for (int i = 0; i < s.length(); i++) {
            temp = romanInt.get(s.charAt(i));
            if ((i < s.length() - 1)  && (romanInt.get(s.charAt(i + 1)) >  temp)) {
                num -= temp;
            }else {
                num += temp;
            }
        }

        return num;
    }

    /**
     *
     * @param strs
     * @return
     */
    public static String longestCommonPrefix(String[] strs) {
        if (strs.length == 0)return "";
        StringBuilder builder = new StringBuilder();
        int index = 0;
        String str = strs[0];
        char c;
        while (true) {
            if (index >= str.length())return builder.toString();
            c = str.charAt(index);
            for (int i = 1; i < strs.length; i++) {
                if (index >= strs[i].length()) {
                    return builder.toString();
                }else {
                    if (strs[i].charAt(index) != c) return builder.toString();
                }
            }
            index++;
            builder.append(c);
        }
    }

    public List<List<Integer>> threeSum(int[] nums) {

        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length < 3)return result;

        Arrays.sort(nums);
        int L , R , sum;
        List<Integer> item;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > 0)return result;
            if (i > 0 && nums[i] == nums[i-1])continue; //去重
            L = i+ 1;
            R = nums.length - 1;
            while (L < R && nums[R] >= 0) {
                sum = nums[i] + nums[L] + nums[R];
                if (sum == 0) {
                    item = new ArrayList<>();
                    item.add(nums[i]);
                    item.add(nums[L]);
                    item.add(nums[R]);
                    result.add(item);
                    while (L < R && nums[L] == nums[L+1])L++;
                    while (R> L && nums[R] == nums[R - 1])R--;
                    L++;
                    R--;
                }else if (sum < 0) {
                    L ++;
                }else {
                    R --;
                }
            }
        }
        return result;

    }

    /**
     * 最接近的三数之和
     * @param nums
     * @param target
     * @return
     */
    public int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);
        int ans = nums[0] + nums[1]+ nums[2],sum , left,right;
        for (int i = 0; i < nums.length; i++) {
            left = i + 1;
            right = nums.length -1;
            while (left < right) {
                sum = nums[i] + nums[left] + nums[right];
                if (sum == target) {
                    return sum;
                }else if (sum < target) {
                    left ++;
                }else {
                    right --;
                }
                if (Math.abs(target - ans) > Math.abs(target - sum)) {
                    ans = sum;
                }
            }
        }
        return ans;
    }


    public List<String> letterCombinations(String digits) {
        List<String> ans = new ArrayList<>();
        if (digits.isEmpty())return ans;

        HashMap<Character,String> temp = new HashMap<>();
        temp.put('2',"abc");
        temp.put('3',"def");
        temp.put('4',"ghi");
        temp.put('5',"jkl");
        temp.put('6',"mno");
        temp.put('7',"pqrs");
        temp.put('8',"tuv");
        temp.put('9',"wxyz");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < digits.length(); i++) {
            list.add(temp.get(digits.charAt(i)));
        }
        List<String> prefix = new ArrayList<>();
        String builder ;
        for (int i = 0; i < list.get(0).length(); i++) {
            builder = String.valueOf(list.get(0).charAt(i));
            prefix.add(builder);
        }
        ans = getAns(prefix,list,1);
        return ans;

    }


    private List<String> getAns(List<String> prefix ,List<String> list, int i){
        if (i >= list.size())return prefix;
        String s = list.get(i);
        List<String> ans = new ArrayList<>();
        for (int j = 0; j < s.length(); j++) {
            for (int k = 0; k < prefix.size(); k++) {
                ans.add(prefix.get(k) + s.charAt(j) );
            }
        }
        return getAns(ans,list,i+1);
    }

    /**
     * 删除链表的倒数第N个节点
     * @param head
     * @param n
     * @return
     */
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0);  //添加一个哑节点处理边界情况
        dummy.next = head;
        ListNode firstNode = dummy;
        ListNode secondNode = dummy;

        for (int i = 0; i <= n; i++) {
            firstNode = firstNode.next;
        }

        while (firstNode != null) {
            firstNode = firstNode.next;
            secondNode = secondNode.next;
        }
        secondNode.next = secondNode.next.next;
        return dummy.next;
    }

    public boolean isValid(String s) {
        HashMap<Character,Character> temp = new HashMap<>();
        temp.put('{','}');
        temp.put('[',']');
        temp.put('(',')');
        List<Character> cacheList = new ArrayList<>();
        Character c;
        for (int i = 0; i < s.length(); i++) {
            c = temp.get(s.charAt(i));
            if (c == null) {
                c = s.charAt(i);
                if (cacheList.size() == 0)return false;
                Character character = cacheList.remove(cacheList.size() - 1);
                if (character != c){
                    System.out.print("character :" + character + "  c:" + c);
                    return false;
                }
            }else {
                cacheList.add(c);
            }
        }

        if (cacheList.size() >0) {
            return false;
        }
        return true;
    }



    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode result = null;
        ListNode head = null;

        while (l1 != null || l2 != null) {
            if (l1 == null) {
                if (result == null) {
                    result = l2;
                    head = result;
                }else {
                    result.next = l2;
                    result = result.next;
                }
                l2 = l2.next;
                continue;
            }

            if (l2 == null) {
                if (result == null) {
                    result = l1;
                    head = result;
                }else {
                    result.next = l1;
                    result = result.next;
                }
                l1 = l1.next;
                continue;
            }
            if (l1.val < l2.val) {
                if (result == null) {
                    result = l1;
                    head = result;
                }else {
                    result.next = l1;
                    result = result.next;
                }
                l1 = l1.next;
            }else {
                if (result == null) {
                    result = l2;
                    head = result;
                }else {
                    result.next = l2;
                    result = result.next;
                }
                l2 = l2.next;
            }
        }
        return head;
    }


//    public List<String> generateParenthesis(int n) {
//
//    }
//
//    private List<String>

}