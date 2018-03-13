package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityWatchLiveBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new android.databinding.ViewDataBinding.IncludedLayouts(30);
        sIncludes.setIncludes(1, 
            new String[] {"avatar_layout"},
            new int[] {2},
            new int[] {R.layout.avatar_layout});
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.video_frame, 3);
        sViewsWithIds.put(R.id.video_view_pager, 4);
        sViewsWithIds.put(R.id.textureView_container, 5);
        sViewsWithIds.put(R.id.texture_view, 6);
        sViewsWithIds.put(R.id.watermark, 7);
        sViewsWithIds.put(R.id.top_frame, 8);
        sViewsWithIds.put(R.id.device_name, 9);
        sViewsWithIds.put(R.id.watch_live_indicator, 10);
        sViewsWithIds.put(R.id.membership_top_overlay, 11);
        sViewsWithIds.put(R.id.talk_membership_copy, 12);
        sViewsWithIds.put(R.id.triangle, 13);
        sViewsWithIds.put(R.id.someone_else_speaking_text_view, 14);
        sViewsWithIds.put(R.id.bottom_btns_layout, 15);
        sViewsWithIds.put(R.id.talk_layout, 16);
        sViewsWithIds.put(R.id.talk_btn, 17);
        sViewsWithIds.put(R.id.pulsator, 18);
        sViewsWithIds.put(R.id.arc_animation, 19);
        sViewsWithIds.put(R.id.talk_background, 20);
        sViewsWithIds.put(R.id.talk_label, 21);
        sViewsWithIds.put(R.id.emergency_call_layout, 22);
        sViewsWithIds.put(R.id.emergency_call_btn, 23);
        sViewsWithIds.put(R.id.emergency_label, 24);
        sViewsWithIds.put(R.id.siren_layout, 25);
        sViewsWithIds.put(R.id.siren_btn, 26);
        sViewsWithIds.put(R.id.siren_click_able, 27);
        sViewsWithIds.put(R.id.siren_countdown_progress_wheel, 28);
        sViewsWithIds.put(R.id.siren_label, 29);
    }
    // views
    @NonNull
    public final is.yranac.canary.ui.views.ArcView arcAnimation;
    @Nullable
    public final is.yranac.canary.databinding.AvatarLayoutBinding avatarLayout;
    @NonNull
    public final android.widget.LinearLayout bottomBtnsLayout;
    @NonNull
    public final android.widget.FrameLayout customerTalkingLayout;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus deviceName;
    @NonNull
    public final android.widget.FrameLayout emergencyCallBtn;
    @NonNull
    public final android.widget.LinearLayout emergencyCallLayout;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus emergencyLabel;
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    @NonNull
    public final android.widget.LinearLayout membershipTopOverlay;
    @NonNull
    public final is.yranac.canary.ui.views.Pulsator pulsator;
    @NonNull
    public final android.widget.FrameLayout sirenBtn;
    @NonNull
    public final android.widget.FrameLayout sirenClickAble;
    @NonNull
    public final is.yranac.canary.ui.views.ProgressWheel sirenCountdownProgressWheel;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus sirenLabel;
    @NonNull
    public final android.widget.LinearLayout sirenLayout;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus someoneElseSpeakingTextView;
    @NonNull
    public final is.yranac.canary.ui.views.CircleView talkBackground;
    @NonNull
    public final android.widget.FrameLayout talkBtn;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus talkLabel;
    @NonNull
    public final android.widget.LinearLayout talkLayout;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus talkMembershipCopy;
    @NonNull
    public final is.yranac.canary.ui.views.ZoomTextureView textureView;
    @NonNull
    public final android.widget.RelativeLayout textureViewContainer;
    @NonNull
    public final android.widget.RelativeLayout topFrame;
    @NonNull
    public final is.yranac.canary.ui.views.TriangleView triangle;
    @NonNull
    public final android.widget.FrameLayout videoFrame;
    @NonNull
    public final android.support.v4.view.ViewPager videoViewPager;
    @NonNull
    public final is.yranac.canary.ui.views.viewpagerindicator.CirclePageIndicator watchLiveIndicator;
    @NonNull
    public final android.widget.ImageView watermark;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityWatchLiveBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 1);
        final Object[] bindings = mapBindings(bindingComponent, root, 30, sIncludes, sViewsWithIds);
        this.arcAnimation = (is.yranac.canary.ui.views.ArcView) bindings[19];
        this.avatarLayout = (is.yranac.canary.databinding.AvatarLayoutBinding) bindings[2];
        setContainedBinding(this.avatarLayout);
        this.bottomBtnsLayout = (android.widget.LinearLayout) bindings[15];
        this.customerTalkingLayout = (android.widget.FrameLayout) bindings[1];
        this.customerTalkingLayout.setTag(null);
        this.deviceName = (is.yranac.canary.ui.views.TextViewPlus) bindings[9];
        this.emergencyCallBtn = (android.widget.FrameLayout) bindings[23];
        this.emergencyCallLayout = (android.widget.LinearLayout) bindings[22];
        this.emergencyLabel = (is.yranac.canary.ui.views.TextViewPlus) bindings[24];
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.membershipTopOverlay = (android.widget.LinearLayout) bindings[11];
        this.pulsator = (is.yranac.canary.ui.views.Pulsator) bindings[18];
        this.sirenBtn = (android.widget.FrameLayout) bindings[26];
        this.sirenClickAble = (android.widget.FrameLayout) bindings[27];
        this.sirenCountdownProgressWheel = (is.yranac.canary.ui.views.ProgressWheel) bindings[28];
        this.sirenLabel = (is.yranac.canary.ui.views.TextViewPlus) bindings[29];
        this.sirenLayout = (android.widget.LinearLayout) bindings[25];
        this.someoneElseSpeakingTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[14];
        this.talkBackground = (is.yranac.canary.ui.views.CircleView) bindings[20];
        this.talkBtn = (android.widget.FrameLayout) bindings[17];
        this.talkLabel = (is.yranac.canary.ui.views.TextViewPlus) bindings[21];
        this.talkLayout = (android.widget.LinearLayout) bindings[16];
        this.talkMembershipCopy = (is.yranac.canary.ui.views.TextViewPlus) bindings[12];
        this.textureView = (is.yranac.canary.ui.views.ZoomTextureView) bindings[6];
        this.textureViewContainer = (android.widget.RelativeLayout) bindings[5];
        this.topFrame = (android.widget.RelativeLayout) bindings[8];
        this.triangle = (is.yranac.canary.ui.views.TriangleView) bindings[13];
        this.videoFrame = (android.widget.FrameLayout) bindings[3];
        this.videoViewPager = (android.support.v4.view.ViewPager) bindings[4];
        this.watchLiveIndicator = (is.yranac.canary.ui.views.viewpagerindicator.CirclePageIndicator) bindings[10];
        this.watermark = (android.widget.ImageView) bindings[7];
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
        }
        avatarLayout.invalidateAll();
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        if (avatarLayout.hasPendingBindings()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
            return variableSet;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeAvatarLayout((is.yranac.canary.databinding.AvatarLayoutBinding) object, fieldId);
        }
        return false;
    }
    private boolean onChangeAvatarLayout(is.yranac.canary.databinding.AvatarLayoutBinding AvatarLayout, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
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
        // batch finished
        executeBindingsOn(avatarLayout);
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static ActivityWatchLiveBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ActivityWatchLiveBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<ActivityWatchLiveBinding>inflate(inflater, is.yranac.canary.R.layout.activity_watch_live, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static ActivityWatchLiveBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ActivityWatchLiveBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.activity_watch_live, null, false), bindingComponent);
    }
    @NonNull
    public static ActivityWatchLiveBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ActivityWatchLiveBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/activity_watch_live_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new ActivityWatchLiveBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): avatarLayout
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}