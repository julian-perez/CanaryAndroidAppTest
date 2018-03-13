package is.yranac.canary.util;

import android.content.Context;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Calendar;
import java.util.Date;

import is.yranac.canary.CanaryApplication;

import static is.yranac.canary.util.DateUtil.getCurrentTime;
import static org.mockito.Mockito.mock;

/**
 * Created by narendramanoharan on 7/20/16.
 */
public class DateUtilityTest {

    @Mock
    Date date;

    @Mock
    Context context;

    @Before
    public void setUp() throws Exception {
        date = mock(Date.class);
        context = mock(Context.class);
    }

    @Test
    public void testGetCurrentTime() {


        Date date = getCurrentTime();
        Assert.assertNotNull(date);
    }

    @Test
    public void testGetCalenderInstance() {

        Calendar calendar = DateUtil.getCalanderInstance();
        Assert.assertNotNull(calendar);
    }

    @Test
    public void testIsToday() {

        long date = getCurrentTime().getTime();
        Boolean isToday = DateUtil.isToday(date);
        Assert.assertTrue(isToday);
    }

    @Test
    public void testDateToDay() throws Exception {

        String day = DateUtil.dateToDay(CanaryApplication.getContext(), date);
        Assert.assertNotNull(day);
    }

    @Test
    public void testUtcDateToDisplayStringSeconds() throws Exception {

        String dateUTC = "5:45:55 AM";
        String dateUT = DateUtil.utcDateToDisplayStringSeconds(date);
        Assert.assertNotNull(dateUTC);
    }

    @Test
    public void testConvertDateToHour() throws Exception {

        String hour = DateUtil.convertDateToHour(date);
        Assert.assertNotNull(hour);
    }
}

