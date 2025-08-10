package com.jc.template.common.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.data.util.Pair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public final class DateUtils {

    public static final SimpleDateFormat SKC_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");
    public static final SimpleDateFormat REM_MAPPER_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat SDF_YYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");

    public static Date getCurrentDateAndTimeInUtc() {
        DateTime now = new DateTime(); // Gives the default time zone.
        DateTime dateTime = now.toDateTime(DateTimeZone.UTC); // Converting default zone to UTC
        return dateTime.toDate();
    }

    public static String getCurrentDateAndTimeInUtcStr() {
        return String.valueOf(getCurrentDateAndTimeInUtc());
    }

    /**
     * @param dateStr 2021-07-06T00:00:00
     */
    public static Date toDate(String dateStr) {
        if (dateStr == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            date = null;
        }
        return date;
    }

    public static Date toDateFromStr(String dateStr) throws ParseException {
        return SDF_YYY_MM_DD.parse(dateStr);
    }

    public static String findYesterday(SimpleDateFormat skcDateFormat) {
        Calendar cal = Calendar.getInstance();
        //subtracting a day
        cal.add(Calendar.DATE, -1);
        return skcDateFormat.format(new Date(cal.getTimeInMillis()));
    }

    public static String findToday(SimpleDateFormat skcDateFormat) {
        Calendar cal = Calendar.getInstance();
        //subtracting a day
        cal.add(Calendar.DATE, 0);
        return skcDateFormat.format(new Date(cal.getTimeInMillis()));
    }

    public static Date findYesterdayDate() {
        Calendar cal = Calendar.getInstance();
        //subtracting a day
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public static Date findTodayDate() {
        Calendar cal = Calendar.getInstance();
        //subtracting a day
        cal.add(Calendar.DATE, 0);
        return cal.getTime();
    }

    public static int getFirstDateOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static String getDatetimeInZone(ZoneOffset zone) {
        return ZonedDateTime.now(zone).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static String getTimeInZone(String date) {
        return date.split(" ")[1];
    }

    public static String getMonthName(int month) {
        String monthName = null;
        switch (month) {
        case 0:
            monthName = "Jan";
            break;
        case 1:
            monthName = "Feb";
            break;
        case 2:
            monthName = "Mar";
            break;
        case 3:
            monthName = "Apr";
            break;
        case 4:
            monthName = "May";
            break;
        case 5:
            monthName = "Jun";
            break;
        case 6:
            monthName = "Jul";
            break;
        case 7:
            monthName = "Aug";
            break;
        case 8:
            monthName = "Sep";
            break;
        case 9:
            monthName = "Oct";
            break;
        case 10:
            monthName = "Nov";
            break;
        case 11:
            monthName = "Dec";
            break;
        }

        return monthName;
    }

    public static String getInvyteDateWithoutTime(Date date) {
        Calendar activityDateCal = Calendar.getInstance();
        activityDateCal.setTime(date);
        String month = String.valueOf(activityDateCal.get(Calendar.MONTH) + 1);
        month = month.length() == 1 ? "0" + month : month;
        String day = String.valueOf(activityDateCal.get(Calendar.DAY_OF_MONTH));
        day = day.length() == 1 ? "0" + day : day;
        int year = activityDateCal.get(Calendar.YEAR);
        String dateStr = "" + year + "-" + month + "-" + day;

        return dateStr;
    }

    public static String getStartDateOfYear() {
        Calendar activityDateCal = Calendar.getInstance();
        int year = activityDateCal.get(Calendar.YEAR);
        return "" + year + "-01-01";
    }

    public static String getEndDateOfYear() {
        Calendar activityDateCal = Calendar.getInstance();
        int year = activityDateCal.get(Calendar.YEAR);
        return "" + year + "-12-31";
    }

    public static Pair<Date, Date> getLastMonthFirstAndLastDate() {
        Calendar aCalendar = Calendar.getInstance();
        // add -1 month to current month
        aCalendar.add(Calendar.MONTH, -1);
        // set DATE to 1, so first date of previous month
        aCalendar.set(Calendar.DATE, 1);
        Date firstDateOfPreviousMonth = aCalendar.getTime();
        // set actual maximum date of previous month
        aCalendar.set(Calendar.DATE, aCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        //read it
        Date lastDateOfPreviousMonth = aCalendar.getTime();
        return Pair.of(firstDateOfPreviousMonth, lastDateOfPreviousMonth);
    }

    public static Pair<Date, Date> getLast2MonthFirstAndLastDate() {
        Calendar aCalendar = Calendar.getInstance();
        // add -1 month to current month
        aCalendar.add(Calendar.MONTH, -2);
        // set DATE to 1, so first date of previous month
        aCalendar.set(Calendar.DATE, 1);
        Date firstDateOfPreviousMonth = aCalendar.getTime();

        //
        aCalendar = Calendar.getInstance();
        // add -1 month to current month
        aCalendar.add(Calendar.MONTH, -1);
        // set DATE to 1, so first date of previous month
        aCalendar.set(Calendar.DATE, 1);


        // set actual maximum date of previous month
        aCalendar.set(Calendar.DATE, aCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        //read it
        Date lastDateOfPreviousMonth = aCalendar.getTime();


        return Pair.of(firstDateOfPreviousMonth, lastDateOfPreviousMonth);
    }
}
