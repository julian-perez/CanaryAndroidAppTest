package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentAboutDeviceBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new android.databinding.ViewDataBinding.IncludedLayouts(5);
        sIncludes.setIncludes(0, 
            new String[] {"layout_about_device_header"},
            new int[] {4},
            new int[] {R.layout.layout_about_device_header});
        sViewsWithIds = null;
    }
    // views
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel deviceFirmware;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel deviceName;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel deviceSerial;
    @Nullable
    public final is.yranac.canary.databinding.LayoutAboutDeviceHeaderBinding headerLayout;
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    // variables
    @Nullable
    private is.yranac.canary.model.device.Device mDevice;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentAboutDeviceBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 1);
        final Object[] bindings = mapBindings(bindingComponent, root, 5, sIncludes, sViewsWithIds);
        this.deviceFirmware = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[3];
        this.deviceFirmware.setTag(null);
        this.deviceName = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[1];
        this.deviceName.setTag(null);
        this.deviceSerial = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[2];
        this.deviceSerial.setTag(null);
        this.headerLayout = (is.yranac.canary.databinding.LayoutAboutDeviceHeaderBinding) bindings[4];
        setContainedBinding(this.headerLayout);
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x4L;
        }
        headerLayout.invalidateAll();
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        if (headerLayout.hasPendingBindings()) {
            return true;
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
            mDirtyFlags |= 0x2L;
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
            case 0 :
                return onChangeHeaderLayout((is.yranac.canary.databinding.LayoutAboutDeviceHeaderBinding) object, fieldId);
        }
        return false;
    }
    private boolean onChangeHeaderLayout(is.yranac.canary.databinding.LayoutAboutDeviceHeaderBinding HeaderLayout, int fieldId) {
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
        is.yranac.canary.model.device.Device device = mDevice;
        boolean deviceDeviceActivated = false;
        java.lang.String deviceApplicationVersion = null;
        int deviceDeviceActivatedViewVISIBLEViewGONE = 0;
        java.lang.String DeviceName1 = null;
        java.lang.String deviceSerialNumber = null;

        if ((dirtyFlags & 0x6L) != 0) {



                if (device != null) {
                    // read device.deviceActivated
                    deviceDeviceActivated = device.deviceActivated;
                    // read device.applicationVersion
                    deviceApplicationVersion = device.applicationVersion;
                    // read device.name
                    DeviceName1 = device.name;
                    // read device.serialNumber
                    deviceSerialNumber = device.serialNumber;
                }
            if((dirtyFlags & 0x6L) != 0) {
                if(deviceDeviceActivated) {
                        dirtyFlags |= 0x10L;
                }
                else {
                        dirtyFlags |= 0x8L;
                }
            }


                // read device.deviceActivated ? View.VISIBLE : View.GONE
                deviceDeviceActivatedViewVISIBLEViewGONE = ((deviceDeviceActivated) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        // batch finished
        if ((dirtyFlags & 0x6L) != 0) {
            // api target 1

            this.deviceFirmware.setText(deviceApplicationVersion);
            this.deviceFirmware.setVisibility(deviceDeviceActivatedViewVISIBLEViewGONE);
            this.deviceName.setText(DeviceName1);
            this.deviceSerial.setText(deviceSerialNumber);
        }
        executeBindingsOn(headerLayout);
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static FragmentAboutDeviceBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentAboutDeviceBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentAboutDeviceBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_about_device, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentAboutDeviceBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentAboutDeviceBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_about_device, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentAboutDeviceBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentAboutDeviceBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_about_device_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentAboutDeviceBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): headerLayout
        flag 1 (0x2L): device
        flag 2 (0x3L): null
        flag 3 (0x4L): device.deviceActivated ? View.VISIBLE : View.GONE
        flag 4 (0x5L): device.deviceActivated ? View.VISIBLE : View.GONE
    flag mapping end*/
    //end
}