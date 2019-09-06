package com.xiaozhanxiang.simplegridview.utils;


import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    /**
     * 时间戳转换成日期格式字符串
     *
     * @param millseconds 精确到毫秒的字符串
     * @param format
     * @return
     */
    public static String timeStamp2Date(String millseconds, String format) {
        if (millseconds == null || millseconds.isEmpty() || millseconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(millseconds)));
    }
    /**
     * 时间戳转换成日期格式字符串
     *
     * @param millseconds 精确到毫秒的字符串
     * @param format
     * @return
     */
    public static String timeStamp2Date(long millseconds, String format) {

        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.format(new Date(System.currentTimeMillis()));
        return sdf.format(new Date(millseconds));
    }

    /**
     * 日期格式字符串转换成时间戳（精确到毫秒）
     *
     * @param dateStr 字符串日期
     * @param format  如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static long date2Time(String dateStr, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(dateStr).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }


    public static String changeFormat(String dateStr, String format,String toFormat) {

       return timeStamp2Date(date2Time(dateStr,format),toFormat);
    }

    /**
     * 取得当前时间戳（精确到毫秒）
     *
     * @return
     */
    public static long timeStamp() {
      return System.currentTimeMillis();
    }
    /**
     * 当前日期
     *
     * @return
     */
    public static String currentDate(String format) {
        Date day = new Date();
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(day);
    }

    /**
     * 计算time2减去time1的差值 差值只设置 几天 几个小时 或 几分钟
     * 根据差值返回多长之间前或多长时间后
     */
    public static String getDistanceTime(long time1, long time2) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long diff;

        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        if (day != 0) return day + "天" + hour + "小时" + min + "分钟" + sec + "秒";
        if (hour != 0) return hour + "小时" + min + "分钟" + sec + "秒";
        if (min != 0) return min + "分钟" + sec + "秒";
        if (sec != 0) return sec + "秒";
        return "0秒";
    }

    public static String changeDate(int year, int month, int day, String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date data = null;
        try {
            data = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.getMessage();
        }
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(data);
        rightNow.add(Calendar.YEAR, year);
        rightNow.add(Calendar.MONTH, month);
        rightNow.add(Calendar.DAY_OF_YEAR, day);
        Date dt = rightNow.getTime();
        return sdf.format(dt);
    }

    public static int getDateYear(long time){
        return getDateField(time,Calendar.YEAR);
    }

    public static int getDateMonth(long time) {
        return getDateField(time,Calendar.MONTH) + 1;
    }

    public static int getDateDayOfMonth(long time){
        return getDateField(time,Calendar.DAY_OF_MONTH);
    }


    public static int getDateField(long time,int field) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(new Date(time));
        return cl.get(field);
    }

    public static String formatDecNum(int num) {
        String numStr = "";
        if (num <= 9 && num >= 0) {
            numStr = "0" + String.valueOf(num);
        } else {
            numStr = String.valueOf(num);
        }
        return numStr;
    }


    /**
     * 加减月份
     * @param datetime
     * @param count  为负数是为减，正数为加
     * @return
     */
    public static String dateAddMonth(String datetime, String format , int count) {
        if (TextUtils.isEmpty(format)) {
            format = "yyyy-MM";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        cl.add(Calendar.MONTH, count);
        date = cl.getTime();
        return sdf.format(date);
    }

    public static long dateAddTime(long time,int field,int count) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(new Date(time));
        cl.add(field,count);
        return cl.getTime().getTime();
    }



    /**设置每个阶段时间*/
    private static final int seconds_of_1minute = 60;
    private static final int seconds_of_30minutes = 30 * 60;
    private static final int seconds_of_1hour = 60 * 60;
    private static final int seconds_of_1day = 24 * 60 * 60;
    private static final int seconds_of_15days = seconds_of_1day * 15;
    private static final int seconds_of_30days = seconds_of_1day * 30;
    private static final int seconds_of_6months = seconds_of_30days * 6;
    private static final int seconds_of_1year = seconds_of_30days * 12;


    /**
     * 格式化时间
     * @param mTime
     * @return
     */
    public static String getTimeRange(Long mTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        /**获取当前时间*/
        Date curDate = new Date(System.currentTimeMillis());
        String dataStrNew= sdf.format(curDate);
        Date startTime=null;
        try {
            /**将时间转化成Date*/
            curDate=sdf.parse(dataStrNew);
            startTime = new Date(mTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /**除以1000是为了转换成秒*/
        long   between=(curDate.getTime()- startTime.getTime())/1000;
        int   elapsedTime= (int) (between);
        if (elapsedTime < seconds_of_1minute) {
            return "刚刚";
        }
        if (elapsedTime < seconds_of_30minutes) {
            return elapsedTime / seconds_of_1minute + "分钟前";
        }
        if (elapsedTime < seconds_of_1hour) {
            return "半小时前";
        }
        if (elapsedTime < seconds_of_1day) {
            return elapsedTime / seconds_of_1hour + "小时前";
        }
        if (elapsedTime < seconds_of_15days) {
            return elapsedTime / seconds_of_1day + "天前";
        }
        if (elapsedTime < seconds_of_30days) {
            return "半个月前";
        }
        if (elapsedTime < seconds_of_6months) {
            return elapsedTime / seconds_of_30days + "月前";
        }
        if (elapsedTime < seconds_of_1year) {
            return "半年前";
        }
        if (elapsedTime >= seconds_of_1year) {
            return elapsedTime / seconds_of_1year + "年前";
        }
        return "";
    }



    /**
     * 获取某月的天数
     *
     * @param year  年
     * @param month 月
     * @return 某月的天数
     */
    public static int getMonthDaysCount(int year, int month) {
        int count = 0;
        //判断大月份
        if (month == 1 || month == 3 || month == 5 || month == 7
                || month == 8 || month == 10 || month == 12) {
            count = 31;
        }

        //判断小月
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            count = 30;
        }

        //判断平年与闰年
        if (month == 2) {
            if (isLeapYear(year)) {
                count = 29;
            } else {
                count = 28;
            }
        }
        return count;
    }


    /**
     * 是否是闰年
     *
     * @param year year
     * @return 是否是闰年
     */
   public static boolean isLeapYear(int year) {
        return ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
    }


}
