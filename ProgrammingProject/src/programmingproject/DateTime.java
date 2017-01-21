package programmingproject;

/**
 *
 * @author Dan
 */
public class DateTime
{

    public static final int SECONDS_PER_WEEK = 604800;
    public static final int SECONDS_PER_DAY = 86400;
    public static final int SECONDS_PER_HOUR = 3600;
    public static final int DAYS_PER_WEEK = 7;
    public static final int[] DAYS_IN_MONTH =
    {
        31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
    };

    public static final int FIRST_WEEK_DAY = 2; //2014-01-01 = Wednesday

    public static final long[] SECONDS_TILL_MONTH_STARTS =
    {
        0, 2678400, 5097600, 7776000, 10368000,
        13046400, 15638400, 18316800, 20995200, 23587200, 26265600, 28857600
    };

    public static long dateTimeToSecs(String dateTime)
    {

        String[] dateTimeArr = dateTime.split(" ");
        String[] date = dateTimeArr[0].split("-");
        String[] time = dateTimeArr[1].split(":");

        int months = Integer.parseInt(date[1]);
        int days = Integer.parseInt(date[2]);
        int hours = Integer.parseInt(time[0]);
        int minutes = Integer.parseInt(time[1]);
        int seconds = Integer.parseInt(time[2]);

        long secondsTillMonth = SECONDS_TILL_MONTH_STARTS[months - 1];
        long secondsOfDays = (days - 1) * SECONDS_PER_DAY;
        long secondsSince2013 = secondsTillMonth + secondsOfDays + hours * SECONDS_PER_HOUR + minutes * 60 + seconds;

        return secondsSince2013;
    }

    public static String secsToDateTime(long secs)
    {

        String dateTime = "2013-";
        //finds month
        int i = 0;
        while (i < 12 && SECONDS_TILL_MONTH_STARTS[i] <= secs)
        {
            //	long secondsSoFar = SECONDS_TILL_MONTH_STARTS[i];
            i++;
        }
        dateTime += String.format("%02d", i);
        dateTime += "-";
        secs -= SECONDS_TILL_MONTH_STARTS[i - 1];

        //finds day
        i = 0;
        while (i * SECONDS_PER_DAY <= secs)
        {
            i++;
        }
        secs -= SECONDS_PER_DAY * (i - 1);
        dateTime += String.format("%02d", i);
        dateTime += " ";

        //finds hours
        i = 0;
        while (i * SECONDS_PER_HOUR <= secs)
        {
            i++;
        }
        secs -= SECONDS_PER_HOUR * (i - 1);
        dateTime += String.format("%02d", i - 1);
        dateTime += ":";

        //finds minutes
        i = 0;
        while (i * 60 <= secs)
        {
            i++;
        }
        secs -= 60 * (i - 1);
        dateTime += String.format("%02d", i - 1);
        dateTime += ":";

        dateTime += String.format("%02d", secs);

        return dateTime;
    }

    public static String secsToHourAndMinute(long secs)
    {

        String timeOfDay = "";
        //finds hours
        int i = 0;
        while (i * SECONDS_PER_HOUR <= secs)
        {
            i++;
        }
        secs -= SECONDS_PER_HOUR * (i - 1);
        timeOfDay += String.format("%02d", i - 1);
        timeOfDay += ":";

        //finds minutes
        i = 0;
        while (i * 60 <= secs)
        {
            i++;
        }
        secs -= 60 * (i - 1);
        timeOfDay += String.format("%02d", i - 1);
        timeOfDay += ":";

        timeOfDay += String.format("%02d", secs);

        return timeOfDay;
    }

}
