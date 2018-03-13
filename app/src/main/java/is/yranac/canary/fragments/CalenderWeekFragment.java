package is.yranac.canary.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import is.yranac.canary.R;
import is.yranac.canary.ui.TimelineDayActivity;
import is.yranac.canary.util.DateUtil;

/**
 * Created by Schroeder on 7/2/15.
 */

public class CalenderWeekFragment extends Fragment {

    private static final String LOG_TAG = "CalenderWeekFragment";

    private static final String WEEKS_BACK = "weeks_back";
    private static final String TIMELINE_LENGTH = "timeline_length";
    private static final String SHOW_BEFORE = "show_before";

    public static CalenderWeekFragment newInstance(int weeksBack, int timelineLenth, boolean showBefore) {
        Bundle bundle = new Bundle();
        bundle.putInt(WEEKS_BACK, weeksBack);
        bundle.putInt(TIMELINE_LENGTH, timelineLenth);
        bundle.putBoolean(SHOW_BEFORE, showBefore);
        CalenderWeekFragment calenderWeekFragment = new CalenderWeekFragment();
        calenderWeekFragment.setArguments(bundle);
        return calenderWeekFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calander_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Calendar cal = DateUtil.getCalanderInstance();
//        cal.setFirstDayOfWeek(Calendar.SUNDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

        int weekOffset = getArguments().getInt(WEEKS_BACK);

        int timelineLength = getArguments().getInt(TIMELINE_LENGTH);

        boolean showBefore = getArguments().getBoolean(SHOW_BEFORE);

        cal.add(Calendar.WEEK_OF_YEAR, -weekOffset);
        SimpleDateFormat postFormatter = new SimpleDateFormat("d", Locale.getDefault());

        for (int i = 0; i < 7; i++) {
            int dayId;
            int dateId;
            int clickableLayoutId;


            switch (i) {
                case 0:
                    dayId = R.id.day_one;
                    dateId = R.id.date_one;
                    clickableLayoutId = R.id.date_layout_one;
                    break;
                case 1:
                    dayId = R.id.day_two;
                    dateId = R.id.date_two;
                    clickableLayoutId = R.id.date_layout_two;
                    break;
                case 2:
                    dayId = R.id.day_three;
                    dateId = R.id.date_three;
                    clickableLayoutId = R.id.date_layout_three;
                    break;
                case 3:
                    dayId = R.id.day_four;
                    dateId = R.id.date_four;
                    clickableLayoutId = R.id.date_layout_four;
                    break;
                case 4:
                    dayId = R.id.day_five;
                    dateId = R.id.date_five;
                    clickableLayoutId = R.id.date_layout_five;
                    break;
                case 5:
                    dayId = R.id.day_six;
                    dateId = R.id.date_six;
                    clickableLayoutId = R.id.date_layout_six;
                    break;
                case 6:
                    dayId = R.id.day_seven;
                    dateId = R.id.date_seven;
                    clickableLayoutId = R.id.date_layout_seven;
                    break;
                default:
                    continue;

            }


            TextView dayTextView = (TextView) view.findViewById(dayId);
            TextView dateTextView = (TextView) view.findViewById(dateId);

            final Date date = cal.getTime();
            Date now = DateUtil.getCurrentTime();

            Calendar c = DateUtil.getCalanderInstance();
            c.setTime(now);
            c.add(Calendar.HOUR, -timelineLength);


            Calendar cal2 = DateUtil.getCalanderInstance();
            cal2.setTime(date);
            boolean sameDay = c.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    c.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

            if (i == 0 && showBefore) {
                dayTextView.setText(R.string.before);
                dateTextView.setText(DecimalFormatSymbols.getInstance().getInfinity());
            } else if (DateUtil.isToday(cal.getTime().getTime())) {
                dayTextView.setText(getString(R.string.today));
                dateTextView.setText(postFormatter.format(cal.getTime()));
            } else {
                dayTextView.setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
                dateTextView.setText(postFormatter.format(cal.getTime()));
            }


            View clickableLayout = view.findViewById(clickableLayoutId);
            if (i == 0 && showBefore) {
                dayTextView.setAlpha(1.0f);
                dateTextView.setAlpha(1.0f);
                clickableLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), TimelineDayActivity.class);
                        intent.putExtra("show_saved", true);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.scale_alpha_push);
                    }
                });
            } else if (date.after(now)) {
                dayTextView.setAlpha(0.3f);
                dateTextView.setAlpha(0.3f);
                clickableLayout.setOnClickListener(null);
            } else if (c.getTime().after(date) && !sameDay) {
                dayTextView.setAlpha(0.3f);
                dateTextView.setAlpha(0.3f);
                clickableLayout.setOnClickListener(null);
            } else {
                dayTextView.setAlpha(1.0f);
                dateTextView.setAlpha(1.0f);
                clickableLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), TimelineDayActivity.class);
                        intent.putExtra("timeline_day", date.getTime());
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.scale_alpha_push);
                    }
                });

            }
            cal.add(Calendar.DAY_OF_WEEK, 1);

        }
    }

}
