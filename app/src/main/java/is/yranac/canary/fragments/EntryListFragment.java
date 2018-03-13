package is.yranac.canary.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.adapter.CalenderPagerAdapter;
import is.yranac.canary.adapter.EntryListAdapter;
import is.yranac.canary.contentproviders.CanaryEntryContentProvider;
import is.yranac.canary.databinding.FragmentTimelineOverlayBinding;
import is.yranac.canary.databinding.ListrowEntrySubscriptionFooterBinding;
import is.yranac.canary.interfaces.ListViewOnScrollListener;
import is.yranac.canary.messages.EntryTableUpdated;
import is.yranac.canary.messages.HideTextCopy;
import is.yranac.canary.messages.LocationTableUpdated;
import is.yranac.canary.messages.ServicePlanUpdated;
import is.yranac.canary.messages.tutorial.StartTimelineFilterTutorial;
import is.yranac.canary.model.entry.Entry;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.subscription.Subscription;
import is.yranac.canary.services.database.EntryDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.services.database.SubscriptionPlanDatabaseService;
import is.yranac.canary.ui.BaseActivity;
import is.yranac.canary.ui.EntryDetailActivity;
import is.yranac.canary.ui.SettingsFragmentStackActivity;
import is.yranac.canary.ui.WebActivity;
import is.yranac.canary.ui.views.CustomTimelineClock;
import is.yranac.canary.ui.views.TimelineGradient;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.AnimationHelper;
import is.yranac.canary.util.DateUtil;
import is.yranac.canary.util.DensityUtil;
import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.StringUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.TouchTimeUtil;
import is.yranac.canary.util.TutorialUtil;
import is.yranac.canary.util.UserUtils;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ZendeskUtil;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import static is.yranac.canary.adapter.EntryListAdapter.SUBMIT_FEEDBACK_CODE;
import static is.yranac.canary.util.TutorialUtil.TutorialType.TIMELINE_FILTER;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_ADD_MEMBERSHIP;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_CALENDAR_CLOSE;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_CALENDAR_OPEN;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_TIMELINE_SHOW_DETAILS;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_TIMELINE_TOGGLE;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_VIEW_HOME_HEALTH;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_VIEW_OFFLINE;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_VIEW_TIMELINE_FOOTER_CTA;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_CALENDAR;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_MEMBERSHIP;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_TIMELINE;
import static is.yranac.canary.util.ga.AnalyticsConstants.LEARN_MORE;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_CALENDAR_TIMELINE;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_TIMELINE;

