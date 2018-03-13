package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentMainLayoutBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new android.databinding.ViewDataBinding.IncludedLayouts(18);
        sIncludes.setIncludes(1, 
            new String[] {"avatar_location_mode"},
            new int[] {2},
            new int[] {R.layout.avatar_location_mode});
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.settings_container, 3);
        sViewsWithIds.put(R.id.main_container, 4);
        sViewsWithIds.put(R.id.device_pager, 5);
        sViewsWithIds.put(R.id.header_layout, 6);
        sViewsWithIds.put(R.id.location_name_wrapper, 7);
        sViewsWithIds.put(R.id.right_header_info, 8);
        sViewsWithIds.put(R.id.preview_duration_text_view, 9);
        sViewsWithIds.put(R.id.menu_button, 10);
        sViewsWithIds.put(R.id.black_overlay_view, 11);
        sViewsWithIds.put(R.id.avatar_container, 12);
        sViewsWithIds.put(R.id.avatar_scroll_view, 13);
        sViewsWithIds.put(R.id.recording_text_view, 14);
        sViewsWithIds.put(R.id.avatar_list_container, 15);
        sViewsWithIds.put(R.id.tutorial_view, 16);
        sViewsWithIds.put(R.id.show_timeline_btn, 17);
    }
    // views
    @NonNull
    public final android.widget.FrameLayout avatarContainer;
    @NonNull
    public final android.widget.LinearLayout avatarListContainer;
    @Nullable
    public final is.yranac.canary.databinding.AvatarLocationModeBinding avatarLocationMode;
    @NonNull
    public final is.yranac.canary.ui.views.CanaryHorizontalScrollView avatarScrollView;
    @NonNull
    public final android.view.View blackOverlayView;
    @NonNull
    public final android.widget.LinearLayout bottomRowAnimations;
    @NonNull
    public final is.yranac.canary.ui.views.CustomViewPager devicePager;
    @NonNull
    public final android.widget.RelativeLayout headerLayout;
    @NonNull
    public final android.widget.TextSwitcher locationNameWrapper;
    @NonNull
    public final android.widget.RelativeLayout mainContainer;
    @NonNull
    public final is.yranac.canary.ui.views.MenuIcon menuButton;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus previewDurationTextView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus recordingTextView;
    @NonNull
    public final android.widget.LinearLayout rightHeaderInfo;
    @NonNull
    public final android.widget.FrameLayout settingsContainer;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus showTimelineBtn;
    @NonNull
    public final com.sothree.slidinguppanel.SlidingUpPanelLayout slidingLayout;
    @NonNull
    public final android.widget.RelativeLayout tutorialView;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentMainLayoutBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 1);
        final Object[] bindings = mapBindings(bindingComponent, root, 18, sIncludes, sViewsWithIds);
        this.avatarContainer = (android.widget.FrameLayout) bindings[12];
        this.avatarListContainer = (android.widget.LinearLayout) bindings[15];
        this.avatarLocationMode = (is.yranac.canary.databinding.AvatarLocationModeBinding) bindings[2];
        setContainedBinding(this.avatarLocationMode);
        this.avatarScrollView = (is.yranac.canary.ui.views.CanaryHorizontalScrollView) bindings[13];
        this.blackOverlayView = (android.view.View) bindings[11];
        this.bottomRowAnimations = (android.widget.LinearLayout) bindings[1];
        this.bottomRowAnimations.setTag(null);
        this.devicePager = (is.yranac.canary.ui.views.CustomViewPager) bindings[5];
        this.headerLayout = (android.widget.RelativeLayout) bindings[6];
        this.locationNameWrapper = (android.widget.TextSwitcher) bindings[7];
        this.mainContainer = (android.widget.RelativeLayout) bindings[4];
        this.menuButton = (is.yranac.canary.ui.views.MenuIcon) bindings[10];
        this.previewDurationTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[9];
        this.recordingTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[14];
        this.rightHeaderInfo = (android.widget.LinearLayout) bindings[8];
        this.settingsContainer = (android.widget.FrameLayout) bindings[3];
        this.showTimelineBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[17];
        this.slidingLayout = (com.sothree.slidinguppanel.SlidingUpPanelLayout) bindings[0];
        this.slidingLayout.setTag(null);
        this.tutorialView = (android.widget.RelativeLayout) bindings[16];
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
        }
        avatarLocationMode.invalidateAll();
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        if (avatarLocationMode.hasPendingBindings()) {
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
                return onChangeAvatarLocationMode((is.yranac.canary.databinding.AvatarLocationModeBinding) object, fieldId);
        }
        return false;
    }
    private boolean onChangeAvatarLocationMode(is.yranac.canary.databinding.AvatarLocationModeBinding AvatarLocationMode, int fieldId) {
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
        executeBindingsOn(avatarLocationMode);
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static FragmentMainLayoutBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentMainLayoutBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentMainLayoutBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_main_layout, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentMainLayoutBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentMainLayoutBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_main_layout, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentMainLayoutBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentMainLayoutBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_main_layout_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentMainLayoutBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): avatarLocationMode
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}