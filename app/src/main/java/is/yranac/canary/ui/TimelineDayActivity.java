package is.yranac.canary.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.adapter.CalenderPagerAdapter;
import is.yranac.canary.adapter.EntryTimelineDayAdapter;
import is.yranac.canary.databinding.FragmentTimelineGridBinding;
import is.yranac.canary.messages.PushReceived;
import is.yranac.canary.model.entry.Entry;
import is.yranac.canary.model.entry.EntryResponse;
import is.yranac.canary.model.subscription.Subscription;
import is.yranac.canary.model.thumbnail.Thumbnail;
import is.yranac.canary.services.api.EntryAPIService;
import is.yranac.canary.services.database.EntryDatabaseService;
import is.yranac.canary.services.database.SubscriptionPlanDatabaseService;
import is.yranac.canary.services.database.ThumbnailDatabaseService;
import is.yranac.canary.util.AnimationHelper;
import is.yranac.canary.util.DateUtil;
import is.yranac.canary.util.DensityUtil;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.PushUtils;
import is.yranac.canary.util.UserUtils;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import retrofit.RetrofitError;

import static is.yranac.canary.ui.EntryDetailActivity.ENTRY_ID;
import static is.yranac.canary.ui.EntryDetailActivity.PLAYING_THUMBNAIL_ID;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_CALENDAR_CLOSE;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_CALENDAR_OPEN;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_CALENDAR;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_CALENDAR_FILTER;

/**
 * Created by Schroeder on 7/3/15.
 */
public class TimelineDayActivity extends BaseActivity implements AdapterView.OnItemClickListener, ViewPager.OnPageChangeListener {


    private static final String LOG_TAG = "TimelineDayActivity";

    private GetEntries getEntries;
    private EntryTimelineDayAdapter adapter;

    private int width;
    private int rowPadding;


    private CalenderPagerAdapter pagerAdapter;
    private Subscription subscription;
    private FragmentTimelineGridBinding binding;

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        hideMoreOptionsLayout();

        boolean showSaved = getIntent().getBooleanExtra("show_saved", false);

        if (showSaved) {
            binding.headerTitleTextView.setText(R.string.before);
        } else {
            Date date = new Date(getIntent().getLongExtra("timeline_day", DateUtil.getCurrentTime().getTime()));
            binding.headerTitleTextView.setText(DateUtil.dateToDay(this, date));
        }
        if (adapter != null)
            adapter.clear();

        binding.loadingView.setVisibility(View.VISIBLE);
        binding.emptyViewNoEntries.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = FragmentTimelineGridBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        rowPadding = DensityUtil.dip2px(TimelineDayActivity.this, 36);

        boolean showSaved = getIntent().getBooleanExtra("show_saved", false);

        if (showSaved) {
            binding.headerTitleTextView.setText(R.string.before);
        } else {
            Date date = new Date(getIntent().getLongExtra("timeline_day", DateUtil.getCurrentTime().getTime()));
            binding.headerTitleTextView.setText(DateUtil.dateToDay(this, date));
        }

        binding.timelineGridView.setOnItemClickListener(this);

        binding.timelineGridView.setEmptyView(binding.emptyView);

        setUpZooming();

        pagerAdapter = new CalenderPagerAdapter(getSupportFragmentManager());
        binding.calenderViewPager.setAdapter(pagerAdapter);

        binding.calenderViewPager.addOnPageChangeListener(this);


