package com.smartfit.utils;

import android.content.Context;

import com.smartfit.R;
import com.smartfit.beans.CustomeDate;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yanchengdneg  on 2016/3/22.
 * 日期工具
 */
public class DateUtils {

    private static final ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>();
    private static final Object object = new Object();

    /**
     * 获取SimpleDateFormat
     *
     * @param pattern 日期格式
     * @return SimpleDateFormat对象
     * @throws RuntimeException 异常：非法日期格式
     */
    private static SimpleDateFormat getDateFormat(String pattern)
            throws RuntimeException {
        SimpleDateFormat dateFormat = threadLocal.get();
        if (dateFormat == null) {
            synchronized (object) {
                if (dateFormat == null) {
                    dateFormat = new SimpleDateFormat(pattern);
                    dateFormat.setLenient(false);
                    threadLocal.set(dateFormat);
                }
            }
        }
        dateFormat.applyPattern(pattern);
        return dateFormat;
    }

    /**
     * 获取日期字符串的日期风格。失敗返回null。
     *
     * @param date 日期字符串
     * @return 日期风格
     */
    public static DateStyle getDateStyle(String date) {
        DateStyle dateStyle = null;
        Map<Long, DateStyle> map = new HashMap<Long, DateStyle>();
        List<Long> timestamps = new ArrayList<Long>();
        for (DateStyle style : DateStyle.values()) {
            if (style.isShowOnly()) {
                continue;
            }
            Date dateTmp = null;
            if (date != null) {
                try {
                    ParsePosition pos = new ParsePosition(0);
                    dateTmp = getDateFormat(style.getValue()).parse(date, pos);
                    if (pos.getIndex() != date.length()) {
                        dateTmp = null;
                    }
                } catch (Exception e) {
                }
            }
            if (dateTmp != null) {
                timestamps.add(dateTmp.getTime());
                map.put(dateTmp.getTime(), style);
            }
        }
        Date accurateDate = getAccurateDate(timestamps);
        if (accurateDate != null) {
            dateStyle = map.get(accurateDate.getTime());
        }
        return dateStyle;
    }


    /**
     * 获取精确的日期
     *
     * @param timestamps 时间long集合
     * @return 日期
     */
    private static Date getAccurateDate(List<Long> timestamps) {
        Date date = null;
        long timestamp = 0;
        Map<Long, long[]> map = new HashMap<Long, long[]>();
        List<Long> absoluteValues = new ArrayList<Long>();

        if (timestamps != null && timestamps.size() > 0) {
            if (timestamps.size() > 1) {
                for (int i = 0; i < timestamps.size(); i++) {
                    for (int j = i + 1; j < timestamps.size(); j++) {
                        long absoluteValue = Math.abs(timestamps.get(i)
                                - timestamps.get(j));
                        absoluteValues.add(absoluteValue);
                        long[] timestampTmp = {timestamps.get(i),
                                timestamps.get(j)};
                        map.put(absoluteValue, timestampTmp);
                    }
                }

                // 有可能有相等的情况。如2012-11和2012-11-01。时间戳是相等的。此时minAbsoluteValue为0
                // 因此不能将minAbsoluteValue取默认值0
                long minAbsoluteValue = -1;
                if (!absoluteValues.isEmpty()) {
                    minAbsoluteValue = absoluteValues.get(0);
                    for (int i = 1; i < absoluteValues.size(); i++) {
                        if (minAbsoluteValue > absoluteValues.get(i)) {
                            minAbsoluteValue = absoluteValues.get(i);
                        }
                    }
                }

                if (minAbsoluteValue != -1) {
                    long[] timestampsLastTmp = map.get(minAbsoluteValue);

                    long dateOne = timestampsLastTmp[0];
                    long dateTwo = timestampsLastTmp[1];
                    if (absoluteValues.size() > 1) {
                        timestamp = Math.abs(dateOne) > Math.abs(dateTwo) ? dateOne
                                : dateTwo;
                    }
                }
            } else {
                timestamp = timestamps.get(0);
            }
        }

        if (timestamp != 0) {
            date = new Date(timestamp);
        }
        return date;
    }

    /**
     * 将日期字符串转化为日期。失败返回null。
     *
     * @param date 日期字符串
     * @return 日期
     */
    public static Date StringToDate(String date) {
        DateStyle dateStyle = getDateStyle(date);
        return StringToDate(date, dateStyle);
    }

    /**
     * 将日期字符串转化为日期。失败返回null。
     *
     * @param date    日期字符串
     * @param pattern 日期格式
     * @return 日期
     */
    public static Date StringToDate(String date, String pattern) {
        Date myDate = null;
        if (date != null) {
            try {
                myDate = getDateFormat(pattern).parse(date);
            } catch (Exception e) {
            }
        }
        return myDate;
    }

    /**
     * 将日期字符串转化为日期。失败返回null。
     *
     * @param date      日期字符串
     * @param dateStyle 日期风格
     * @return 日期
     */
    public static Date StringToDate(String date, DateStyle dateStyle) {
        Date myDate = null;
        if (dateStyle != null) {
            myDate = StringToDate(date, dateStyle.getValue());
        }
        return myDate;
    }

    /**
     * 将日期转化为日期字符串。失败返回null。
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return 日期字符串
     */
    public static String DateToString(Date date, String pattern) {
        String dateString = null;
        if (date != null) {
            try {
                dateString = getDateFormat(pattern).format(date);
            } catch (Exception e) {
            }
        }
        return dateString;
    }

