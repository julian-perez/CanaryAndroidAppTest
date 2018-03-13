package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class LayoutDeviceStataticsBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new android.databinding.ViewDataBinding.IncludedLayouts(11);
        sIncludes.setIncludes(1, 
            new String[] {"layout_homehealth", "layout_sensor_flex"},
            new int[] {2, 3},
            new int[] {R.layout.layout_homehealth, R.layout.layout_sensor_flex});
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.device_name, 4);
        sViewsWithIds.put(R.id.device_status_container, 5);
        sViewsWithIds.put(R.id.watch_live_button, 6);
        sViewsWithIds.put(R.id.device_offine_text_view, 7);
        sViewsWithIds.put(R.id.device_privacy_text_view, 8);
        sViewsWithIds.put(R.id.sensor_container, 9);
        sViewsWithIds.put(R.id.spotlight_message, 10);
    }
    // views
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus deviceName;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus deviceOffineTextView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus devicePrivacyTextView;
    @NonNull
    public final android.widget.FrameLayout deviceStatusContainer;
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    @NonNull
    public final android.widget.FrameLayout sensorContainer;
    @NonNull
    public final android.widget.LinearLayout sensorDataContainer;
    @Nullable
    public final is.yranac.canary.databinding.LayoutHomehealthBinding sensorDataLayout;
    @NonNull
    public final is.yranac.canary.ui.views.spotlightview.SpotlightTextView spotlightMessage;
    @Nullable
    public final is.yranac.canary.databinding.LayoutSensorFlexBinding statisticsFlexContainer;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus watchLiveButton;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public LayoutDeviceStataticsBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 2);
        final Object[] bindings = mapBindings(bindingComponent, root, 11, sIncludes, sViewsWithIds);
        this.deviceName = (is.yranac.canary.ui.views.TextViewPlus) bindings[4];
        this.deviceOffineTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[7];
        this.devicePrivacyTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[8];
        this.deviceStatusContainer = (android.widget.FrameLayout) bindings[5];
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.sensorContainer = (android.widget.FrameLayout) bindings[9];
        this.sensorDataContainer = (android.widget.LinearLayout) bindings[1];
        this.sensorDataContainer.setTag(null);
        this.sensorDataLayout = (is.yranac.canary.databinding.LayoutHomehealthBinding) bindings[2];
        setContainedBinding(this.sensorDataLayout);
        this.spotlightMessage = (is.yranac.canary.ui.views.spotlightview.SpotlightTextView) bindings[10];
        this.statisticsFlexContainer = (is.yranac.canary.databinding.LayoutSensorFlexBinding) bindings[3];
        setContainedBinding(this.statisticsFlexContainer);
        this.watchLiveButton = (is.yranac.canary.ui.views.ButtonPlus) bindings[6];
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x4L;
        }
        sensorDataLayout.invalidateAll();
        statisticsFlexContainer.invalidateAll();
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
        if (statisticsFlexContainer.hasPendingBindings()) {
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
                return onChangeSensorDataLayout((is.yranac.canary.databinding.LayoutHomehealthBinding) object, fieldId);
            case 1 :
                return onChangeStatisticsFlexContainer((is.yranac.canary.databinding.LayoutSensorFlexBinding) object, fieldId);
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
    private boolean onChangeStatisticsFlexContainer(is.yranac.canary.databinding.LayoutSensorFlexBinding StatisticsFlexContainer, int fieldId) {
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
        executeBindingsOn(sensorDataLayout);
        executeBindingsOn(statisticsFlexContainer);
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static LayoutDeviceStataticsBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static LayoutDeviceStataticsBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<LayoutDeviceStataticsBinding>inflate(inflater, is.yranac.canary.R.layout.layout_device_statatics, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static LayoutDeviceStataticsBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static LayoutDeviceStataticsBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.layout_device_statatics, null, false), bindingComponent);
    }
    @NonNull
    public static LayoutDeviceStataticsBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static LayoutDeviceStataticsBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/layout_device_statatics_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new LayoutDeviceStataticsBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): sensorDataLayout
        flag 1 (0x2L): statisticsFlexContainer
        flag 2 (0x3L): null
    flag mapping end*/
    //end
}