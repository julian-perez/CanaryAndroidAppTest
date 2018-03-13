package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentTutorialBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new android.databinding.ViewDataBinding.IncludedLayouts(24);
        sIncludes.setIncludes(1, 
            new String[] {"layout_device_statatics"},
            new int[] {3},
            new int[] {R.layout.layout_device_statatics});
        sIncludes.setIncludes(2, 
            new String[] {"avatar_location_mode"},
            new int[] {4},
            new int[] {R.layout.avatar_location_mode});
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.tutorial_image, 5);
        sViewsWithIds.put(R.id.menu_layout, 6);
        sViewsWithIds.put(R.id.menu_button, 7);
        sViewsWithIds.put(R.id.arrow_four, 8);
        sViewsWithIds.put(R.id.menu_dsc_text_view, 9);
        sViewsWithIds.put(R.id.watch_live_dsc_text_view, 10);
        sViewsWithIds.put(R.id.sensor_dsc_text_view, 11);
        sViewsWithIds.put(R.id.arrow_one, 12);
        sViewsWithIds.put(R.id.arrow_two, 13);
        sViewsWithIds.put(R.id.mode_change_dsc_text_view, 14);
        sViewsWithIds.put(R.id.flex_tutorial_dsc_text_view, 15);
        sViewsWithIds.put(R.id.arrow_three, 16);
        sViewsWithIds.put(R.id.avatar_container, 17);
        sViewsWithIds.put(R.id.okay_button, 18);
        sViewsWithIds.put(R.id.setup_one_layout, 19);
        sViewsWithIds.put(R.id.lets_start_with_basics, 20);
        sViewsWithIds.put(R.id.bottom_btns_layout, 21);
        sViewsWithIds.put(R.id.continue_buttton, 22);
        sViewsWithIds.put(R.id.skip_button, 23);
    }
    // views
    @NonNull
    public final android.widget.ImageView arrowFour;
    @NonNull
    public final android.widget.ImageView arrowOne;
    @NonNull
    public final android.widget.ImageView arrowThree;
    @NonNull
    public final android.widget.ImageView arrowTwo;
    @NonNull
    public final android.widget.FrameLayout avatarContainer;
    @NonNull
    public final android.widget.LinearLayout avatarListContainer;
    @NonNull
    public final android.widget.LinearLayout bottomBtnsLayout;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus continueButtton;
    @Nullable
    public final is.yranac.canary.databinding.LayoutDeviceStataticsBinding deviceTutorialLayout;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus flexTutorialDscTextView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus letsStartWithBasics;
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    @NonNull
    public final android.widget.ImageButton menuButton;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus menuDscTextView;
    @NonNull
    public final android.widget.LinearLayout menuLayout;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus modeChangeDscTextView;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus okayButton;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus sensorDscTextView;
    @NonNull
    public final android.widget.RelativeLayout setupOneLayout;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus skipButton;
    @NonNull
    public final android.widget.ImageView tutorialImage;
    @NonNull
    public final android.widget.RelativeLayout tutorialLayout;
    @Nullable
    public final is.yranac.canary.databinding.AvatarLocationModeBinding tutorialModeLayout;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus watchLiveDscTextView;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentTutorialBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 2);
        final Object[] bindings = mapBindings(bindingComponent, root, 24, sIncludes, sViewsWithIds);
        this.arrowFour = (android.widget.ImageView) bindings[8];
        this.arrowOne = (android.widget.ImageView) bindings[12];
        this.arrowThree = (android.widget.ImageView) bindings[16];
        this.arrowTwo = (android.widget.ImageView) bindings[13];
        this.avatarContainer = (android.widget.FrameLayout) bindings[17];
        this.avatarListContainer = (android.widget.LinearLayout) bindings[2];
        this.avatarListContainer.setTag(null);
        this.bottomBtnsLayout = (android.widget.LinearLayout) bindings[21];
        this.continueButtton = (is.yranac.canary.ui.views.ButtonPlus) bindings[22];
        this.deviceTutorialLayout = (is.yranac.canary.databinding.LayoutDeviceStataticsBinding) bindings[3];
        setContainedBinding(this.deviceTutorialLayout);
        this.flexTutorialDscTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[15];
        this.letsStartWithBasics = (is.yranac.canary.ui.views.TextViewPlus) bindings[20];
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.menuButton = (android.widget.ImageButton) bindings[7];
        this.menuDscTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[9];
        this.menuLayout = (android.widget.LinearLayout) bindings[6];
        this.modeChangeDscTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[14];
        this.okayButton = (is.yranac.canary.ui.views.ButtonPlus) bindings[18];
        this.sensorDscTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[11];
        this.setupOneLayout = (android.widget.RelativeLayout) bindings[19];
        this.skipButton = (is.yranac.canary.ui.views.ButtonPlus) bindings[23];
        this.tutorialImage = (android.widget.ImageView) bindings[5];
        this.tutorialLayout = (android.widget.RelativeLayout) bindings[1];
        this.tutorialLayout.setTag(null);
        this.tutorialModeLayout = (is.yranac.canary.databinding.AvatarLocationModeBinding) bindings[4];
        setContainedBinding(this.tutorialModeLayout);
        this.watchLiveDscTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[10];
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x4L;
        }
        deviceTutorialLayout.invalidateAll();
        tutorialModeLayout.invalidateAll();
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        if (deviceTutorialLayout.hasPendingBindings()) {
            return true;
        }
        if (tutorialModeLayout.hasPendingBindings()) {
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
                return onChangeTutorialModeLayout((is.yranac.canary.databinding.AvatarLocationModeBinding) object, fieldId);
            case 1 :
                return onChangeDeviceTutorialLayout((is.yranac.canary.databinding.LayoutDeviceStataticsBinding) object, fieldId);
        }
        return false;
    }
    private boolean onChangeTutorialModeLayout(is.yranac.canary.databinding.AvatarLocationModeBinding TutorialModeLayout, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeDeviceTutorialLayout(is.yranac.canary.databinding.LayoutDeviceStataticsBinding DeviceTutorialLayout, int fieldId) {
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
        // batch finished
        executeBindingsOn(deviceTutorialLayout);
        executeBindingsOn(tutorialModeLayout);
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static FragmentTutorialBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentTutorialBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentTutorialBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_tutorial, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentTutorialBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentTutorialBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_tutorial, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentTutorialBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentTutorialBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_tutorial_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentTutorialBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): tutorialModeLayout
        flag 1 (0x2L): deviceTutorialLayout
        flag 2 (0x3L): null
    flag mapping end*/
    //end
}