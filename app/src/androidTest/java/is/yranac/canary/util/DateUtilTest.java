package is.yranac.canary.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import is.yranac.canary.CanaryApplication;

import static is.yranac.canary.util.DateUtil.getCurrentTime;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by narendramanoharan on 5/24/16.
 */

public class DateUtilTest {


    @Test
    public void testGetCurrentTime() {

        Date date = getCurrentTime();
        assertTrue(date.equals(new Date()));
    }

    @Test
    public void testGetCalenderInstance() {

        Calendar calendar = DateUtil.getCalanderInstance();
        Assert.assertNotNull(calendar);
    }

    @Test
    public void testConvertingApiDateString() {

        String testDate = "2015-01-16T17:28:00";
        Date date = DateUtil.convertApiStringToDate(testDate);

        assertNotNull(date);

    }


    @Test
    public void testGetDay(){

        String testDate = "2015-01-16T17:10:00";
        Date date = DateUtil.convertApiStringToDate(testDate);
        long dateTime = DateUtil.findDayForDate(date);

        String testDate2 = "2015-01-16T17:28:00";
        Date date2 = DateUtil.convertApiStringToDate(testDate2);
        long date2Day = DateUtil.findDayForDate(date2);

        assertTrue(dateTime == date2Day);

    }

    @Test
    public void testIsToday() {

        long date = getCurrentTime().getTime();
        Boolean isToday = DateUtil.isToday(date);
        assertTrue(isToday);
    }

    @Test
    public void testDateToDay() throws Exception {

        Date date = new Date();
        String day = DateUtil.dateToDay(CanaryApplication.getContext(), date);
        Assert.assertNotNull(day);
    }

    @Test
    public void testUtcDateToDisplayStringSeconds() throws Exception {
        Date date = new Date();
        String dateUTC = DateUtil.utcDateToDisplayStringSeconds(date);
        Assert.assertNotNull(dateUTC);
    }

    @Test
    public void testConvertDateToHour() throws Exception {

        Date date = new Date();
        String hour = DateUtil.convertDateToHour(date);
        Assert.assertNotNull(hour);
    }
}
