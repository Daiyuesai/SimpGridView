package com.xiaozhanxiang.simplegridview;

import com.xiaozhanxiang.simplegridview.utils.leet.Solution;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * author: dai
 * date:2019/9/18
 */
public class SolutionTest {

    @Test
    public void longestPalindromeTest() {
        String s = Solution.longestPalindrome("cbbd");

        System.out.print("输出字符： " + s);
    }

    @Test
    public void zConvertTest() {
        String zConvert = Solution.convert("AB", 1);

        System.out.print("输出字符： " + zConvert);
    }

    @Test
    public void isMatchTest() {
        boolean match = Solution.isMatch("ab", ".*c");
        assertFalse(match);

    }

    @Test
    public void intToRomanTest() {
        String roman = Solution.intToRoman(3236);

        System.out.print("输出字符： " + roman);
    }

    @Test
    public void longestCommonPrefix(){
        String prefix = Solution.longestCommonPrefix(new String[]{"adadad", "adadbbbb", "adacedee", "adarvfeff"});
        System.out.print("输出字符： " + prefix);
    }

    @Test
    public void threeSumClosestTest() {
        int [] list = {-1,3,-2,4,-5};
        Solution solution = new Solution();
        int closest = solution.threeSumClosest(list, 4);
        assertEquals(closest,5);

    }

    @Test
    public void letterCombinationsTest() {
        Solution solution = new Solution();
        List<String> list = solution.letterCombinations("23");

        System.out.print("输出字符： " + list.toString());
    }

    @Test
    public void isValidTest() {
        Solution solution = new Solution();
        boolean valid = solution.isValid("()");
        assertTrue(valid);
    }

}