        binding.calenderViewPager.setCurrentItem(pagerAdapter.getCount() - 1);
        setupCalendarMonth(binding.calenderViewPager.getCurrentItem());

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.calenderBackground.getVisibility() == View.VISIBLE) {
                    hideMoreOptionsLayout();
                } else {
                    showMoreOptionsLayout();
                }
            }
        };
        binding.header.setOnClickListener(onClickListener);

        subscription = SubscriptionPlanDatabaseService.getServicePlanForCurrentLocation();
    }


    private void showMoreOptionsLayout() {
        binding.calenderLayout.setVisibility(View.VISIBLE);
        AnimationHelper.slideViewInFromTop(binding.calenderLayout, 200, 1.0f);
        AnimationHelper.viewVisibleAfterDelay(binding.calenderBackground, 100);
        AnimationHelper.fadeFromAlphaToAlpha(binding.blackOverlayView, 0.0f, 1.0f, 200);


        GoogleAnalyticsHelper.trackEvent(CATEGORY_CALENDAR, ACTION_CALENDAR_OPEN,
                PROPERTY_CALENDAR_FILTER, null, UserUtils.getLastViewedLocationId(), 0);
        binding.blackOverlayView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideMoreOptionsLayout();
                binding.timelineGridView.onTouchEvent(event);
                return true;
            }
        });

    }

    public void hideMoreOptionsLayout() {
        AnimationHelper.slideViewOutToTop(binding.calenderLayout, 200, 1.0f, new AnimationHelper.AnimationCompletion() {
            @Override
            public void onComplete() {
                binding.calenderViewPager.setCurrentItem(pagerAdapter.getCount() - 1);
            }
        });

        GoogleAnalyticsHelper.trackEvent(CATEGORY_CALENDAR, ACTION_CALENDAR_CLOSE,
                PROPERTY_CALENDAR_FILTER, null, UserUtils.getLastViewedLocationId(), 0);
        AnimationHelper.viewGoneAfterDelay(
                binding.calenderBackground, 200);

        AnimationHelper.fadeFromAlphaToAlpha(binding.blackOverlayView, 1.0f, 0.0f, 200);

        binding.blackOverlayView.setOnTouchListener(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getEntries = new GetEntries();
        getEntries.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getEntries.cancel(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Entry entry = adapter.getItem(position);
        if (entry != null && entry.hasDetailView()) {
            Intent intent = new Intent(TimelineDayActivity.this, EntryDetailActivity.class);
            intent.putExtra(ENTRY_ID, entry.id);

            List<Thumbnail> thumbnails = entry.thumbnails;

            if (thumbnails != null && !thumbnails.isEmpty()) {
                Thumbnail thumbnail = thumbnails.get(0);
                intent.putExtra(PLAYING_THUMBNAIL_ID, thumbnail.id);

            }

            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.scale_alpha_push);

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setupCalendarMonth(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private void setupCalendarMonth(int position) {
        Calendar cal = DateUtil.getCalanderInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        cal.add(Calendar.DAY_OF_WEEK, 3);
        cal.add(Calendar.WEEK_OF_MONTH, position - (pagerAdapter.getCount() - 1));
        binding.monthLabel.setText(DateUtil.convertDateToMonthYearDisplayFormat(cal.getTime()));
    }

    private class GetEntries extends AsyncTask<Void, Void, List<Entry>> {

        @Override
        protected List<Entry> doInBackground(Void... params) {

            List<Entry> entries = checkEntries();

            List<Entry> entriesAndThumbnail = new ArrayList<>();
            for (Entry entry : entries) {
                List<Thumbnail> thumbnails = ThumbnailDatabaseService.getThumbnailsForEntry(entry.id);
                for (Thumbnail thumbnail : thumbnails) {
                    Entry cloneEntry = entry.clone();
                    cloneEntry.thumbnails = new ArrayList<>();
                    cloneEntry.thumbnails.add(thumbnail);
                    entriesAndThumbnail.add(cloneEntry);
                }
            }


            return entriesAndThumbnail;
        }

        @Override
        protected void onPostExecute(List<Entry> entries) {
            super.onPostExecute(entries);
            binding.loadingView.setVisibility(View.GONE);
            binding.emptyViewNoEntries.setVisibility(View.VISIBLE);

            width = binding.gridLayout.getWidth();
            adapter = new EntryTimelineDayAdapter(TimelineDayActivity.this, entries, getColumnHeight(2));
            binding.timelineGridView.setAdapter(adapter);
        }
    }

    private int getColumnHeight(int column) {
        float columnWidth = width / (float) column;
        return (int) (columnWidth * (250.0 / 445.0)) + rowPadding;
    }

    private List<Entry> checkEntries() {
        boolean showSaved = getIntent().getBooleanExtra("show_saved", false);

        if (showSaved) {
            return getSavedEntries();
        }

        Date date = new Date(getIntent().getLongExtra("timeline_day", DateUtil.getCurrentTime().getTime()));

        Calendar cal = DateUtil.getCalanderInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startDate = cal.getTime();


        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date endDate = cal.getTime();

        Calendar planCal = DateUtil.getCalanderInstance();

        if (EntryDatabaseService.allValidRecordsAreInDatabase(UserUtils.getLastViewedLocationId(), Entry.SHOWING_ALL))
            return setEntries(startDate, endDate);


        if (subscription != null) {
            int hours = subscription.currentServiceProfile.timeLineLength;
            planCal.add(Calendar.HOUR, -1 * hours);
            if (startDate.before(planCal.getTime()))
                startDate = planCal.getTime();

        }
        try {
            EntryResponse entryResponse = EntryAPIService.getEntriesBetweenDates(startDate, endDate, UserUtils.getLastViewedLocationId());
            EntryDatabaseService.checkAndUpdateEntries(entryResponse.entries, Entry.DAILY_TIMELINE);
        } catch (RetrofitError error) {
        }
        return setEntries(startDate, endDate);

    }

    private List<Entry> getSavedEntries() {
        Calendar calender = DateUtil.getCalanderInstance();
        if (subscription == null) {
            return new ArrayList<>();
        }

        calender.add(Calendar.HOUR, -subscription.currentServiceProfile.timeLineLength);

        if (EntryDatabaseService.allValidRecordsAreInDatabase(UserUtils.getLastViewedLocationId(), Entry.SHOWING_FLAGGED)) {
            return EntryDatabaseService.getFlaggedEntriesBeforeDate(calender.getTime(), UserUtils.getLastViewedLocationId());
        }
        try {
            EntryResponse entryResponse = EntryAPIService.getFlaggedEntryRecordsBeforeDate(UserUtils.getLastViewedLocationId(), calender.getTime());
            EntryDatabaseService.checkAndUpdateEntries(entryResponse.entries, Entry.DAILY_TIMELINE);
            return entryResponse.entries;
        } catch (RetrofitError error) {
            error.printStackTrace();
            return EntryDatabaseService.getFlaggedEntriesBeforeDate(calender.getTime(), UserUtils.getLastViewedLocationId());
        }
    }

    private List<Entry> setEntries(final Date startDate, final Date endDate) {

        return EntryDatabaseService.getEntriesBetweenDates(startDate, endDate, UserUtils.getLastViewedLocationId());

    }

    private boolean zooming = false;

    private void setUpZooming() {
        final ScaleGestureDetector scaleGestureDetector;
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        binding.timelineGridView.setOnTouchListener(new View.OnTouchListener() {
            private boolean zoomingGesture;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scaleGestureDetector.onTouchEvent(event);
                if (zooming)
                    zoomingGesture = true;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.i(LOG_TAG, String.valueOf(event.getAction()));
                    try {
                        return zoomingGesture;
                    } finally {
                        zoomingGesture = false;

                    }
                }

                return zoomingGesture;

            }
        });
    }

    private class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {
        float zoomLevel;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scale = detector.getScaleFactor();
            zoomLevel *= scale;
            if (zoomLevel > 1.5f) {
                zoomLevel = 1;
                int columns = binding.timelineGridView.getNumColumns();
                if (columns > 2) {
                    binding.timelineGridView.setNumColumns(columns - 1);

                    adapter.setRowHeight(getColumnHeight(columns - 1));
                }
                return false;
            } else if (zoomLevel < 0.75f) {
                zoomLevel = 1;
                int columns = binding.timelineGridView.getNumColumns();
                if (columns < 4) {
                    binding.timelineGridView.setNumColumns(columns + 1);
                    adapter.setRowHeight(getColumnHeight(columns + 1));
                }
                return false;
            }
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            zoomLevel = 1.0f;
            zooming = true;
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            zooming = false;

        }
    }

    @Override
    protected String getAnalyticsTag() {
        return "FilteredView";
    }

    @Subscribe
    public void onPushReceived(PushReceived pushReceived) {
        PushUtils.showPush(this, pushReceived);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.scale_alpha_pop, R.anim.slide_out_bottom);
    }
}