    /**
     * 将日期转化为日期字符串。失败返回null。
     *
     * @param date      日期
     * @param dateStyle 日期风格
     * @return 日期字符串
     */
    public static String DateToString(Date date, DateStyle dateStyle) {
        String dateString = null;
        if (dateStyle != null) {
            dateString = DateToString(date, dateStyle.getValue());
        }
        return dateString;
    }

    /**
     * 获取日期中的某数值。如获取月份
     *
     * @param date     日期
     * @param dateType 日期格式
     * @return 数值
     */
    private static int getInteger(Date date, int dateType) {
        int num = 0;
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
            num = calendar.get(dateType);
        }
        return num;
    }

    /**
     * 获取日期的年份。失败返回0。
     *
     * @param date 日期字符串
     * @return 年份
     */
    public static int getYear(String date) {
        return getYear(StringToDate(date));
    }

    /**
     * 获取日期的年份。失败返回0。
     *
     * @param date 日期
     * @return 年份
     */
    public static int getYear(Date date) {
        return getInteger(date, Calendar.YEAR);
    }

    /**
     * 获取日期的月份。失败返回0。
     *
     * @param date 日期字符串
     * @return 月份
     */
    public static int getMonth(String date) {
        return getMonth(StringToDate(date));
    }

    /**
     * 获取日期的月份。失败返回0。
     *
     * @param date 日期
     * @return 月份
     */
    public static int getMonth(Date date) {
        return getInteger(date, Calendar.MONTH) + 1;
    }

    /**
     * 获取日期的天数。失败返回0。
     *
     * @param date 日期字符串
     * @return 天
     */
    public static int getDay(String date) {
        return getDay(StringToDate(date));
    }

    /**
     * 获取日期的天数。失败返回0。
     *
     * @param date 日期
     * @return 天
     */
    public static int getDay(Date date) {
        return getInteger(date, Calendar.DATE);
    }


    /****
     * 根据日期的第几天  换算出  日历中的星期（一二三四。。）
     * param context
     *
     * @param day
     * @return
     */
    public static String getDayOFWeek(Context context, int day) {
        String dayOfWeek = context.getString(R.string.saturday);
        switch (day) {
            case 7:
                dayOfWeek = context.getString(R.string.saturday);
                break;
            case 1:
                dayOfWeek = context.getString(R.string.sunday);
                break;
            case 2:
                dayOfWeek = context.getString(R.string.monday);
                break;
            case 3:
                dayOfWeek = context.getString(R.string.tuesday);
                break;
            case 4:
                dayOfWeek = context.getString(R.string.wednesday);
                break;
            case 5:
                dayOfWeek = context.getString(R.string.thursday);
                break;
            case 6:
                dayOfWeek = context.getString(R.string.friday);
                break;
        }
        return dayOfWeek;
    }


    public static int getIconOfWeek(int day) {
        int icon = R.mipmap.icon_1;
        switch (day) {
            case 1:
                icon = R.mipmap.icon_7;
                break;
            case 2:
                icon = R.mipmap.icon_1;
                break;
            case 3:
                icon = R.mipmap.icon_2;
                break;
            case 4:
                icon = R.mipmap.icon_3;
                break;
            case 5:
                icon = R.mipmap.icon_4;
                break;
            case 6:
                icon = R.mipmap.icon_5;
                break;
            case 7:
                icon = R.mipmap.icon_6;
                break;
        }
        return icon;
    }


    /***
     * 获取点击的日期图片
     *
     * @param day
     * @return
     */
    public static int getSelectIconOfWeek(int day) {
        int icon = R.mipmap.icon_1;
        switch (day) {
            case 1:
                icon = R.mipmap.icon_7_on;
                break;
            case 2:
                icon = R.mipmap.icon_1_on;
                break;
            case 3:
                icon = R.mipmap.icon_2_on;
                break;
            case 4:
                icon = R.mipmap.icon_3_on;
                break;
            case 5:
                icon = R.mipmap.icon_4_on;
                break;
            case 6:
                icon = R.mipmap.icon_5_on;
                break;
            case 7:
                icon = R.mipmap.icon_6_on;
                break;
        }
        return icon;
    }

    /**
     * 获取一周日期信息 ：日期    周几
     *
     * @return
     */
    public static List<CustomeDate> getWeekInfo() {
        List<CustomeDate> customeDates = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_WEEK, -1);
        // 今天是一周中的第几天
//        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

//        if (c.getFirstDayOfWeek() == Calendar.SUNDAY) {
//            c.add(Calendar.DAY_OF_MONTH, 1);
//        }
        // 计算一周开始的日期
//        c.add(Calendar.DAY_OF_MONTH, -dayOfWeek);

        for (int i = 0; i < 7; i++) {
            c.add(Calendar.DAY_OF_MONTH, 1);
            CustomeDate item = new CustomeDate();
            item.setDate(sdf.format(c.getTime()));
            item.setWeekday(c.get(Calendar.DAY_OF_WEEK));
            customeDates.add(item);
        }
        return customeDates;
    }


    public static String getData(String longSeconds) {

        Date date = new Date(Long.parseLong(longSeconds) * 1000);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return simpleDateFormat.format(date);

    }



    public static String getDataTime(String longSeconds) {

        Date date = new Date(Long.parseLong(longSeconds) * 1000);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(" HH:mm ");
        return simpleDateFormat.format(date);

    }

    public static long getTheDateMillions(String selectData) {

        Date currtent = StringToDate(selectData, DateStyle.YYYY_MM_DD);


        return currtent.getTime() / 1000;
    }
}
