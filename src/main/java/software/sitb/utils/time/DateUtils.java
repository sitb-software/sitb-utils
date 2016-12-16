package software.sitb.utils.time;

import java.util.Calendar;
import java.util.Date;

/**
 * Date Util
 *
 * @author 田尘殇Sean(sean.snow@live.com) createAt 2016/12/16
 */
public class DateUtils {


    /**
     * @return 一天的开始时间
     */
    public static Calendar dayStartTime() {
        return dayStartTime(null);
    }

    /**
     * @param calendar 时间信息
     * @return 一天的开始时间
     */
    public static Calendar dayStartTime(Calendar calendar) {
        if (null == calendar)
            calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }

    /**
     * @param calendar 时间
     * @return 一天的结束时间
     */
    public static Calendar dayEndTime(Calendar calendar) {
        if (null == calendar)
            calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar;
    }

    /**
     * 获取一周的第一天
     *
     * @return 本周第一天的时间信息
     */
    public static Calendar weekFirstDay() {
        return weekFirstDay(new Date());
    }

    /**
     * @param date 时间
     * @return 一周的第一天
     */
    public static Calendar weekFirstDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        dayStartTime(calendar);
        return calendar;
    }

}
