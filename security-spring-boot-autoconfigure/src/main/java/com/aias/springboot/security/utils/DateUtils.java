package com.aias.springboot.security.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <b>
 *
 * <br>
 * <b>@ClassName:</b> DateUtils <br>
 * <b>@Date:</b> 2019/10/29  <br>
 *
 * @author <a> liuhy </a><br>
 */

public final class DateUtils {
    public static String FORMAT_SHORT = "yyyy-MM-dd";
    public static String FORMAT_MORE_SHORT = "yyyyMMdd";
    public static String FORMAT_LONG = "yyyy-MM-dd HH:mm:ss";
    public static String FORMAT_CAR_DATE = "yyyy-MM-dd HH:mm";
    public static String FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.S";
    public static String FORMAT_SHORT_CN = "yyyy年MM月dd";
    public static String FORMAT_LONG_CN = "yyyy年MM月dd日  HH时mm分ss秒";
    public static String FORMAT_FULL_CN = "yyyy年MM月dd日  HH时mm分ss秒SSS毫秒";
    public static String FORMAT_FULL_SIMPLE = "yyyyMMddHHmmss";
    public static String FORMAT_TIME_MINUTE = "yyyyMMddHHmm";
    public static String FORMAT_TIME_FULL = "HHmmss";
    public static String FORMAT_TIME_FULL_MILLI = "yyyyMMddHHmmssSSS";
    public static String DATA_PATTERN = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
    public static String ADDTIONAL_STR_BEGAIN = "2000-01-01 00:00:00";
    public static String ADDTIONAL_STR_END = "2000-01-01 23:59:59";

    public DateUtils() {
    }

    public static String getDatePattern() {
        return FORMAT_LONG;
    }

    public static String getCarDatePattern() {
        return FORMAT_CAR_DATE;
    }

    public static Date getDate() {
        return parse(getNow());
    }

    public static Date getDate(String format) {
        return parse(getNow(), format);
    }

    public static String getNow() {
        return format(new Date());
    }

    public static String getNow(String format) {
        return format(new Date(), format);
    }

    public static String format(Date date) {
        return format(date, getDatePattern());
    }

    public static String carDateFormat(Date date) {
        return format(date, getCarDatePattern());
    }

