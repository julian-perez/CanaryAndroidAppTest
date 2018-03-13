package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentSetupMembershipPreviewBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.header_text_view, 4);
        sViewsWithIds.put(R.id.bottom_btns, 5);
        sViewsWithIds.put(R.id.add_membership_btn, 6);
    }
    // views
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus addMembershipBtn;
    @NonNull
    public final android.widget.LinearLayout bottomBtns;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus headerTextView;
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    @NonNull
    private final is.yranac.canary.ui.views.TextViewPlus mboundView1;
    @NonNull
    private final is.yranac.canary.ui.views.TextViewPlus mboundView2;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus noThanksBtn;
    // variables
    @Nullable
    private is.yranac.canary.model.device.Device mDevice;
    @Nullable
    private is.yranac.canary.model.subscription.Subscription mSubscription;
    @Nullable
    private android.view.View mV;
    @Nullable
    private is.yranac.canary.model.location.Location mLocation;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentSetupMembershipPreviewBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 7, sIncludes, sViewsWithIds);
        this.addMembershipBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[6];
        this.bottomBtns = (android.widget.LinearLayout) bindings[5];
        this.headerTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[4];
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView1 = (is.yranac.canary.ui.views.TextViewPlus) bindings[1];
        this.mboundView1.setTag(null);
        this.mboundView2 = (is.yranac.canary.ui.views.TextViewPlus) bindings[2];
        this.mboundView2.setTag(null);
        this.noThanksBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[3];
        this.noThanksBtn.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x10L;
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
        if (BR.device == variableId) {
            setDevice((is.yranac.canary.model.device.Device) variable);
        }
        else if (BR.subscription == variableId) {
            setSubscription((is.yranac.canary.model.subscription.Subscription) variable);
        }
        else if (BR.v == variableId) {
            setV((android.view.View) variable);
        }
        else if (BR.location == variableId) {
            setLocation((is.yranac.canary.model.location.Location) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setDevice(@Nullable is.yranac.canary.model.device.Device Device) {
        this.mDevice = Device;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.device);
        super.requestRebind();
    }
    @Nullable
    public is.yranac.canary.model.device.Device getDevice() {
        return mDevice;
    }
    public void setSubscription(@Nullable is.yranac.canary.model.subscription.Subscription Subscription) {
        this.mSubscription = Subscription;
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
    public void setLocation(@Nullable is.yranac.canary.model.location.Location Location) {
        this.mLocation = Location;
        synchronized(this) {
            mDirtyFlags |= 0x8L;
        }
        notifyPropertyChanged(BR.location);
        super.requestRebind();
    }
    @Nullable
    public is.yranac.canary.model.location.Location getLocation() {
        return mLocation;
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
        is.yranac.canary.model.device.Device device = mDevice;
        boolean deviceHSNDevice = false;
        int locationUnitedStatesVVISIBLEVGONE = 0;
        int deviceHSNDeviceViewGONEViewVISIBLE = 0;
        is.yranac.canary.model.location.Location location = mLocation;
        boolean locationUnitedStates = false;

        if ((dirtyFlags & 0x11L) != 0) {



                if (device != null) {
                    // read device.HSNDevice
                    deviceHSNDevice = device.isHSNDevice();
                }
            if((dirtyFlags & 0x11L) != 0) {
                if(deviceHSNDevice) {
                        dirtyFlags |= 0x100L;
                }
                else {
                        dirtyFlags |= 0x80L;
                }
            }


                // read device.HSNDevice ? View.GONE : View.VISIBLE
                deviceHSNDeviceViewGONEViewVISIBLE = ((deviceHSNDevice) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
        }
        if ((dirtyFlags & 0x18L) != 0) {



                if (location != null) {
                    // read location.unitedStates
                    locationUnitedStates = location.isUnitedStates();
                }
            if((dirtyFlags & 0x18L) != 0) {
                if(locationUnitedStates) {
                        dirtyFlags |= 0x40L;
                }
                else {
                        dirtyFlags |= 0x20L;
                }
            }


                // read location.unitedStates ? View.VISIBLE : View.GONE
                locationUnitedStatesVVISIBLEVGONE = ((locationUnitedStates) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        // batch finished
        if ((dirtyFlags & 0x18L) != 0) {
            // api target 1

            this.mboundView1.setVisibility(locationUnitedStatesVVISIBLEVGONE);
            this.mboundView2.setVisibility(locationUnitedStatesVVISIBLEVGONE);
        }
        if ((dirtyFlags & 0x11L) != 0) {
            // api target 1

            this.noThanksBtn.setVisibility(deviceHSNDeviceViewGONEViewVISIBLE);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static FragmentSetupMembershipPreviewBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSetupMembershipPreviewBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentSetupMembershipPreviewBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_setup_membership_preview, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentSetupMembershipPreviewBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSetupMembershipPreviewBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_setup_membership_preview, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentSetupMembershipPreviewBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSetupMembershipPreviewBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_setup_membership_preview_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentSetupMembershipPreviewBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): device
        flag 1 (0x2L): subscription
        flag 2 (0x3L): v
        flag 3 (0x4L): location
        flag 4 (0x5L): null
        flag 5 (0x6L): location.unitedStates ? View.VISIBLE : View.GONE
        flag 6 (0x7L): location.unitedStates ? View.VISIBLE : View.GONE
        flag 7 (0x8L): device.HSNDevice ? View.GONE : View.VISIBLE
        flag 8 (0x9L): device.HSNDevice ? View.GONE : View.VISIBLE
    flag mapping end*/
    //end
}