public class EntryListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = "EntryListFragment";

    private static final int TIMELINE_LOADER = 0x02423;

    private int SHOWING_STATE = Entry.SHOWING_ALL;

    private StickyListHeadersListView entryListView;
    private EntryListAdapter entryAdapter;
    private ImageView emptyImageView;
    private TextView emptyTextView;
    private TextView emptyHeaderTextView;

    private ImageButton flaggedEntriesBtn;
    private ImageButton allEntriesBtn;
    private ImageButton armedModeEntries;

    private View calenderView;
    private View blackOverlay;
    private View calenderBackground;

    private ViewPager viewPager;
    private View headerView;

    private TextView headerTextCopy;
    private View triangleView;
    private TextView monthTextView;
    private String headerText;

    private CalenderPagerAdapter pagerAdapter;
    private TextView emptyBannerTextView;
    private RelativeLayout tutorialView;
    private FragmentTimelineOverlayBinding tutorialBinding;
    private ListrowEntrySubscriptionFooterBinding subscriptionFooterBinding;
    private Subscription subscription;
    private TimelineGradient timelineGradient;
    private SpannableStringBuilder bottomString1;
    private SpannableStringBuilder bottomString2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_entry_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        entryListView = (StickyListHeadersListView) view.findViewById(R.id.entry_list_view);
        entryListView.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
        entryListView.setOnItemClickListener(listClickListener);
        subscription = SubscriptionPlanDatabaseService.getServicePlanForCurrentLocation();

        final CustomTimelineClock clock = (CustomTimelineClock) view.findViewById(R.id.timeline_clock);
        timelineGradient = (TimelineGradient) view.findViewById(R.id.timeline_footer_background);
        timelineGradient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleAnalyticsHelper.trackEvent(CATEGORY_MEMBERSHIP, LEARN_MORE, PROPERTY_TIMELINE, null, 0, UserUtils.getLastViewedLocationId());
                BaseActivity baseActivity = (BaseActivity) getActivity();
                Intent i = new Intent(baseActivity, SettingsFragmentStackActivity.class);
                i.setAction(SettingsFragmentStackActivity.location_plan);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.scale_alpha_push);
            }
        });
        final TextView timelineTextView = (TextView) view.findViewById(R.id.entry_timeline_text);

        String string1 = getString(R.string.events_from_over_24, 24);
        String string2 = getString(R.string.see_events_24_hours, 24);

        String learnMore = getString(R.string.learn_more);

        bottomString1 = StringUtils.spannableStringBuilder(getContext(), string1, learnMore, "Gibson-Light.otf", "Gibson-SemiBold.otf", 15, 13);
        bottomString2 = StringUtils.spannableStringBuilder(getContext(), string2, learnMore, "Gibson-Light.otf", "Gibson-SemiBold.otf", 15, 13);
        timelineTextView.setText(bottomString1, TextView.BufferType.SPANNABLE);
        final int color1 = ContextCompat.getColor(getContext(), R.color.black);
        final int color2 = ContextCompat.getColor(getContext(), R.color.white);
        clock.setTime(0, color1);
        timelineGradient.setPostion(false);
        timelineTextView.setTextColor(color1);
        entryListView.setOnScrollListener(new ListViewOnScrollListener() {
            private int preLast;

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);

                final int lastItem = firstVisibleItem + visibleItemCount;

                if (lastItem == totalItemCount) {
                    if (preLast != lastItem) {
                        GoogleAnalyticsHelper.trackEvent(CATEGORY_MEMBERSHIP, ACTION_VIEW_TIMELINE_FOOTER_CTA, null, null, UserUtils.getLastViewedLocationId(), 0);
                        preLast = lastItem;
                    }
                }
            }

            @Override
            public void onScrollPosition(int lastY) {
                int firstPosition = entryListView.getLastVisiblePosition();

                if (firstPosition <= 0)
                    return;
                Cursor cursor = (Cursor) entryAdapter.getItem(firstPosition);
                if (cursor == null || cursor.getCount() <= firstPosition)
                    return;
                Entry entry = EntryDatabaseService.getEntryFromCursor(cursor);

                long time = new Date().getTime() - entry.startTime.getTime();
                if (time > TimeUnit.DAYS.toMillis(1)) {
                    timelineTextView.setText(bottomString2, TextView.BufferType.SPANNABLE);
                    timelineTextView.setTextColor(color2);
                    timelineGradient.setPostion(true);
                    clock.setTime(lastY, color2);
                } else {
                    timelineTextView.setText(bottomString1, TextView.BufferType.SPANNABLE);
                    timelineTextView.setTextColor(color1);
                    timelineGradient.setPostion(false);
                    clock.setTime(lastY, color1);
                }
            }
        });


        entryListView.setOnStickyHeaderOffsetChangedListener(new StickyListHeadersListView.OnStickyHeaderOffsetChangedListener() {
            @Override
            public void onStickyHeaderOffsetChanged(StickyListHeadersListView stickyListHeadersListView, View view, int i) {
                EntryListAdapter.HeaderViewHolder headerViewHolder = (EntryListAdapter.HeaderViewHolder) view.getTag();
                float offset = Integer.valueOf(i).floatValue();
                float height = Integer.valueOf(view.getHeight()).floatValue();
                headerText = headerViewHolder.dayTextView.getText().toString();
                if (headerTextCopy.getText().toString().equalsIgnoreCase(headerText)) {
                    triangleView.setAlpha((height - offset) / height);
                } else {
                    triangleView.setAlpha(offset / height);
                }
                if (offset == 0) {
                    headerTextCopy.setText(headerViewHolder.dayTextView.getText());
                    triangleView.animate().alpha(1.0f).setDuration(200).start();
                }
            }
        });


        monthTextView = (TextView) view.findViewById(R.id.month_label);
        viewPager = (ViewPager) view.findViewById(R.id.calender_view_pager);

        pagerAdapter = new CalenderPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        });

        calenderView = view.findViewById(R.id.calender_layout);

        viewPager.setCurrentItem(pagerAdapter.getCount() - 1);
        setupCalendarMonth(viewPager.getCurrentItem());

        blackOverlay = view.findViewById(R.id.black_overlay_view);
        calenderBackground = view.findViewById(R.id.calender_background);
        headerView = view.findViewById(R.id.reset_date_touch_view);
        headerTextCopy = (TextView) view.findViewById(R.id.header_text_view);
        triangleView = view.findViewById(R.id.triangle_view);
        View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Cursor cursor = entryAdapter.getCursor();
                if (cursor.moveToPosition(1)) {
                    Entry entry = EntryDatabaseService.getEntryFromCursor(cursor);
                    headerTextCopy.setText(DateUtil.dateToDay(getActivity(), entry.startTime));
                    triangleView.setAlpha(1.0f);
                    headerTextCopy.setVisibility(View.INVISIBLE);
                }
                entryListView.setSelection(0);
                return true;
            }
        };

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utils.isDemo()) {
                    AlertUtils.showGenericAlert(getActivity(), getString(R.string.jump_back_in_time), getString(R.string.jump_back_in_time_dsc));
                    return;
                }
                if (calenderBackground.getVisibility() == View.VISIBLE) {
                    hideMoreOptionsLayout();
                } else {
                    showMoreOptionsLayout();
                }
            }
        };
        headerTextCopy.setOnClickListener(onClickListener);
        headerTextCopy.setOnLongClickListener(onLongClickListener);
        headerView.setOnClickListener(onClickListener);
        headerView.setOnLongClickListener(onLongClickListener);
        tutorialView = (RelativeLayout) view.findViewById(R.id.tutorial_view);

        entryListView.getWrappedList().setPadding(0, 0, 0, DensityUtil.dip2px(getContext(), 96));
        entryListView.setClipToPadding(false);

        View emptyView = view.findViewById(R.id.entry_empty_view);

        emptyBannerTextView = (TextView) view.findViewById(R.id.empty_banner_text_view);

        entryListView.setEmptyView(emptyView);
        emptyImageView = (ImageView) view.findViewById(R.id.empty_image_view);
        emptyTextView = (TextView) view.findViewById(R.id.empty_text_view);
        emptyHeaderTextView = (TextView) view.findViewById(R.id.empty_header_text_view);

        flaggedEntriesBtn = (ImageButton) view.findViewById(R.id.flagged_entry_records_filter);
        allEntriesBtn = (ImageButton) view.findViewById(R.id.all_entry_records_filter);
        armedModeEntries = (ImageButton) view.findViewById(R.id.armed_entry_records_filter);

        flaggedEntriesBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TutorialUtil.getTutorialInProgress() == TIMELINE_FILTER)
                            return;

                        if (SHOWING_STATE != Entry.SHOWING_FLAGGED) {
                            headerText = null;
                            GoogleAnalyticsHelper.trackEvent(CATEGORY_TIMELINE, ACTION_TIMELINE_TOGGLE,
                                    null, null, UserUtils.getLastViewedLocationId(), 0);

                            SHOWING_STATE = Entry.SHOWING_FLAGGED;
                            refreshEntryLoader();
                            removeTutorial();
                        }
                    }
                });

        allEntriesBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TutorialUtil.getTutorialInProgress() == TIMELINE_FILTER)
                            return;

                        if (SHOWING_STATE != Entry.SHOWING_ALL) {
                            GoogleAnalyticsHelper.trackEvent(CATEGORY_TIMELINE, ACTION_TIMELINE_TOGGLE,
                                    null, null, UserUtils.getLastViewedLocationId(), 0);

                            headerText = null;
                            SHOWING_STATE = Entry.SHOWING_ALL;
                            refreshEntryLoader();
                            removeTutorial();
                        }
                    }
                });

        armedModeEntries.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SHOWING_STATE != Entry.SHOWING_AWAY_MODE) {
                            GoogleAnalyticsHelper.trackEvent(CATEGORY_TIMELINE, ACTION_TIMELINE_TOGGLE,
                                    null, null, UserUtils.getLastViewedLocationId(), 0);

                            headerText = null;
                            SHOWING_STATE = Entry.SHOWING_AWAY_MODE;
                            refreshEntryLoader();

                            if (TutorialUtil.getTutorialInProgress() == TIMELINE_FILTER)
                                TutorialUtil.finishTutorial(TIMELINE_FILTER);

                            removeTutorial();
                        }
                    }
                });

        subscriptionFooterBinding = DataBindingUtil.inflate(getLayoutInflater(savedInstanceState),
                R.layout.listrow_entry_subscription_footer, entryListView.getWrappedList(), false);
        subscriptionFooterBinding.backToTopButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    entryListView.setSelection(0);
                    Cursor cursor = entryAdapter.getCursor();
                    if (cursor.moveToPosition(1)) {
                        Entry entry = EntryDatabaseService.getEntryFromCursor(cursor);
                        headerTextCopy.setText(DateUtil.dateToDay(getActivity(), entry.startTime));
                        triangleView.setAlpha(1.0f);
                        headerTextCopy.setVisibility(View.INVISIBLE);
                    }
                    return true;
                }
                return false;
            }
        });

        subscriptionFooterBinding.addMembershipButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (TouchTimeUtil.dontAllowTouch())
                    return true;

                GoogleAnalyticsHelper.trackEvent(CATEGORY_MEMBERSHIP, ACTION_ADD_MEMBERSHIP, PROPERTY_TIMELINE, null, 0, UserUtils.getLastViewedLocationId());

                String url = Constants.autoLoginUrlWithPromoCodes(UserUtils.getLastViewedLocation(), getContext(), false);
                String title = getString(R.string.activate_membership);
                Intent intent = WebActivity.newInstance(getContext(), url, title, true);
                startActivity(intent);
                return false;
            }
        });

        setEmptyImage();

        // need to add a footerview before setting the adapter.  No panic because it will be removed after the cursor has loaded
        entryListView.addFooterView(subscriptionFooterBinding.getRoot());

        entryAdapter = new EntryListAdapter((is.yranac.canary.ui.BaseActivity) getActivity(), null, false);
        entryListView.setAdapter(entryAdapter);
        onLocationTableUpdated(null);
        refreshEntryLoader();
        onServicePlanUpdated(null);
    }

    private void removeTutorial() {
        if (tutorialBinding != null) {
            tutorialBinding = null;
            AnimationHelper.fadeViewOut(tutorialView, 200);
            AnimationHelper.viewGoneAfterDelay(tutorialView, 200);
        }
    }

    private void setupCalendarMonth(int position) {
        Calendar cal = DateUtil.getCalanderInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        cal.add(Calendar.DAY_OF_WEEK, 3);
        cal.add(Calendar.WEEK_OF_MONTH, position - (pagerAdapter.getCount() - 1));
        monthTextView.setText(DateUtil.convertDateToMonthYearDisplayFormat(cal.getTime()));
    }

    @Override
    public void onStart() {
        super.onStart();
        TinyMessageBus.register(this);
        refreshEntryLoader();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshEntryLoader();
    }

    @Subscribe
    public void startTutorial(StartTimelineFilterTutorial startTutorial) {

        tutorialView.removeAllViews();
        tutorialBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_timeline_overlay, tutorialView, true);
        tutorialBinding.setTutorialType(TIMELINE_FILTER);
        tutorialView.setVisibility(View.VISIBLE);
        AnimationHelper.fadeViewInAfterDelay(tutorialView, 500, 750);
        AnimationHelper.fadeViewIn(tutorialBinding.timelineTutorialArrow2, 400);
        int dp5 = DensityUtil.dip2px(getContext(), 5);
        AnimationHelper.startPulsing(tutorialBinding.timelineTutorialArrow2, true, dp5, 400);
    }

    @Override
    public void onStop() {
        super.onStop();
        TinyMessageBus.unregister(this);
    }


    private ListView.OnItemClickListener listClickListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (position == 0)
                return;


            if (entryAdapter == null)
                return;

            if (view.getId() == R.id.listview_footer_message) {
                return;
            }
            int type = entryAdapter.getItemViewType(position);

            if (type == SUBMIT_FEEDBACK_CODE) {
                return;
            }

            Entry entry = EntryDatabaseService.getEntryFromCursor((Cursor) entryAdapter.getItem(position));
            switch (entryAdapter.getItemViewType(position)) {
                case EntryListAdapter.ENTRY_TYPE_MOTION_CODE:
                    if (entry.hasDetailView()) {
                        GoogleAnalyticsHelper.trackEvent(CATEGORY_TIMELINE, ACTION_TIMELINE_SHOW_DETAILS,
                                null, null, UserUtils.getLastViewedLocationId(), entry.id);
                        Intent intent = new Intent(getActivity(), EntryDetailActivity.class);
                        intent.putExtra("entryId", entry.id);
                        intent.putExtra("position", -1);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.scale_alpha_push);
                    }
                    break;
                case EntryListAdapter.ENTRY_TYPE_HOMEHEALTH_CODE:
                    //need to get entry devices

                    GoogleAnalyticsHelper.trackEvent(CATEGORY_TIMELINE, ACTION_TIMELINE_SHOW_DETAILS,
                            null, null, UserUtils.getLastViewedLocationId(), entry.id);
                    if (entry.deviceUuids == null || entry.deviceUuids.size() == 0)
                        return;

                    String deviceUuid = entry.deviceUuids.get(0);
                    ((BaseActivity) getContext()).showHomehealthFragment(deviceUuid);
                    GoogleAnalyticsHelper.trackEntry(ACTION_VIEW_HOME_HEALTH, PROPERTY_TIMELINE, null, entry.getLocationId(), entry.id);

                    break;
                case EntryListAdapter.ENTRY_TYPE_SIMPLE_CODE:
                    if (entry.entryType.equalsIgnoreCase(Entry.ENTRY_TYPE_DISCONNECT)) {

                        GoogleAnalyticsHelper.trackEvent(CATEGORY_TIMELINE, ACTION_TIMELINE_SHOW_DETAILS,
                                null, null, UserUtils.getLastViewedLocationId(), entry.id);
                        GoogleAnalyticsHelper.trackEntry(ACTION_VIEW_OFFLINE, PROPERTY_TIMELINE, null, entry.getLocationId(), entry.id);

                        String url = getString(R.string.connectivity_url);
                        ZendeskUtil.loadHelpCenter(getContext(), url);
                    }
            }
        }
    };

    private void setButtonDrawables(int state) {
        switch (state) {
            case Entry.SHOWING_ALL:
                allEntriesBtn.setImageResource(R.drawable.tab_all_on);
                flaggedEntriesBtn.setImageResource(R.drawable.tab_saved_off);
                armedModeEntries.setImageResource(R.drawable.tab_armed_off);
                break;
            case Entry.SHOWING_FLAGGED:
                allEntriesBtn.setImageResource(R.drawable.tab_all_off);
                flaggedEntriesBtn.setImageResource(R.drawable.tab_saved_on);
                armedModeEntries.setImageResource(R.drawable.tab_armed_off);
                break;
            case Entry.SHOWING_AWAY_MODE:
                allEntriesBtn.setImageResource(R.drawable.tab_all_off);
                flaggedEntriesBtn.setImageResource(R.drawable.tab_saved_off);
                armedModeEntries.setImageResource(R.drawable.tab_armed_on);
                break;
        }
    }

    private void setEmptyImage() {
        emptyHeaderTextView.setText(R.string.today);
        if (SHOWING_STATE == Entry.SHOWING_FLAGGED) {
            emptyBannerTextView.setText(R.string.bookmark_events);
            emptyImageView.setImageResource(R.drawable.timeline_empty_save);
            emptyTextView.setText(R.string.tap_the_bookmark);

        } else if (SHOWING_STATE == Entry.SHOWING_AWAY_MODE) {
            emptyImageView.setImageResource(R.drawable.timeline_empty_list);
            emptyBannerTextView.setText(R.string.away_events);
            emptyTextView.setText(R.string.all_armed_show_here);
        } else {
            emptyImageView.setImageResource(R.drawable.timeline_empty_list);
            emptyBannerTextView.setText(R.string.timeline);
            emptyTextView.setText(R.string.video_all_show_here);
        }
    }


    @Subscribe
    public void hideTextCopy(HideTextCopy hideTextCopy) {

        headerView.setBackgroundColor(getResources().getColor(R.color.transparent));
        headerTextCopy.setVisibility(View.INVISIBLE);
        viewPager.setCurrentItem(pagerAdapter.getCount() - 1);
    }

    public void hideMoreOptionsLayout() {

        if (calenderView.getAnimation() == null || calenderView.getAnimation().hasEnded()) {
            if (calenderView.getVisibility() != View.GONE) {
                AnimationHelper.slideViewOutToTop(calenderView, 200, 1.0f, new AnimationHelper.AnimationCompletion() {
                    @Override
                    public void onComplete() {
                        calenderView.setVisibility(View.GONE);
                        TinyMessageBus.postDelayed(new HideTextCopy(), 100);

                        GoogleAnalyticsHelper.trackEvent(CATEGORY_CALENDAR, ACTION_CALENDAR_CLOSE,
                                PROPERTY_CALENDAR_TIMELINE, null, UserUtils.getLastViewedLocationId(), 0);
                    }
                });
            }
        }

        AnimationHelper.viewGoneAfterDelay(calenderBackground, 200);

        AnimationHelper.fadeViewOut(blackOverlay, 200);

        blackOverlay.setOnTouchListener(null);
    }

    private void showMoreOptionsLayout() {

        GoogleAnalyticsHelper.trackEvent(CATEGORY_CALENDAR, ACTION_CALENDAR_OPEN,
                PROPERTY_CALENDAR_TIMELINE, null, UserUtils.getLastViewedLocationId(), 0);
        calenderView.setVisibility(View.VISIBLE);
        headerView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        headerTextCopy.setVisibility(View.VISIBLE);
        AnimationHelper.slideViewInFromTop(calenderView, 200, 1.0f);
        AnimationHelper.viewVisibleAfterDelay(calenderBackground, 100);
        AnimationHelper.fadeFromAlphaToAlpha(blackOverlay, 0.0f, 1.0f, 200);


        blackOverlay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideMoreOptionsLayout();
                entryListView.onTouchEvent(event);
                return true;
            }
        });

    }

    public void refreshEntryLoader() {
        if (getContext() == null)
            return;

        LoaderManager loaderManager = getLoaderManager();

        Loader loader = loaderManager.getLoader(TIMELINE_LOADER);
        if (loader != null) {
            loader.forceLoad();
        }

        loaderManager.restartLoader(TIMELINE_LOADER, null, this);


        resetCalendar();
        onServicePlanUpdated(null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        int location = UserUtils.getLastViewedLocationId();

        String lastLocation = String.valueOf(location);
        String where;
        String whereArgs[] = null;
        Uri contentUri = CanaryEntryContentProvider.CONTENT_URI;
        String sortOrder;

        where = CanaryEntryContentProvider.COLUMN_LOCATION_ID + " == ?";
        sortOrder = CanaryEntryContentProvider.COLUMN_START_TIME + " DESC, " + CanaryEntryContentProvider.COLUMN_ID + " DESC";

        setButtonDrawables(SHOWING_STATE);
        switch (SHOWING_STATE) {
            case Entry.SHOWING_ALL:

                where += " AND ";
                where += CanaryEntryContentProvider.COLUMN_START_TIME + " >= ?";
                where += " AND ";
                where += CanaryEntryContentProvider.COLUMN_ENTRY_CALL_TYPE + " == ?";

                whereArgs = new String[]{lastLocation, String.valueOf(
                        SubscriptionPlanDatabaseService.oldestAllowableNonStarredSubscriptionDate(location)
                                .getTime()), String.valueOf(Entry.SHOWING_ALL)};
                break;

            case Entry.SHOWING_FLAGGED:

                where += " AND ";
                where += CanaryEntryContentProvider.COLUMN_STARRED;
                where += " == 1";
                where += " AND ";
                where += CanaryEntryContentProvider.COLUMN_ENTRY_CALL_TYPE + " != ?";
                whereArgs = new String[]{lastLocation, String.valueOf(Entry.DAILY_TIMELINE)};
                break;

            case Entry.SHOWING_AWAY_MODE:
                where += " AND ";
                where += " ( " + CanaryEntryContentProvider.COLUMN_LOCATION_MODE + " == ?";
                where += " OR ";
                where += CanaryEntryContentProvider.COLUMN_LOCATION_MODE + " == ? )";
                where += " AND ";
                where += CanaryEntryContentProvider.COLUMN_START_TIME + " >= ?";
                where += " AND ";
                where += CanaryEntryContentProvider.COLUMN_ENTRY_TYPE + " == ?";
                where += " AND ";
                where += CanaryEntryContentProvider.COLUMN_ENTRY_CALL_TYPE + " != ?";

                String awayMode = "away";
                String armedMode = "armed";

                whereArgs = new String[]{lastLocation, armedMode, awayMode, String.valueOf(
                        SubscriptionPlanDatabaseService.oldestAllowableNonStarredSubscriptionDate(location)
                                .getTime()), "motion", String.valueOf(Entry.DAILY_TIMELINE)};

                break;
            default:
                break;
        }

        return new CursorLoader(
                getActivity(), contentUri, null, where, whereArgs, sortOrder);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int lastLocation = UserUtils.getLastViewedLocationId();

        int sideMargin = (int) getResources().getDimension(R.dimen.standard_margin);
        int top = DensityUtil.dip2px(getContext(), 38);
        int bottom = DensityUtil.dip2px(getContext(), 27);
        if (!EntryDatabaseService.allValidRecordsAreInDatabase(lastLocation, SHOWING_STATE)) {
            subscriptionFooterBinding.progressView.setVisibility(View.VISIBLE);
            subscriptionFooterBinding.footerLayout.setVisibility(View.GONE);
            subscriptionFooterBinding.getRoot().setPadding(sideMargin, top, sideMargin, bottom);
        } else if (subscription != null && subscription.onTrial) {
            subscriptionFooterBinding.getRoot().setVisibility(View.GONE);
            subscriptionFooterBinding.getRoot().setPadding(0, -1 * subscriptionFooterBinding.getRoot().getHeight(), 0, 0);
        } else {
            subscriptionFooterBinding.progressView.setVisibility(View.GONE);
            subscriptionFooterBinding.footerLayout.setVisibility(View.VISIBLE);
            subscriptionFooterBinding.getRoot().setPadding(sideMargin, top, sideMargin, bottom);
        }

        MergeCursor mergeCursor;
        boolean showAmazonHeader = false;
        if (cursor.getCount() == 0) {
            mergeCursor = new MergeCursor(new Cursor[]{cursor});
            triangleView.setAlpha(1.0f);
            headerTextCopy.setText(R.string.today);
        } else {
            if (cursor.moveToFirst() && headerText == null) {
                Entry entry = EntryDatabaseService.getEntryFromCursor(cursor);
                headerTextCopy.setText(DateUtil.dateToDay(getActivity(), entry.startTime));
                triangleView.setAlpha(1.0f);
            } else {
                headerTextCopy.setText(headerText);
                triangleView.setAlpha(1.0f);
            }

            Location location = LocationDatabaseService.getLocationFromId(lastLocation);

            if ((location != null && location.createdAfterGrandfather())|| (subscription != null && subscription.hasMembership)) {
                showAmazonHeader = !PreferencesUtils.hasShownAmazonHeader(getContext());
                if (!showAmazonHeader) {
                    showAmazonHeader = !PreferencesUtils.hasShownAmazonEnd(getContext());
                }
            }


            MatrixCursor matrixCursor = new MatrixCursor(new String[]{"_id", "colName2"});
            matrixCursor.addRow(new Object[]{"1", "header_one"});

            if (showAmazonHeader) {
                matrixCursor.addRow(new Object[]{"2", "header_two"});
            }
            mergeCursor = new MergeCursor(new Cursor[]{matrixCursor, cursor});

        }


        entryAdapter.swapCursor(SHOWING_STATE, mergeCursor, showAmazonHeader);
        setEmptyImage();
        onLocationTableUpdated(null);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }


    @Subscribe
    public void onLocationTableUpdated(LocationTableUpdated message) {

        if (Utils.isDemo()) {
            subscriptionFooterBinding.addMembershipButton.setVisibility(View.GONE);
            subscriptionFooterBinding.subscriptionNameTextView.setText(R.string.service_plans);
            subscriptionFooterBinding.subscriptionDscTextView.setText(R.string.service_plans_dsc);
            entryListView.invalidate();
            return;
        }

        if (subscription != null && !subscription.hasMembership) {
            subscriptionFooterBinding.subscriptionNameTextView.setText(R.string.see_more_events_beyond);
            subscriptionFooterBinding.subscriptionDscTextView.setText(getString(R.string.with_membership_unlimited_access));
            subscriptionFooterBinding.addMembershipButton.setVisibility(View.VISIBLE);
        } else {
            subscriptionFooterBinding.subscriptionNameTextView.setText(R.string.you_reach_end_timeline);
            subscriptionFooterBinding.subscriptionDscTextView.setText(R.string.book_import_events_or_download);
            subscriptionFooterBinding.addMembershipButton.setVisibility(View.GONE);
        }
    }


    @Subscribe
    public void onEntryTableUpdated(EntryTableUpdated message) {
        if (message.someReturn && UserUtils.getLastViewedLocationId() == message.location) {
            refreshEntryLoader();
        }
    }

    @Subscribe
    public void onServicePlanUpdated(ServicePlanUpdated servicePlanUpdated) {
        subscription = SubscriptionPlanDatabaseService.getServicePlanForCurrentLocation();
        resetCalendar();

        if (subscription.onTrial) {
            timelineGradient.setVisibility(View.VISIBLE);
            entryListView.setPadding(0, 0, 0, DensityUtil.dip2px(getContext(), 96));
            entryListView.getWrappedList().setPadding(0, 0, 0, DensityUtil.dip2px(getContext(), 96));
            subscriptionFooterBinding.getRoot().setVisibility(View.GONE);
        } else {
            timelineGradient.setVisibility(View.GONE);
            entryListView.setPadding(0, 0, 0, 0);
            entryListView.getWrappedList().setPadding(0, 0, 0, 0);
            subscriptionFooterBinding.getRoot().setVisibility(View.VISIBLE);
        }
    }

    public void resetCalendar() {
        if (pagerAdapter != null && pagerAdapter.swapPlan()) {
            viewPager.invalidate();
            viewPager.setCurrentItem(pagerAdapter.getCount() - 1);
            setupCalendarMonth(viewPager.getCurrentItem());
        }
    }

    public void showBookmarks() {
        SHOWING_STATE = Entry.SHOWING_FLAGGED;

    }

    public void showAll() {
        if (SHOWING_STATE == Entry.SHOWING_ALL)
            return;

        SHOWING_STATE = Entry.SHOWING_ALL;
        refreshEntryLoader();
    }
}
