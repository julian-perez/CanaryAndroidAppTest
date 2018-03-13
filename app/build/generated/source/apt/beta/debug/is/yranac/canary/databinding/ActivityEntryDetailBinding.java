package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityEntryDetailBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new android.databinding.ViewDataBinding.IncludedLayouts(54);
        sIncludes.setIncludes(2, 
            new String[] {"new_layout_video_playback"},
            new int[] {7},
            new int[] {R.layout.new_layout_video_playback});
        sIncludes.setIncludes(1, 
            new String[] {"layout_siren_light"},
            new int[] {8},
            new int[] {R.layout.layout_siren_light});
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.entry_scroll_view, 9);
        sViewsWithIds.put(R.id.scrolling_content, 10);
        sViewsWithIds.put(R.id.free_video_over, 11);
        sViewsWithIds.put(R.id.replay_btn, 12);
        sViewsWithIds.put(R.id.watch_live_btn, 13);
        sViewsWithIds.put(R.id.top_frame, 14);
        sViewsWithIds.put(R.id.date_timestamp_portrait, 15);
        sViewsWithIds.put(R.id.bottom_frame, 16);
        sViewsWithIds.put(R.id.video_scrub_controls, 17);
        sViewsWithIds.put(R.id.play_pause_btn, 18);
        sViewsWithIds.put(R.id.rewind_btn_landscape, 19);
        sViewsWithIds.put(R.id.video_seek_bar, 20);
        sViewsWithIds.put(R.id.date_timestamp_landscape, 21);
        sViewsWithIds.put(R.id.rewind_btn_portrait, 22);
        sViewsWithIds.put(R.id.zoom_text_view, 23);
        sViewsWithIds.put(R.id.circle_page_indicator, 24);
        sViewsWithIds.put(R.id.entry_duration_text_view, 25);
        sViewsWithIds.put(R.id.detail_entry_summary, 26);
        sViewsWithIds.put(R.id.tag_container, 27);
        sViewsWithIds.put(R.id.tag_icon_small, 28);
        sViewsWithIds.put(R.id.entry_labels, 29);
        sViewsWithIds.put(R.id.entry_actions, 30);
        sViewsWithIds.put(R.id.watch_live_icn, 31);
        sViewsWithIds.put(R.id.share_layout, 32);
        sViewsWithIds.put(R.id.share_icon, 33);
        sViewsWithIds.put(R.id.share_button_label, 34);
        sViewsWithIds.put(R.id.cv_layout, 35);
        sViewsWithIds.put(R.id.tag_icon, 36);
        sViewsWithIds.put(R.id.tag_button_label, 37);
        sViewsWithIds.put(R.id.save_layout, 38);
        sViewsWithIds.put(R.id.save_entry_icon, 39);
        sViewsWithIds.put(R.id.comment_container, 40);
        sViewsWithIds.put(R.id.post_edit_text, 41);
        sViewsWithIds.put(R.id.post_btn, 42);
        sViewsWithIds.put(R.id.black_overlay_view, 43);
        sViewsWithIds.put(R.id.tutorial_view, 44);
        sViewsWithIds.put(R.id.more_options_background, 45);
        sViewsWithIds.put(R.id.more_button_layout, 46);
        sViewsWithIds.put(R.id.export_video_option, 47);
        sViewsWithIds.put(R.id.export_video_option_icon, 48);
        sViewsWithIds.put(R.id.delete_entry_btn, 49);
        sViewsWithIds.put(R.id.report_issue, 50);
        sViewsWithIds.put(R.id.event_header_layout, 51);
        sViewsWithIds.put(R.id.header_title_text_view, 52);
        sViewsWithIds.put(R.id.more_options_btn, 53);
    }
    // views
    @NonNull
    public final android.view.View blackOverlayView;
    @Nullable
    public final is.yranac.canary.databinding.LayoutSirenLightBinding bottomBtnsLayout;
    @NonNull
    public final android.widget.FrameLayout bottomFrame;
    @NonNull
    public final is.yranac.canary.ui.views.viewpagerindicator.CirclePageIndicator circlePageIndicator;
    @NonNull
    public final android.widget.LinearLayout commentContainer;
    @NonNull
    public final android.widget.LinearLayout cvLayout;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus dateTimestampLandscape;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus dateTimestampPortrait;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus deleteEntryBtn;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus detailEntrySummary;
    @NonNull
    public final android.widget.LinearLayout entryActions;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus entryDurationTextView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus entryLabels;
    @NonNull
    public final is.yranac.canary.ui.views.CustomScrollView entryScrollView;
    @NonNull
    public final android.widget.FrameLayout eventHeaderLayout;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus exportVideoOption;
    @NonNull
    public final android.widget.ImageView exportVideoOptionIcon;
    @NonNull
    public final android.widget.RelativeLayout fragmentContainer;
    @NonNull
    public final android.widget.FrameLayout freeVideoOver;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus headerTitleTextView;
    @NonNull
    public final android.widget.LinearLayout landscapeHidden;
    @NonNull
    public final android.widget.RelativeLayout mainContentView;
    @NonNull
    private final android.widget.LinearLayout mboundView5;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus membershipCtaTextView;
    @NonNull
    public final android.widget.RelativeLayout moreButtonLayout;
    @NonNull
    public final android.view.View moreOptionsBackground;
    @NonNull
    public final android.widget.ImageView moreOptionsBtn;
    @NonNull
    public final android.widget.ImageButton playPauseBtn;
    @NonNull
    public final android.widget.FrameLayout playbackContainer;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus postBtn;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextPlus postEditText;
    @NonNull
    public final android.widget.LinearLayout replayBtn;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus reportIssue;
    @NonNull
    public final android.widget.RelativeLayout reportIssueLayout;
    @NonNull
    public final android.support.v7.widget.AppCompatImageView rewindBtnLandscape;
    @NonNull
    public final android.support.v7.widget.AppCompatImageView rewindBtnPortrait;
    @NonNull
    public final android.widget.ImageView saveEntryIcon;
    @NonNull
    public final android.widget.LinearLayout saveLayout;
    @NonNull
    public final android.widget.LinearLayout scrollingContent;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus shareButtonLabel;
    @NonNull
    public final android.widget.ImageView shareIcon;
    @NonNull
    public final android.widget.LinearLayout shareLayout;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus tagButtonLabel;
    @NonNull
    public final android.widget.LinearLayout tagContainer;
    @NonNull
    public final android.widget.ImageView tagIcon;
    @NonNull
    public final android.widget.ImageView tagIconSmall;
    @NonNull
    public final android.widget.FrameLayout topFrame;
    @NonNull
    public final android.widget.RelativeLayout tutorialView;
    @Nullable
    public final is.yranac.canary.databinding.NewLayoutVideoPlaybackBinding videoLayout;
    @NonNull
    public final android.widget.FrameLayout videoScrubControls;
    @NonNull
    public final is.yranac.canary.ui.widget.SeekbarWithIntervals videoSeekBar;
    @NonNull
    public final android.widget.LinearLayout watchLiveBtn;
    @NonNull
    public final android.widget.LinearLayout watchLiveIcn;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus zoomTextView;
    // variables
    @Nullable
    private is.yranac.canary.model.subscription.Subscription mSubscription;
    @Nullable
    private android.view.View mV;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityEntryDetailBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 2);
        final Object[] bindings = mapBindings(bindingComponent, root, 54, sIncludes, sViewsWithIds);
        this.blackOverlayView = (android.view.View) bindings[43];
        this.bottomBtnsLayout = (is.yranac.canary.databinding.LayoutSirenLightBinding) bindings[8];
        setContainedBinding(this.bottomBtnsLayout);
        this.bottomFrame = (android.widget.FrameLayout) bindings[16];
        this.circlePageIndicator = (is.yranac.canary.ui.views.viewpagerindicator.CirclePageIndicator) bindings[24];
        this.commentContainer = (android.widget.LinearLayout) bindings[40];
        this.cvLayout = (android.widget.LinearLayout) bindings[35];
        this.dateTimestampLandscape = (is.yranac.canary.ui.views.TextViewPlus) bindings[21];
        this.dateTimestampPortrait = (is.yranac.canary.ui.views.TextViewPlus) bindings[15];
        this.deleteEntryBtn = (is.yranac.canary.ui.views.TextViewPlus) bindings[49];
        this.detailEntrySummary = (is.yranac.canary.ui.views.TextViewPlus) bindings[26];
        this.entryActions = (android.widget.LinearLayout) bindings[30];
        this.entryDurationTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[25];
        this.entryLabels = (is.yranac.canary.ui.views.TextViewPlus) bindings[29];
        this.entryScrollView = (is.yranac.canary.ui.views.CustomScrollView) bindings[9];
        this.eventHeaderLayout = (android.widget.FrameLayout) bindings[51];
        this.exportVideoOption = (is.yranac.canary.ui.views.TextViewPlus) bindings[47];
        this.exportVideoOptionIcon = (android.widget.ImageView) bindings[48];
        this.fragmentContainer = (android.widget.RelativeLayout) bindings[0];
        this.fragmentContainer.setTag(null);
        this.freeVideoOver = (android.widget.FrameLayout) bindings[11];
        this.headerTitleTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[52];
        this.landscapeHidden = (android.widget.LinearLayout) bindings[4];
        this.landscapeHidden.setTag(null);
        this.mainContentView = (android.widget.RelativeLayout) bindings[1];
        this.mainContentView.setTag(null);
        this.mboundView5 = (android.widget.LinearLayout) bindings[5];
        this.mboundView5.setTag(null);
        this.membershipCtaTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[3];
        this.membershipCtaTextView.setTag(null);
        this.moreButtonLayout = (android.widget.RelativeLayout) bindings[46];
        this.moreOptionsBackground = (android.view.View) bindings[45];
        this.moreOptionsBtn = (android.widget.ImageView) bindings[53];
        this.playPauseBtn = (android.widget.ImageButton) bindings[18];
        this.playbackContainer = (android.widget.FrameLayout) bindings[2];
        this.playbackContainer.setTag(null);
        this.postBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[42];
        this.postEditText = (is.yranac.canary.ui.views.EditTextPlus) bindings[41];
        this.replayBtn = (android.widget.LinearLayout) bindings[12];
        this.reportIssue = (is.yranac.canary.ui.views.ButtonPlus) bindings[50];
        this.reportIssueLayout = (android.widget.RelativeLayout) bindings[6];
        this.reportIssueLayout.setTag(null);
        this.rewindBtnLandscape = (android.support.v7.widget.AppCompatImageView) bindings[19];
        this.rewindBtnPortrait = (android.support.v7.widget.AppCompatImageView) bindings[22];
        this.saveEntryIcon = (android.widget.ImageView) bindings[39];
        this.saveLayout = (android.widget.LinearLayout) bindings[38];
        this.scrollingContent = (android.widget.LinearLayout) bindings[10];
        this.shareButtonLabel = (is.yranac.canary.ui.views.TextViewPlus) bindings[34];
        this.shareIcon = (android.widget.ImageView) bindings[33];
        this.shareLayout = (android.widget.LinearLayout) bindings[32];
        this.tagButtonLabel = (is.yranac.canary.ui.views.TextViewPlus) bindings[37];
        this.tagContainer = (android.widget.LinearLayout) bindings[27];
        this.tagIcon = (android.widget.ImageView) bindings[36];
        this.tagIconSmall = (android.widget.ImageView) bindings[28];
        this.topFrame = (android.widget.FrameLayout) bindings[14];
        this.tutorialView = (android.widget.RelativeLayout) bindings[44];
        this.videoLayout = (is.yranac.canary.databinding.NewLayoutVideoPlaybackBinding) bindings[7];
        setContainedBinding(this.videoLayout);
        this.videoScrubControls = (android.widget.FrameLayout) bindings[17];
        this.videoSeekBar = (is.yranac.canary.ui.widget.SeekbarWithIntervals) bindings[20];
        this.watchLiveBtn = (android.widget.LinearLayout) bindings[13];
        this.watchLiveIcn = (android.widget.LinearLayout) bindings[31];
        this.zoomTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[23];
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x10L;
        }
        videoLayout.invalidateAll();
        bottomBtnsLayout.invalidateAll();
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        if (videoLayout.hasPendingBindings()) {
            return true;
        }
        if (bottomBtnsLayout.hasPendingBindings()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.subscription == variableId) {
            setSubscription((is.yranac.canary.model.subscription.Subscription) variable);
        }
        else if (BR.v == variableId) {
            setV((android.view.View) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setSubscription(@Nullable is.yranac.canary.model.subscription.Subscription Subscription) {
        this.mSubscription = Subscription;
        synchronized(this) {
            mDirtyFlags |= 0x4L;
        }
        notifyPropertyChanged(BR.subscription);
        super.requestRebind();
    }
    @Nullable
    public is.yranac.canary.model.subscription.Subscription getSubscription() {
        return mSubscription;
    }
    public void setV(@Nullable android.view.View V) {
        this.mV = V;
    }
    @Nullable
    public android.view.View getV() {
        return mV;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeBottomBtnsLayout((is.yranac.canary.databinding.LayoutSirenLightBinding) object, fieldId);
            case 1 :
                return onChangeVideoLayout((is.yranac.canary.databinding.NewLayoutVideoPlaybackBinding) object, fieldId);
        }
        return false;
    }
    private boolean onChangeBottomBtnsLayout(is.yranac.canary.databinding.LayoutSirenLightBinding BottomBtnsLayout, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVideoLayout(is.yranac.canary.databinding.NewLayoutVideoPlaybackBinding VideoLayout, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2L;
            }
            return true;
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        boolean subscriptionHaveFullVideo = false;
        is.yranac.canary.model.subscription.Subscription subscription = mSubscription;
        int subscriptionHaveFullVideoVGONEVVISIBLE = 0;

        if ((dirtyFlags & 0x14L) != 0) {



                if (subscription != null) {
                    // read subscription.haveFullVideo()
                    subscriptionHaveFullVideo = subscription.haveFullVideo();
                }
            if((dirtyFlags & 0x14L) != 0) {
                if(subscriptionHaveFullVideo) {
                        dirtyFlags |= 0x40L;
                }
                else {
                        dirtyFlags |= 0x20L;
                }
            }


                // read subscription.haveFullVideo() ? View.GONE : View.VISIBLE
                subscriptionHaveFullVideoVGONEVVISIBLE = ((subscriptionHaveFullVideo) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
        }
        // batch finished
        if ((dirtyFlags & 0x14L) != 0) {
            // api target 1

            this.membershipCtaTextView.setVisibility(subscriptionHaveFullVideoVGONEVVISIBLE);
        }
        executeBindingsOn(videoLayout);
        executeBindingsOn(bottomBtnsLayout);
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static ActivityEntryDetailBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ActivityEntryDetailBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<ActivityEntryDetailBinding>inflate(inflater, is.yranac.canary.R.layout.activity_entry_detail, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static ActivityEntryDetailBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ActivityEntryDetailBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.activity_entry_detail, null, false), bindingComponent);
    }
    @NonNull
    public static ActivityEntryDetailBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ActivityEntryDetailBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/activity_entry_detail_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new ActivityEntryDetailBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): bottomBtnsLayout
        flag 1 (0x2L): videoLayout
        flag 2 (0x3L): subscription
        flag 3 (0x4L): v
        flag 4 (0x5L): null
        flag 5 (0x6L): subscription.haveFullVideo() ? View.GONE : View.VISIBLE
        flag 6 (0x7L): subscription.haveFullVideo() ? View.GONE : View.VISIBLE
    flag mapping end*/
    //end
}