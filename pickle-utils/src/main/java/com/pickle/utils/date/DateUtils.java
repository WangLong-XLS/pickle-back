package com.pickle.utils.date;


import com.pickle.utils.constant.StringConstant;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DateUtils {
    public static final String DATE_FORMAT_YEAR = "yyyy";
    public static final String DATE_FORMAT_MONTH = "yyyy-MM";
    public static final String DATE_FORMAT_DAY = "yyyy-MM-dd";
    public static final String DATE_FORMAT_MINITE = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_SECOND = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_MILLISECOND = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DATE_FORMAT_CHINESE_MONTH = "yyyy年MM月";
    public static final String DATE_FORMAT_CHINESE = "yyyy年MM月dd日";
    public static final String DATE_FORMAT_CHINESE_SECONDE = "yyyy年MM月dd日 HH:mm:ss";
    public static final String DATE_FORMAT_CHINESE_WEEK_SECONDE = "yyyy年MM月dd日 E HH:mm:ss";
    public static final String DATE_FORMAT_ZONED_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    public static final String DATE_FORMAT_DAY_X = "yyyy/MM/dd";
    public static final String DATE_FORMAT_MONTH_X = "yyyy/MM";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
    public static final String STANDARD_DATE_FORMAT_UTC = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static final ZoneId ZONE_ID = ZoneId.systemDefault();

    /**
     * 日期格式化,date->string
     *
     * @param date    待转换的日期
     * @param pattern 转换后目标日期字符串格式
     * @return 日期字符串
     */
    public static String date2String(Date date, String pattern) {
        LocalDate localDate = date2LocalDate(date);
        return localDate.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 日期格式化,string->date
     *
     * @param dateString 待转换的日期字符串
     * @param pattern    待转换的日期字符串格式
     * @return 日期Date
     */
    public static Date string2Date(String dateString, String pattern) {
        LocalDate localDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern(pattern));
        return localDate2Date(localDate);
    }


    /**
     * 日期格式化，date->localDate
     *
     * @param date 转换前日期date
     * @return 转换后日期localDate
     */
    public static LocalDate date2LocalDate(Date date) {
        Instant instant = date.toInstant();
        return instant.atZone(ZONE_ID).toLocalDate();
    }

    /**
     * 日期格式化，localDate->date
     *
     * @param localDate 转换前日期localDate
     * @return 转换后日期date
     */
    public static Date localDate2Date(LocalDate localDate) {
        Instant instant = localDate.atStartOfDay().atZone(ZONE_ID).toInstant();
        return Date.from(instant);
    }

    /**
     * 日期格式化，date->localDateTime
     *
     * @param date 转换前日期date
     * @return 转换后日期localDateTime
     */
    public static LocalDateTime date2LocalDateTime(Date date) {
        Instant instant = date.toInstant();
        return instant.atZone(ZONE_ID).toLocalDateTime();
    }

    /**
     * 日期格式化，date->string,含时分秒
     *
     * @param date 转换前日期date
     * @return 转换后日期localDateTime
     */
    public static String date2StringTime(Date date, String pattern) {
        LocalDateTime localDateTime = date2LocalDateTime(date);
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 日期格式化，localDateTime->date
     *
     * @param localDateTime 转换前时间localDateTime
     * @return 转换后时间date
     */
    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZONE_ID).toInstant();
        return Date.from(instant);
    }

    /**
     * 获取传入日期所在年份第一天
     *
     * @param date 日期date
     * @return 当年第一天日期date
     */
    public static Date firstDayOfYear(Date date) {
        LocalDate localDate = date2LocalDate(date);
        return localDate2Date(localDate.with(TemporalAdjusters.firstDayOfYear()));
    }

    /**
     * 获取传入日期所在年份最后一天
     *
     * @param date 日期date
     * @return 当年最后一天日期date
     */
    public static Date lastDayOfYear(Date date) {
        LocalDate localDate = date2LocalDate(date);
        return localDate2Date(localDate.with(TemporalAdjusters.lastDayOfYear()));
    }

    /**
     * 获取传入日期所在月份第一天
     *
     * @param date 日期date
     * @return 当月第一天日期date
     */
    public static Date firstDayOfMonth(Date date) {
        LocalDate localDate = date2LocalDate(date);
        return localDate2Date(localDate.with(TemporalAdjusters.firstDayOfMonth()));
    }

    /**
     * 获取传入日期所在月份最后一天
     *
     * @param date 日期date
     * @return 当月最后一天日期date
     */
    public static Date lastDayOfMonth(Date date) {
        LocalDate localDate = date2LocalDate(date);
        return localDate2Date(localDate.with(TemporalAdjusters.lastDayOfMonth()));
    }

    /**
     * 获取传入日期所下个月份第一天
     *
     * @param date 日期date
     * @return 前日期所下个月份第一天日期date
     */
    public static Date firstDayOfNextMonth(Date date) {
        LocalDate localDate = date2LocalDate(date);
        return localDate2Date(localDate.with(TemporalAdjusters.firstDayOfNextMonth()));
    }

    /**
     * 获取传入日期的上个月 "yyyy-MM"
     *
     * @param date 日期date
     * @return lastMonth 上个月 "yyyy-MM"
     */
    public static String lastMonth(Date date) {
        LocalDate localDate = date2LocalDate(date);
        return localDate.plusMonths(-1).format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }

    /**
     * 获取传入日期的上年同月 "yyyy-MM"
     *
     * @param date 日期date
     * @return 上年同月
     * Same month of last year 上年同月 "yyyy-MM"
     */
    public static String sameMonthOfLastYear(Date date) {
        LocalDate localDate = date2LocalDate(date);
        return localDate.plusYears(-1).format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }

    public static String sameYearMonthDayOfLastYear(Date date) {
        LocalDate localDate = date2LocalDate(date);
        return localDate.plusYears(-1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    /**
     * 获取传入日期的偏移对应年度后的日期
     *
     * @param date 日期
     * @return year 偏移年
     */
    public static Date lastSomeYear(Date date, Integer year) {
        LocalDate localDate = date2LocalDate(date);
        return localDate2Date(localDate.plusYears(year));
    }

    /**
     * 获取传入日期的偏移对应月份后的日期
     *
     * @param date 日期
     * @return year 偏移月
     */
    public static Date lastSomeMonth(Date date, Integer month) {
        LocalDate localDate = date2LocalDate(date);
        return localDate2Date(localDate.plusMonths(month));
    }

    /**
     * 计算两个时间相差几个月(包括当月)
     *
     * @retrun int 整数
     */
    public static int differMonth(Date start, Date end) {
        long months = ChronoUnit.MONTHS.between(date2LocalDate(start), date2LocalDate(end));
        return (int) Math.abs(months) + 1;
    }

    /**
     * 计算两个时间相差几个月(包括当月)
     *
     * @retrun BigDecimal 数值
     */
    public static BigDecimal getMonthSpace(Date start, Date end) {
        return new BigDecimal(differMonth(start, end));
    }

    /**
     * 两个日期期间的所有月份
     */
    public static List<String> monthBetweenTwoDates(Date start, Date end) {
        List<String> result = new ArrayList<>();
        while (start.compareTo(end) < 1) {
            result.add(date2String(end, DATE_FORMAT_MONTH));
            end = string2Date(lastMonth(end) + "-01", DATE_FORMAT_DAY);
        }
        Collections.sort(result);
        return result;
    }

    //两个日期期间的所有月份,不考虑日
    public static List<String> monthBetweenAndDates(Date start, Date end) {
        start = firstDayOfMonth(start);
        end = lastDayOfMonth(end);
        return monthBetweenTwoDates(start, end);
    }

    /**
     * 获取给定日期的年份
     */
    public static String getYear(Date date) {
        LocalDate localDate = date2LocalDate(date);
        int year = localDate.getYear();
        return year + StringConstant.EMPTY;
    }

    /**
     * 获取给定日期的月份，小于10自动补零
     */
    public static String getMonth(Date date) {
        LocalDate localDate = date2LocalDate(date);
        int month = localDate.getMonthValue();
        if (month < 10) {
            return "0" + month;
        }
        return month + StringConstant.EMPTY;
    }

    /**
     * 获取日期整数 yyyy-MM-dd 00:00:00
     */
    public static Date getDate() {
        return localDate2Date(LocalDate.now());
    }

    /**
     * 获上月最后一天
     */
    public static Date getLastDayOfBeforeMonth(Date date) {
        LocalDate localDate = date2LocalDate(date);
        LocalDate lastMonth = localDate.plusMonths(-1);
        LocalDate lastDay = lastMonth.with(TemporalAdjusters.lastDayOfMonth());
        return localDate2Date(lastDay);
    }

    /**
     * 获取明天的日期
     */
    public static Date getTomorrow(Date date) {
        return getDateDiff(date, 1);
    }

    /**
     * 获取日期偏移天
     */
    public static Date getDateDiff(Date date, int days) {
        LocalDate localDate = date2LocalDate(date);
        LocalDate plusDate = localDate.plusDays(days);
        return localDate2Date(plusDate);
    }

    /**
     * 判断时间是否是周六或周日
     */
    public static boolean isWeekend(Date date) {
        LocalDate localDate = date2LocalDate(date);
        DayOfWeek week = localDate.getDayOfWeek();
        return week == DayOfWeek.SATURDAY || week == DayOfWeek.SUNDAY;
    }

    /**
     * 获取指定N个工作日后的日期
     *
     * @param from     传递的日期(开始日期)
     * @param workdays 指定工作日
     */
    public static Date getWorkDateDiff(Date from, int workdays) {
        int delay = 1;
        while (delay < workdays) {
            from = getTomorrow(from);
            //不是周末，时间需要加1
            if (!isWeekend(from) && delay < workdays) {
                delay++;
            }
        }
        return from;
    }

    /**
     * 获取两个日期相差几天
     * @param date1     减数
     * @param date2     被减数
     * @return  long
     */
    public static long daysBetween2Dates(Date date1,Date date2){
        return (date1.getTime() - date2.getTime())/(60*60*24*1000);
    }

    /**
     * 获取两个日期相差几个工作日
     * @param date1     减数(大)
     * @param date2     被减数（小）
     * @return  long
     */
    public static long workdaysBetween2Dates(Date date1,Date date2){
        if(date1.after(date2)){
            int delay = 0;
            while (date1.after(date2)) {
                // System.out.println("date2:"+date2String(date2,"yyyy-MM-dd")+","+date2String(date2,"yyyy-MM-dd")+",delay:"+delay);
                date2 = getTomorrow(date2);
                //不是周末，时间需要加1
                if (!isWeekend(date2) && date1.after(date2)) {
                    delay++;
                }
            }
            return delay;
        }else{
            return -1;
        }
    }


    /**
     * 判断当前系统时间是否为月初第一天
     */
    public static boolean isFirstDayOfMonth(){
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        return currentDate.get(ChronoField.DAY_OF_MONTH) == 1;
    }

    /**
     * 判断当前系统时间是否为年初第一天
     * @return
     */
    public static boolean isFirstDayOfYear(){
        LocalDate now = LocalDate.now();
        return now.getDayOfYear() == 1;
    }

    public static Date lastDayOfLastYear(){
        // 获取上一年的最后一天日期
        LocalDate lastDayOfLastYear = LocalDate.of( LocalDate.now().getYear() - 1, 12, 31);
        return localDate2Date(lastDayOfLastYear);
    }

    /**
     *  获取当前时间多少秒之后
     * @return
     */
    public static Date getSecondsLastTime(int seconds){
        // 获取当前的日期和时间
        LocalDateTime currentDateTime = LocalDateTime.now();

        LocalDateTime localDateTime = currentDateTime.plusSeconds(seconds);
        return DateUtils.localDateTime2Date(localDateTime);
    }
}
