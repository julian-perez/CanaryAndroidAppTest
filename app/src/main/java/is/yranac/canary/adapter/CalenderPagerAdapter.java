package is.yranac.canary.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Calendar;
import java.util.Date;

import is.yranac.canary.fragments.CalenderWeekFragment;
import is.yranac.canary.model.subscription.Subscription;
import is.yranac.canary.services.database.SubscriptionPlanDatabaseService;
import is.yranac.canary.util.DateUtil;

/**
 * Created by Schroeder on 7/2/15.
 */
public class CalenderPagerAdapter extends FragmentStatePagerAdapter {

    private static final String LOG_TAG = "CalenderPagerAdapter";
    private Subscription currentSubscription;
    private int count;

    public CalenderPagerAdapter(FragmentManager fm) {
        super(fm);

        swapPlan();
    }

    @Override
    public CalenderWeekFragment getItem(int position) {
        return CalenderWeekFragment.newInstance((getCount() - 1) - position,
                currentSubscription.currentServiceProfile.timeLineLength,
                position == 0);
    }

    @Override
    public int getCount() {


        return count;
    }

    public boolean swapPlan() {
        Subscription subscription = SubscriptionPlanDatabaseService.getServicePlanForCurrentLocation();
        if (subscription == null)
            return false;

        if (currentSubscription == null) {
            currentSubscription = subscription;
            notifyDataSetChanged();
            return false;

        }
        if (currentSubscription.hasMembership != subscription.hasMembership) {
            currentSubscription = subscription;
            notifyDataSetChanged();
            return true;
        }
        notifyDataSetChanged();
        return false;
    }

    public void notifyDataSetChanged() {
        if (currentSubscription == null) {
            count = 0;
            super.notifyDataSetChanged();
            return;

        }

        Calendar calendar = DateUtil.getCalanderInstance();


        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

        Date startOfWeek = calendar.getTime();

        Date now = DateUtil.getCurrentTime();

        //difference in milli between now and start of week

        long milliDiff = now.getTime() - startOfWeek.getTime() - (24 * 60 * 60 * 1000);

        //milli back in timeline

        double hoursBack = currentSubscription.currentServiceProfile.timeLineLength;

        double milliBack = hoursBack * 60 * 60 * 1000;


        if (milliBack < milliDiff) {
            count = 1;
            super.notifyDataSetChanged();
            return;
        }

        count = 1;

        while (milliBack > 0) {
            count++;
            milliBack -= (7 * 24 * 60 * 60 * 1000);
        }

        milliBack /= 24 * 60 * 60 * 1000;
        milliDiff /= 24 * 60 * 60 * 1000;
        if (Math.abs(milliDiff) + Math.abs(milliBack) == 7)
            count--;

        super.notifyDataSetChanged();
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