    public static String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }

        return returnValue;
    }

    public static Date parse(String strDate) {
        return parse(strDate, getDatePattern());
    }

    public static Date parseDate(String strDate) {
        return parse(strDate, getCarDatePattern());
    }

    public static Date parse(String strDate, String pattern) {
        if (StringUtil.isNullOrEmpty(strDate)) {
            return null;
        } else {
            SimpleDateFormat df = new SimpleDateFormat(pattern);

            try {
                return df.parse(strDate);
            } catch (ParseException var4) {
//                log.error("parse date error. date: " + strDate + " pattern:" + pattern);
                return null;
            }
        }
    }

    public static Date addMonth(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(2, n);
        return cal.getTime();
    }

    public static Date addYear(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(1, n);
        return cal.getTime();
    }

    public static Date addDay(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(5, n);
        return cal.getTime();
    }

    public static String addDay(Date date, int i, String format) {
        Date result = addDay(date, i);
        return format(result, format);
    }

    public static Date addHour(Date date, Double hour) {
        Long time = date.getTime();
        Double times = (double)time + hour * 60.0D * 60.0D * 1000.0D;
        return new Date(Math.round(times));
    }

    public static Date subtractionHour(Date date, Double hour) {
        Long time = date.getTime();
        Double times = (double)time - hour * 60.0D * 60.0D * 1000.0D;
        return new Date(Math.round(times));
    }

    public static Date addMinute(Date date, Double mins) {
        Long time = date.getTime();
        Double times = (double)time + mins * 60.0D * 1000.0D;
        return new Date(Math.round(times));
    }

    public static Date addMinute(Date date, int mins) {
        Long time = date.getTime();
        Double times = (double)time + Double.valueOf((double)mins) * 60.0D * 1000.0D;
        return new Date(Math.round(times));
    }

    public static Date addSecond(Date date, int second) {
        Long time = date.getTime();
        Double times = (double)time + Double.valueOf((double)second) * 1000.0D;
        return new Date(Math.round(times));
    }

    public static String getTimeString() {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_FULL);
        Calendar calendar = Calendar.getInstance();
        return df.format(calendar.getTime());
    }

    public static String getYear(Date date) {
        return format(date).substring(0, 4);
    }

    public static String getShortDay(Date date) {
        return format(date, FORMAT_SHORT);
    }

    public static int countDays(String date) {
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(parse(date));
        long t1 = c.getTime().getTime();
        return (int)((t / 1000L - t1 / 1000L) / 3600L / 24L);
    }

    public static int countDays(String date, String format) {
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(parse(date, format));
        long t1 = c.getTime().getTime();
        return (int)((t / 1000L - t1 / 1000L) / 3600L / 24L);
    }

    public static int countDays(Date start, Date end) {
        long t1 = start.getTime();
        long t2 = end.getTime();
        return (int)((t2 / 1000L - t1 / 1000L) / 3600L / 24L);
    }

    public static int countMonth(String start, String end) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(parse(start));
        cal2.setTime(parse(end));
        if (cal1.after(cal2)) {
            Calendar temp = cal1;
            cal1 = cal2;
            cal2 = temp;
        }

        int year = Math.abs(cal2.get(1) - cal1.get(1));
        int month = cal2.get(2) - cal1.get(2);
        return year * 12 + month;
    }

    public static int countDaysIngoreMonthAndYear(String start, String end) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(parse(start));
        cal2.setTime(parse(end));
        int result = cal2.get(5) - cal1.get(5);
        return result;
    }

    public static int countDays(Date start, Date end, boolean isCeil) {
        if (!isCeil) {
            return countDays(start, end);
        } else {
            int mins = countMins(start, end);
            double hours = (double)mins / 60.0D / 24.0D;
            int intHours = (int)hours;
            if (hours > (double)intHours) {
                ++intHours;
            }

            return intHours;
        }
    }

    public static double countHour(Date start, Date end) {
        long t1 = start.getTime();
        long t2 = end.getTime();
        int hour = (int)((t2 / 1000L - t1 / 1000L) / 3600L);
        double halfHour = 0.0D;
        if (countMins(start, end) - hour * 60 > 30) {
            halfHour = 1.0D;
        } else if (countMins(start, end) - hour * 60 <= 30 && countMins(start, end) - hour * 60 > 0) {
            halfHour = 0.5D;
        }

        return (double)hour + halfHour;
    }

    public static int countMins(Date start, Date end) {
        start = parse(format(start, FORMAT_CAR_DATE) + ":00", FORMAT_LONG);
        end = parse(format(end, FORMAT_CAR_DATE) + ":00", FORMAT_LONG);
        long t1 = start.getTime();
        long t2 = end.getTime();
        return (int)((t2 - t1) / 1000L / 60L);
    }

    public static int countSeconds(Date start, Date end) {
        long t1 = start.getTime();
        long t2 = end.getTime();
        return (int)((t2 - t1) / 1000L);
    }

    public static Date getYester0Date() {
        return parse(format(addDay(new Date(), -1), FORMAT_SHORT), FORMAT_SHORT);
    }

    public static Date getYester24Date() {
        return parse(format(new Date(), FORMAT_SHORT), FORMAT_SHORT);
    }

    public static boolean isMoreThenMonth(Date startDate, Date enDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.add(2, 1);
        return enDate.getTime() - c.getTimeInMillis() >= 0L;
    }

    public static String countDateLength(Date endDate, Date startDate) {
        long diff = endDate.getTime() - startDate.getTime();
        long days = diff / 86400000L;
        long hours = (diff - days * 86400000L) / 3600000L;
        long minutes = (diff - days * 86400000L - hours * 3600000L) / 60000L;
        String hireTimeStr = days + "天" + hours + "小时" + minutes + "分";
        return hireTimeStr;
    }

    public static String countDateLength(String endDate, String startDate, String pattern) {
        return countDateLength(parse(endDate, pattern), parse(startDate, pattern));
    }

    public static int countHours(int minute, boolean isCeil) {
        int hours = 0;
        if (minute == 0) {
            return hours;
        } else {
            if (isCeil) {
                if (minute % 60 == 0) {
                    hours = minute / 60;
                } else {
                    hours = minute / 60 + 1;
                }
            } else {
                hours = minute / 60;
            }

            return hours;
        }
    }

    public static int countDays(int minute) {
        int days = 0;
        if (minute == 0) {
            return days;
        } else {
            if (minute % 1440 == 0) {
                days = minute / 1440;
            } else {
                days = minute / 1440 + 1;
            }

            return days;
        }
    }

    public static Date setSecondZero(Date date) {
        String time = format(date, "yyyyMMddHHmm");
        date = parse(time, "yyyyMMddHHmm");
        return date;
    }

    public static Date convertToYYMMDD(Date date) {
        String time = format(date, "yyyyMMdd");
        date = parse(time, "yyyyMMdd");
        return date;
    }

    public static boolean isDateFormat(String strDate) {
        if (strDate == null) {
            return Boolean.FALSE;
        } else {
            Pattern pattern = Pattern.compile(DATA_PATTERN);
            Matcher matcher = pattern.matcher(strDate);
            return matcher.matches();
        }
    }

    public static boolean isDateTimeFormat(String dateTime, String patternStr) {
        if (!StringUtil.isNullOrEmpty(dateTime) && !StringUtil.isNullOrEmpty(patternStr)) {
            SimpleDateFormat dfs = new SimpleDateFormat(patternStr);

            try {
                dfs.setLenient(false);
                dfs.parse(dateTime);
                return Boolean.TRUE;
            } catch (Exception var4) {
                return Boolean.FALSE;
            }
        } else {
            return Boolean.FALSE;
        }
    }

    public static String getLongFormatDateStr(String strDate, String additionalStr) {
        return strDate != null && strDate.trim().length() >= 10 && additionalStr != null && additionalStr.trim().length() == 19 ? strDate + additionalStr.substring(strDate.length()) : null;
    }

    public static Date getDateBefore(Date d, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.set(5, calendar.get(5) - day);
        return calendar.getTime();
    }

    public static Date getDateAfter(Date d, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.set(5, calendar.get(5) + day);
        return calendar.getTime();
    }

    public static String dateDiff(String startTime, String endTime, String format, String tag) {
        SimpleDateFormat sd = new SimpleDateFormat(format);
        long nd = 86400000L;
        long nh = 3600000L;
        long nm = 60000L;
        long diff = 0L;

        try {
            diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
        } catch (ParseException var20) {
//            log.error("日期转换异常");
            throw new RuntimeException();
        }

        long day = diff / nd;
        long hour = diff % nd / nh;
        long min = diff % nd % nh / nm;
        StringBuilder diffs = new StringBuilder();
        if (day != 0L) {
            diffs.append(day + "天");
        }

        if (hour != 0L) {
            diffs.append(hour + "小时");
        }

        if (min != 0L) {
            diffs.append(min + "分钟");
        }

        return diffs.toString();
    }

    public static int diffSecond(String mintime, String maxtime) throws ParseException {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date begin = dfs.parse(mintime);
        Date end = dfs.parse(maxtime);
        long between = (end.getTime() - begin.getTime()) / 1000L;
        return (new Long(between)).intValue();
    }

    public static Date toLastSecond(Date date) {
        String format = format(date, FORMAT_SHORT) + " 23:59:59";
        return parse(format, FORMAT_LONG);
    }
}