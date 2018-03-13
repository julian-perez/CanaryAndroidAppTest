package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentEditDeviceBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new android.databinding.ViewDataBinding.IncludedLayouts(17);
        sIncludes.setIncludes(4, 
            new String[] {"layout_homehealth", "layout_sensor_flex"},
            new int[] {9, 10},
            new int[] {R.layout.layout_homehealth, R.layout.layout_sensor_flex});
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.retry_setup_btn, 11);
        sViewsWithIds.put(R.id.remove_device_btn, 12);
        sViewsWithIds.put(R.id.device_thumbnail_image_view, 13);
        sViewsWithIds.put(R.id.image_gradient, 14);
        sViewsWithIds.put(R.id.masking_display, 15);
        sViewsWithIds.put(R.id.image_preview_unavailable, 16);
    }
    // views
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel aboutDevice;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel batterySettings;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel changeConnection;
    @NonNull
    public final android.support.percent.PercentFrameLayout deviceDetailsLayout;
    @NonNull
    public final android.widget.ImageView deviceThumbnailImageView;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus editMasksLayout;
    @NonNull
    public final android.view.View imageGradient;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus imagePreviewUnavailable;
    @NonNull
    public final is.yranac.canary.ui.views.masking.MaskViewingView maskingDisplay;
    @NonNull
    private final android.widget.ScrollView mboundView0;
    @NonNull
    private final android.widget.LinearLayout mboundView1;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus otaStatusBackGround;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus removeDeviceBtn;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus retrySetupBtn;
    @NonNull
    public final android.widget.LinearLayout sensorDataContainer;
    @Nullable
    public final is.yranac.canary.databinding.LayoutSensorFlexBinding sensorDataFlexLayout;
    @Nullable
    public final is.yranac.canary.databinding.LayoutHomehealthBinding sensorDataLayout;
    // variables
    @Nullable
    private is.yranac.canary.model.device.Device mDevice;
    @Nullable
    private int mNumberOfMasks;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentEditDeviceBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 2);
        final Object[] bindings = mapBindings(bindingComponent, root, 17, sIncludes, sViewsWithIds);
        this.aboutDevice = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[6];
        this.aboutDevice.setTag(null);
        this.batterySettings = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[7];
        this.batterySettings.setTag(null);
        this.changeConnection = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[8];
        this.changeConnection.setTag(null);
        this.deviceDetailsLayout = (android.support.percent.PercentFrameLayout) bindings[3];
        this.deviceDetailsLayout.setTag(null);
        this.deviceThumbnailImageView = (android.widget.ImageView) bindings[13];
        this.editMasksLayout = (is.yranac.canary.ui.views.ButtonPlus) bindings[5];
        this.editMasksLayout.setTag(null);
        this.imageGradient = (android.view.View) bindings[14];
        this.imagePreviewUnavailable = (is.yranac.canary.ui.views.TextViewPlus) bindings[16];
        this.maskingDisplay = (is.yranac.canary.ui.views.masking.MaskViewingView) bindings[15];
        this.mboundView0 = (android.widget.ScrollView) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView1 = (android.widget.LinearLayout) bindings[1];
        this.mboundView1.setTag(null);
        this.otaStatusBackGround = (is.yranac.canary.ui.views.TextViewPlus) bindings[2];
        this.otaStatusBackGround.setTag(null);
        this.removeDeviceBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[12];
        this.retrySetupBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[11];
        this.sensorDataContainer = (android.widget.LinearLayout) bindings[4];
        this.sensorDataContainer.setTag(null);
        this.sensorDataFlexLayout = (is.yranac.canary.databinding.LayoutSensorFlexBinding) bindings[10];
        setContainedBinding(this.sensorDataFlexLayout);
        this.sensorDataLayout = (is.yranac.canary.databinding.LayoutHomehealthBinding) bindings[9];
        setContainedBinding(this.sensorDataLayout);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x10L;
        }
        sensorDataLayout.invalidateAll();
        sensorDataFlexLayout.invalidateAll();
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        if (sensorDataLayout.hasPendingBindings()) {
            return true;
        }
        if (sensorDataFlexLayout.hasPendingBindings()) {
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
        else if (BR.numberOfMasks == variableId) {
            setNumberOfMasks((int) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setDevice(@Nullable is.yranac.canary.model.device.Device Device) {
        this.mDevice = Device;
        synchronized(this) {
            mDirtyFlags |= 0x4L;
        }
        notifyPropertyChanged(BR.device);
        super.requestRebind();
    }
    @Nullable
    public is.yranac.canary.model.device.Device getDevice() {
        return mDevice;
    }
    public void setNumberOfMasks(int NumberOfMasks) {
        this.mNumberOfMasks = NumberOfMasks;
        synchronized(this) {
            mDirtyFlags |= 0x8L;
        }
        notifyPropertyChanged(BR.numberOfMasks);
        super.requestRebind();
    }
    public int getNumberOfMasks() {
        return mNumberOfMasks;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeSensorDataLayout((is.yranac.canary.databinding.LayoutHomehealthBinding) object, fieldId);
            case 1 :
                return onChangeSensorDataFlexLayout((is.yranac.canary.databinding.LayoutSensorFlexBinding) object, fieldId);
        }
        return false;
    }
    private boolean onChangeSensorDataLayout(is.yranac.canary.databinding.LayoutHomehealthBinding SensorDataLayout, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeSensorDataFlexLayout(is.yranac.canary.databinding.LayoutSensorFlexBinding SensorDataFlexLayout, int fieldId) {
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
        java.lang.String numberOfMasksInt1EditMasksLayoutAndroidStringMaskingEditMasksEditMasksLayoutAndroidStringMaskingEditMask = null;
        boolean numberOfMasksInt0 = false;
        is.yranac.canary.model.device.Device device = mDevice;
        boolean deviceFailedOTA = false;
        boolean deviceDeviceActivated = false;
        java.lang.String numberOfMasksInt0EditMasksLayoutAndroidStringMaskingCreateMaskNumberOfMasksInt1EditMasksLayoutAndroidStringMaskingEditMasksEditMasksLayoutAndroidStringMaskingEditMask = null;
        int numberOfMasks = mNumberOfMasks;
        boolean deviceOtaing = false;
        boolean deviceHasBattery = false;
        int deviceDeviceActivatedViewVISIBLEViewGONE = 0;
        int deviceHasBatteryDeviceDeviceActivatedBooleanFalseViewVISIBLEViewGONE = 0;
        java.lang.String deviceGetDeviceTypeName = null;
        boolean deviceHasBatteryDeviceDeviceActivatedBooleanFalse = false;
        java.lang.String stringFormatAboutDeviceAndroidStringAboutOptionDeviceGetDeviceTypeName = null;
        boolean numberOfMasksInt1 = false;
        int deviceOtaingViewVISIBLEViewGONE = 0;
        int deviceFailedOTAViewVISIBLEViewGONE = 0;

        if ((dirtyFlags & 0x14L) != 0) {



                if (device != null) {
                    // read device.failedOTA()
                    deviceFailedOTA = device.failedOTA();
                    // read device.deviceActivated
                    deviceDeviceActivated = device.deviceActivated;
                    // read device.otaing
                    deviceOtaing = device.isOtaing();
                    // read device.hasBattery()
                    deviceHasBattery = device.hasBattery();
                    // read device.getDeviceTypeName()
                    deviceGetDeviceTypeName = device.getDeviceTypeName();
                }
            if((dirtyFlags & 0x14L) != 0) {
                if(deviceFailedOTA) {
                        dirtyFlags |= 0x40000L;
                }
                else {
                        dirtyFlags |= 0x20000L;
                }
            }
            if((dirtyFlags & 0x14L) != 0) {
                if(deviceDeviceActivated) {
                        dirtyFlags |= 0x400L;
                }
                else {
                        dirtyFlags |= 0x200L;
                }
            }
            if((dirtyFlags & 0x14L) != 0) {
                if(deviceOtaing) {
                        dirtyFlags |= 0x10000L;
                }
                else {
                        dirtyFlags |= 0x8000L;
                }
            }
            if((dirtyFlags & 0x14L) != 0) {
                if(deviceHasBattery) {
                        dirtyFlags |= 0x4000L;
                }
                else {
                        dirtyFlags |= 0x2000L;
                }
            }


                // read device.failedOTA() ? View.VISIBLE : View.GONE
                deviceFailedOTAViewVISIBLEViewGONE = ((deviceFailedOTA) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
                // read device.deviceActivated ? View.VISIBLE : View.GONE
                deviceDeviceActivatedViewVISIBLEViewGONE = ((deviceDeviceActivated) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
                // read device.otaing ? View.VISIBLE : View.GONE
                deviceOtaingViewVISIBLEViewGONE = ((deviceOtaing) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
                // read String.format(@android:string/about_option, device.getDeviceTypeName())
                stringFormatAboutDeviceAndroidStringAboutOptionDeviceGetDeviceTypeName = java.lang.String.format(aboutDevice.getResources().getString(R.string.about_option), deviceGetDeviceTypeName);
        }
        if ((dirtyFlags & 0x18L) != 0) {



                // read numberOfMasks == 0
                numberOfMasksInt0 = (numberOfMasks) == (0);
            if((dirtyFlags & 0x18L) != 0) {
                if(numberOfMasksInt0) {
                        dirtyFlags |= 0x100L;
                }
                else {
                        dirtyFlags |= 0x80L;
                }
            }
        }
        // batch finished

        if ((dirtyFlags & 0x14L) != 0) {

                // read device.hasBattery() ? device.deviceActivated : false
                deviceHasBatteryDeviceDeviceActivatedBooleanFalse = ((deviceHasBattery) ? (deviceDeviceActivated) : (false));
            if((dirtyFlags & 0x14L) != 0) {
                if(deviceHasBatteryDeviceDeviceActivatedBooleanFalse) {
                        dirtyFlags |= 0x1000L;
                }
                else {
                        dirtyFlags |= 0x800L;
                }
            }


                // read device.hasBattery() ? device.deviceActivated : false ? View.VISIBLE : View.GONE
                deviceHasBatteryDeviceDeviceActivatedBooleanFalseViewVISIBLEViewGONE = ((deviceHasBatteryDeviceDeviceActivatedBooleanFalse) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        if ((dirtyFlags & 0x80L) != 0) {

                // read numberOfMasks > 1
                numberOfMasksInt1 = (numberOfMasks) > (1);
            if((dirtyFlags & 0x80L) != 0) {
                if(numberOfMasksInt1) {
                        dirtyFlags |= 0x40L;
                }
                else {
                        dirtyFlags |= 0x20L;
                }
            }


                // read numberOfMasks > 1 ? @android:string/masking_edit_masks : @android:string/masking_edit_mask
                numberOfMasksInt1EditMasksLayoutAndroidStringMaskingEditMasksEditMasksLayoutAndroidStringMaskingEditMask = ((numberOfMasksInt1) ? (editMasksLayout.getResources().getString(R.string.masking_edit_masks)) : (editMasksLayout.getResources().getString(R.string.masking_edit_mask)));
        }

        if ((dirtyFlags & 0x18L) != 0) {

                // read numberOfMasks == 0 ? @android:string/masking_create_mask : numberOfMasks > 1 ? @android:string/masking_edit_masks : @android:string/masking_edit_mask
                numberOfMasksInt0EditMasksLayoutAndroidStringMaskingCreateMaskNumberOfMasksInt1EditMasksLayoutAndroidStringMaskingEditMasksEditMasksLayoutAndroidStringMaskingEditMask = ((numberOfMasksInt0) ? (editMasksLayout.getResources().getString(R.string.masking_create_mask)) : (numberOfMasksInt1EditMasksLayoutAndroidStringMaskingEditMasksEditMasksLayoutAndroidStringMaskingEditMask));
        }
        // batch finished
        if ((dirtyFlags & 0x14L) != 0) {
            // api target 1

            this.aboutDevice.setText(stringFormatAboutDeviceAndroidStringAboutOptionDeviceGetDeviceTypeName);
            this.batterySettings.setVisibility(deviceHasBatteryDeviceDeviceActivatedBooleanFalseViewVISIBLEViewGONE);
            this.changeConnection.setVisibility(deviceDeviceActivatedViewVISIBLEViewGONE);
            this.deviceDetailsLayout.setVisibility(deviceDeviceActivatedViewVISIBLEViewGONE);
            this.mboundView1.setVisibility(deviceFailedOTAViewVISIBLEViewGONE);
            this.otaStatusBackGround.setVisibility(deviceOtaingViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x18L) != 0) {
            // api target 1

            android.databinding.adapters.TextViewBindingAdapter.setText(this.editMasksLayout, numberOfMasksInt0EditMasksLayoutAndroidStringMaskingCreateMaskNumberOfMasksInt1EditMasksLayoutAndroidStringMaskingEditMasksEditMasksLayoutAndroidStringMaskingEditMask);
        }
        executeBindingsOn(sensorDataLayout);
        executeBindingsOn(sensorDataFlexLayout);
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static FragmentEditDeviceBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentEditDeviceBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentEditDeviceBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_edit_device, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentEditDeviceBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentEditDeviceBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_edit_device, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentEditDeviceBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentEditDeviceBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_edit_device_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentEditDeviceBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): sensorDataLayout
        flag 1 (0x2L): sensorDataFlexLayout
        flag 2 (0x3L): device
        flag 3 (0x4L): numberOfMasks
        flag 4 (0x5L): null
        flag 5 (0x6L): numberOfMasks > 1 ? @android:string/masking_edit_masks : @android:string/masking_edit_mask
        flag 6 (0x7L): numberOfMasks > 1 ? @android:string/masking_edit_masks : @android:string/masking_edit_mask
        flag 7 (0x8L): numberOfMasks == 0 ? @android:string/masking_create_mask : numberOfMasks > 1 ? @android:string/masking_edit_masks : @android:string/masking_edit_mask
        flag 8 (0x9L): numberOfMasks == 0 ? @android:string/masking_create_mask : numberOfMasks > 1 ? @android:string/masking_edit_masks : @android:string/masking_edit_mask
        flag 9 (0xaL): device.deviceActivated ? View.VISIBLE : View.GONE
        flag 10 (0xbL): device.deviceActivated ? View.VISIBLE : View.GONE
        flag 11 (0xcL): device.hasBattery() ? device.deviceActivated : false ? View.VISIBLE : View.GONE
        flag 12 (0xdL): device.hasBattery() ? device.deviceActivated : false ? View.VISIBLE : View.GONE
        flag 13 (0xeL): device.hasBattery() ? device.deviceActivated : false
        flag 14 (0xfL): device.hasBattery() ? device.deviceActivated : false
        flag 15 (0x10L): device.otaing ? View.VISIBLE : View.GONE
        flag 16 (0x11L): device.otaing ? View.VISIBLE : View.GONE
        flag 17 (0x12L): device.failedOTA() ? View.VISIBLE : View.GONE
        flag 18 (0x13L): device.failedOTA() ? View.VISIBLE : View.GONE
    flag mapping end*/
    //end
}