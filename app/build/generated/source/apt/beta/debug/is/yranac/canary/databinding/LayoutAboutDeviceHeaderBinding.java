package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class LayoutAboutDeviceHeaderBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.device_layout, 2);
        sViewsWithIds.put(R.id.device_icon, 3);
        sViewsWithIds.put(R.id.header_title_text_view, 4);
    }
    // views
    @NonNull
    public final android.widget.ImageView deviceIcon;
    @NonNull
    public final android.widget.LinearLayout deviceLayout;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus headerTitleTextView;
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus removeDeviceBtn;
    // variables
    @Nullable
    private is.yranac.canary.model.device.Device mDevice;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public LayoutAboutDeviceHeaderBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 5, sIncludes, sViewsWithIds);
        this.deviceIcon = (android.widget.ImageView) bindings[3];
        this.deviceLayout = (android.widget.LinearLayout) bindings[2];
        this.headerTitleTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[4];
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.removeDeviceBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[1];
        this.removeDeviceBtn.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
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
        boolean deviceFailedOTA = false;
        boolean deviceDeviceActivated = false;
        int deviceFailedOTAViewGONEViewVISIBLE = 0;
        float deviceDeviceActivatedFloat10fFloat05f = 0f;

        if ((dirtyFlags & 0x3L) != 0) {



                if (device != null) {
                    // read device.failedOTA()
                    deviceFailedOTA = device.failedOTA();
                    // read device.deviceActivated
                    deviceDeviceActivated = device.deviceActivated;
                }
            if((dirtyFlags & 0x3L) != 0) {
                if(deviceFailedOTA) {
                        dirtyFlags |= 0x8L;
                }
                else {
                        dirtyFlags |= 0x4L;
                }
            }
            if((dirtyFlags & 0x3L) != 0) {
                if(deviceDeviceActivated) {
                        dirtyFlags |= 0x20L;
                }
                else {
                        dirtyFlags |= 0x10L;
                }
            }


                // read device.failedOTA() ? View.GONE : View.VISIBLE
                deviceFailedOTAViewGONEViewVISIBLE = ((deviceFailedOTA) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
                // read device.deviceActivated ? 1.0f : 0.5f
                deviceDeviceActivatedFloat10fFloat05f = ((deviceDeviceActivated) ? (1.0f) : (0.5f));
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 11
            if(getBuildSdkInt() >= 11) {

                this.removeDeviceBtn.setAlpha(deviceDeviceActivatedFloat10fFloat05f);
            }
            // api target 1

            this.removeDeviceBtn.setEnabled(deviceDeviceActivated);
            this.removeDeviceBtn.setVisibility(deviceFailedOTAViewGONEViewVISIBLE);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static LayoutAboutDeviceHeaderBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static LayoutAboutDeviceHeaderBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<LayoutAboutDeviceHeaderBinding>inflate(inflater, is.yranac.canary.R.layout.layout_about_device_header, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static LayoutAboutDeviceHeaderBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static LayoutAboutDeviceHeaderBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.layout_about_device_header, null, false), bindingComponent);
    }
    @NonNull
    public static LayoutAboutDeviceHeaderBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static LayoutAboutDeviceHeaderBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/layout_about_device_header_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new LayoutAboutDeviceHeaderBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): device
        flag 1 (0x2L): null
        flag 2 (0x3L): device.failedOTA() ? View.GONE : View.VISIBLE
        flag 3 (0x4L): device.failedOTA() ? View.GONE : View.VISIBLE
        flag 4 (0x5L): device.deviceActivated ? 1.0f : 0.5f
        flag 5 (0x6L): device.deviceActivated ? 1.0f : 0.5f
    flag mapping end*/
    //end
}