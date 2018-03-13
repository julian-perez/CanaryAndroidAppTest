package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentModeSlideShowBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new android.databinding.ViewDataBinding.IncludedLayouts(44);
        sIncludes.setIncludes(0, 
            new String[] {"layout_mode_actions"},
            new int[] {2},
            new int[] {R.layout.layout_mode_actions});
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.customize_image_view, 3);
        sViewsWithIds.put(R.id.privacy_image_view, 4);
        sViewsWithIds.put(R.id.auto_arm_image_view, 5);
        sViewsWithIds.put(R.id.night_image_view, 6);
        sViewsWithIds.put(R.id.home_image_view, 7);
        sViewsWithIds.put(R.id.away_image_view, 8);
        sViewsWithIds.put(R.id.device_armed_image_view, 9);
        sViewsWithIds.put(R.id.blur_image_view, 10);
        sViewsWithIds.put(R.id.night_mode_layout, 11);
        sViewsWithIds.put(R.id.night_text_view, 12);
        sViewsWithIds.put(R.id.night_dsc_text_view, 13);
        sViewsWithIds.put(R.id.privacy_mode_layout, 14);
        sViewsWithIds.put(R.id.privacy_header_view, 15);
        sViewsWithIds.put(R.id.privacy_dsc_text_view, 16);
        sViewsWithIds.put(R.id.customize_modes, 17);
        sViewsWithIds.put(R.id.personalize_modes, 18);
        sViewsWithIds.put(R.id.device_lights_dsc, 19);
        sViewsWithIds.put(R.id.auto_arm_layout, 20);
        sViewsWithIds.put(R.id.auto_arm_dsc_text_vew, 21);
        sViewsWithIds.put(R.id.home_mode_layout, 22);
        sViewsWithIds.put(R.id.home_text_view, 23);
        sViewsWithIds.put(R.id.home_dsc_text_view, 24);
        sViewsWithIds.put(R.id.away_mode_layout, 25);
        sViewsWithIds.put(R.id.away_text_view, 26);
        sViewsWithIds.put(R.id.away_dsc_text_view, 27);
        sViewsWithIds.put(R.id.canary_three_modes, 28);
        sViewsWithIds.put(R.id.establishing_connection_text_view, 29);
        sViewsWithIds.put(R.id.downloading_latest_text_view, 30);
        sViewsWithIds.put(R.id.ota_finish_layout, 31);
        sViewsWithIds.put(R.id.ota_title_text, 32);
        sViewsWithIds.put(R.id.ota_message_text_view, 33);
        sViewsWithIds.put(R.id.next_btn, 34);
        sViewsWithIds.put(R.id.first_slide_layout, 35);
        sViewsWithIds.put(R.id.canary_mode_text_view, 36);
        sViewsWithIds.put(R.id.pager, 37);
        sViewsWithIds.put(R.id.indicator, 38);
        sViewsWithIds.put(R.id.setup_steps, 39);
        sViewsWithIds.put(R.id.left_arrow, 40);
        sViewsWithIds.put(R.id.current_step, 41);
        sViewsWithIds.put(R.id.next_step, 42);
        sViewsWithIds.put(R.id.right_arrow, 43);
    }
    // views
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus autoArmDscTextVew;
    @NonNull
    public final android.widget.ImageView autoArmImageView;
    @NonNull
    public final android.widget.LinearLayout autoArmLayout;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus awayDscTextView;
    @NonNull
    public final android.widget.ImageView awayImageView;
    @NonNull
    public final android.widget.LinearLayout awayModeLayout;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus awayTextView;
    @NonNull
    public final android.widget.ImageView blurImageView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus canaryModeTextView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus canaryThreeModes;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus currentStep;
    @NonNull
    public final android.widget.ImageView customizeImageView;
    @NonNull
    public final android.widget.LinearLayout customizeModes;
    @NonNull
    public final android.widget.ImageView deviceArmedImageView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus deviceLightsDsc;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus downloadingLatestTextView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus establishingConnectionTextView;
    @NonNull
    public final android.widget.LinearLayout firstSlideLayout;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus homeDscTextView;
    @NonNull
    public final android.widget.ImageView homeImageView;
    @NonNull
    public final android.widget.LinearLayout homeModeLayout;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus homeTextView;
    @NonNull
    public final is.yranac.canary.ui.views.viewpagerindicator.CirclePageIndicator indicator;
    @NonNull
    public final android.support.v7.widget.AppCompatImageView leftArrow;
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    @Nullable
    public final is.yranac.canary.databinding.LayoutModeActionsBinding modeActionLayout;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus nextBtn;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus nextStep;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus nightDscTextView;
    @NonNull
    public final android.widget.ImageView nightImageView;
    @NonNull
    public final android.widget.LinearLayout nightModeLayout;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus nightTextView;
    @NonNull
    public final android.widget.RelativeLayout otaFinishLayout;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus otaMessageTextView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus otaTitleText;
    @NonNull
    public final is.yranac.canary.ui.views.ViewPagerCustomDuration pager;
    @NonNull
    public final android.widget.LinearLayout personalizeModes;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus privacyDscTextView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus privacyHeaderView;
    @NonNull
    public final android.widget.ImageView privacyImageView;
    @NonNull
    public final android.widget.LinearLayout privacyModeLayout;
    @NonNull
    public final android.support.v7.widget.AppCompatImageView rightArrow;
    @NonNull
    public final android.widget.LinearLayout setupSteps;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus startTutorial;
    // variables
    @Nullable
    private int mTutorialType;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentModeSlideShowBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 1);
        final Object[] bindings = mapBindings(bindingComponent, root, 44, sIncludes, sViewsWithIds);
        this.autoArmDscTextVew = (is.yranac.canary.ui.views.TextViewPlus) bindings[21];
        this.autoArmImageView = (android.widget.ImageView) bindings[5];
        this.autoArmLayout = (android.widget.LinearLayout) bindings[20];
        this.awayDscTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[27];
        this.awayImageView = (android.widget.ImageView) bindings[8];
        this.awayModeLayout = (android.widget.LinearLayout) bindings[25];
        this.awayTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[26];
        this.blurImageView = (android.widget.ImageView) bindings[10];
        this.canaryModeTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[36];
        this.canaryThreeModes = (is.yranac.canary.ui.views.TextViewPlus) bindings[28];
        this.currentStep = (is.yranac.canary.ui.views.TextViewPlus) bindings[41];
        this.customizeImageView = (android.widget.ImageView) bindings[3];
        this.customizeModes = (android.widget.LinearLayout) bindings[17];
        this.deviceArmedImageView = (android.widget.ImageView) bindings[9];
        this.deviceLightsDsc = (is.yranac.canary.ui.views.TextViewPlus) bindings[19];
        this.downloadingLatestTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[30];
        this.establishingConnectionTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[29];
        this.firstSlideLayout = (android.widget.LinearLayout) bindings[35];
        this.homeDscTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[24];
        this.homeImageView = (android.widget.ImageView) bindings[7];
        this.homeModeLayout = (android.widget.LinearLayout) bindings[22];
        this.homeTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[23];
        this.indicator = (is.yranac.canary.ui.views.viewpagerindicator.CirclePageIndicator) bindings[38];
        this.leftArrow = (android.support.v7.widget.AppCompatImageView) bindings[40];
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.modeActionLayout = (is.yranac.canary.databinding.LayoutModeActionsBinding) bindings[2];
        setContainedBinding(this.modeActionLayout);
        this.nextBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[34];
        this.nextStep = (is.yranac.canary.ui.views.TextViewPlus) bindings[42];
        this.nightDscTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[13];
        this.nightImageView = (android.widget.ImageView) bindings[6];
        this.nightModeLayout = (android.widget.LinearLayout) bindings[11];
        this.nightTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[12];
        this.otaFinishLayout = (android.widget.RelativeLayout) bindings[31];
        this.otaMessageTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[33];
        this.otaTitleText = (is.yranac.canary.ui.views.TextViewPlus) bindings[32];
        this.pager = (is.yranac.canary.ui.views.ViewPagerCustomDuration) bindings[37];
        this.personalizeModes = (android.widget.LinearLayout) bindings[18];
        this.privacyDscTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[16];
        this.privacyHeaderView = (is.yranac.canary.ui.views.TextViewPlus) bindings[15];
        this.privacyImageView = (android.widget.ImageView) bindings[4];
        this.privacyModeLayout = (android.widget.LinearLayout) bindings[14];
        this.rightArrow = (android.support.v7.widget.AppCompatImageView) bindings[43];
        this.setupSteps = (android.widget.LinearLayout) bindings[39];
        this.startTutorial = (is.yranac.canary.ui.views.ButtonPlus) bindings[1];
        this.startTutorial.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x4L;
        }
        modeActionLayout.invalidateAll();
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        if (modeActionLayout.hasPendingBindings()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.tutorialType == variableId) {
            setTutorialType((int) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setTutorialType(int TutorialType) {
        this.mTutorialType = TutorialType;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.tutorialType);
        super.requestRebind();
    }
    public int getTutorialType() {
        return mTutorialType;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeModeActionLayout((is.yranac.canary.databinding.LayoutModeActionsBinding) object, fieldId);
        }
        return false;
    }
    private boolean onChangeModeActionLayout(is.yranac.canary.databinding.LayoutModeActionsBinding ModeActionLayout, int fieldId) {
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
        boolean tutorialTypeInt1 = false;
        int tutorialType = mTutorialType;
        int tutorialTypeInt1VVISIBLEVGONE = 0;

        if ((dirtyFlags & 0x6L) != 0) {



                // read tutorialType == 1
                tutorialTypeInt1 = (tutorialType) == (1);
            if((dirtyFlags & 0x6L) != 0) {
                if(tutorialTypeInt1) {
                        dirtyFlags |= 0x10L;
                }
                else {
                        dirtyFlags |= 0x8L;
                }
            }


                // read tutorialType == 1 ? v.VISIBLE : v.GONE
                tutorialTypeInt1VVISIBLEVGONE = ((tutorialTypeInt1) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        // batch finished
        if ((dirtyFlags & 0x6L) != 0) {
            // api target 1

            this.startTutorial.setVisibility(tutorialTypeInt1VVISIBLEVGONE);
        }
        executeBindingsOn(modeActionLayout);
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static FragmentModeSlideShowBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentModeSlideShowBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentModeSlideShowBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_mode_slide_show, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentModeSlideShowBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentModeSlideShowBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_mode_slide_show, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentModeSlideShowBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentModeSlideShowBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_mode_slide_show_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentModeSlideShowBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): modeActionLayout
        flag 1 (0x2L): tutorialType
        flag 2 (0x3L): null
        flag 3 (0x4L): tutorialType == 1 ? v.VISIBLE : v.GONE
        flag 4 (0x5L): tutorialType == 1 ? v.VISIBLE : v.GONE
    flag mapping end*/
    //end
}