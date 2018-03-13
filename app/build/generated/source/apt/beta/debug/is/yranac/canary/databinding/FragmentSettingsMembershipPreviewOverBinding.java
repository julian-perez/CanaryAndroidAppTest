package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentSettingsMembershipPreviewOverBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.location_address_label, 1);
        sViewsWithIds.put(R.id.preview_ended_dsc_text_view, 2);
        sViewsWithIds.put(R.id.device_layout, 3);
        sViewsWithIds.put(R.id.device_one_image, 4);
        sViewsWithIds.put(R.id.device_two_image, 5);
        sViewsWithIds.put(R.id.device_three_image, 6);
        sViewsWithIds.put(R.id.device_four_image, 7);
        sViewsWithIds.put(R.id.big_blue_line, 8);
        sViewsWithIds.put(R.id.twenty_four_hours, 9);
        sViewsWithIds.put(R.id.device_hour_layout, 10);
        sViewsWithIds.put(R.id.device_layout_one, 11);
        sViewsWithIds.put(R.id.device_hours_one, 12);
        sViewsWithIds.put(R.id.device_layout_two, 13);
        sViewsWithIds.put(R.id.device_hours_two, 14);
        sViewsWithIds.put(R.id.device_layout_three, 15);
        sViewsWithIds.put(R.id.device_hours_three, 16);
        sViewsWithIds.put(R.id.device_layout_four, 17);
        sViewsWithIds.put(R.id.device_hours_four, 18);
        sViewsWithIds.put(R.id.add_a_membersip_button, 19);
        sViewsWithIds.put(R.id.continue_preview_button, 20);
    }
    // views
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus addAMembersipButton;
    @NonNull
    public final android.view.View bigBlueLine;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus continuePreviewButton;
    @NonNull
    public final android.widget.ImageView deviceFourImage;
    @NonNull
    public final android.widget.LinearLayout deviceHourLayout;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus deviceHoursFour;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus deviceHoursOne;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus deviceHoursThree;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus deviceHoursTwo;
    @NonNull
    public final android.widget.LinearLayout deviceLayout;
    @NonNull
    public final android.widget.LinearLayout deviceLayoutFour;
    @NonNull
    public final android.widget.LinearLayout deviceLayoutOne;
    @NonNull
    public final android.widget.LinearLayout deviceLayoutThree;
    @NonNull
    public final android.widget.LinearLayout deviceLayoutTwo;
    @NonNull
    public final android.widget.ImageView deviceOneImage;
    @NonNull
    public final android.widget.ImageView deviceThreeImage;
    @NonNull
    public final android.widget.ImageView deviceTwoImage;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus locationAddressLabel;
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus previewEndedDscTextView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus twentyFourHours;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentSettingsMembershipPreviewOverBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 21, sIncludes, sViewsWithIds);
        this.addAMembersipButton = (is.yranac.canary.ui.views.ButtonPlus) bindings[19];
        this.bigBlueLine = (android.view.View) bindings[8];
        this.continuePreviewButton = (is.yranac.canary.ui.views.ButtonPlus) bindings[20];
        this.deviceFourImage = (android.widget.ImageView) bindings[7];
        this.deviceHourLayout = (android.widget.LinearLayout) bindings[10];
        this.deviceHoursFour = (is.yranac.canary.ui.views.TextViewPlus) bindings[18];
        this.deviceHoursOne = (is.yranac.canary.ui.views.TextViewPlus) bindings[12];
        this.deviceHoursThree = (is.yranac.canary.ui.views.TextViewPlus) bindings[16];
        this.deviceHoursTwo = (is.yranac.canary.ui.views.TextViewPlus) bindings[14];
        this.deviceLayout = (android.widget.LinearLayout) bindings[3];
        this.deviceLayoutFour = (android.widget.LinearLayout) bindings[17];
        this.deviceLayoutOne = (android.widget.LinearLayout) bindings[11];
        this.deviceLayoutThree = (android.widget.LinearLayout) bindings[15];
        this.deviceLayoutTwo = (android.widget.LinearLayout) bindings[13];
        this.deviceOneImage = (android.widget.ImageView) bindings[4];
        this.deviceThreeImage = (android.widget.ImageView) bindings[6];
        this.deviceTwoImage = (android.widget.ImageView) bindings[5];
        this.locationAddressLabel = (is.yranac.canary.ui.views.TextViewPlus) bindings[1];
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.previewEndedDscTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[2];
        this.twentyFourHours = (is.yranac.canary.ui.views.TextViewPlus) bindings[9];
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x1L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
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
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static FragmentSettingsMembershipPreviewOverBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsMembershipPreviewOverBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentSettingsMembershipPreviewOverBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_settings_membership_preview_over, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentSettingsMembershipPreviewOverBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsMembershipPreviewOverBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_settings_membership_preview_over, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentSettingsMembershipPreviewOverBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsMembershipPreviewOverBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_settings_membership_preview_over_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentSettingsMembershipPreviewOverBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}