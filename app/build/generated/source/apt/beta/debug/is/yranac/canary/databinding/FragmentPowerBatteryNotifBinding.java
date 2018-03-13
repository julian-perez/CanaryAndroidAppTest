package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentPowerBatteryNotifBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.power_source_header, 1);
        sViewsWithIds.put(R.id.power_source_check_box, 2);
        sViewsWithIds.put(R.id.power_source_desc, 3);
        sViewsWithIds.put(R.id.power_source_body, 4);
        sViewsWithIds.put(R.id.power_source_body_header, 5);
        sViewsWithIds.put(R.id.list_view, 6);
        sViewsWithIds.put(R.id.battery_full_header, 7);
        sViewsWithIds.put(R.id.divider_bottom, 8);
        sViewsWithIds.put(R.id.full_battery_body_header, 9);
        sViewsWithIds.put(R.id.full_battery_check_box, 10);
        sViewsWithIds.put(R.id.battery_full_desc, 11);
        sViewsWithIds.put(R.id.divider_tail, 12);
    }
    // views
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus batteryFullDesc;
    @NonNull
    public final android.widget.RelativeLayout batteryFullHeader;
    @NonNull
    public final android.view.View dividerBottom;
    @NonNull
    public final android.view.View dividerTail;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus fullBatteryBodyHeader;
    @NonNull
    public final android.support.v7.widget.SwitchCompat fullBatteryCheckBox;
    @NonNull
    public final android.widget.ListView listView;
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    @NonNull
    public final android.widget.RelativeLayout powerSourceBody;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus powerSourceBodyHeader;
    @NonNull
    public final android.support.v7.widget.SwitchCompat powerSourceCheckBox;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus powerSourceDesc;
    @NonNull
    public final android.widget.RelativeLayout powerSourceHeader;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentPowerBatteryNotifBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 13, sIncludes, sViewsWithIds);
        this.batteryFullDesc = (is.yranac.canary.ui.views.TextViewPlus) bindings[11];
        this.batteryFullHeader = (android.widget.RelativeLayout) bindings[7];
        this.dividerBottom = (android.view.View) bindings[8];
        this.dividerTail = (android.view.View) bindings[12];
        this.fullBatteryBodyHeader = (is.yranac.canary.ui.views.TextViewPlus) bindings[9];
        this.fullBatteryCheckBox = (android.support.v7.widget.SwitchCompat) bindings[10];
        this.listView = (android.widget.ListView) bindings[6];
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.powerSourceBody = (android.widget.RelativeLayout) bindings[4];
        this.powerSourceBodyHeader = (is.yranac.canary.ui.views.TextViewPlus) bindings[5];
        this.powerSourceCheckBox = (android.support.v7.widget.SwitchCompat) bindings[2];
        this.powerSourceDesc = (is.yranac.canary.ui.views.TextViewPlus) bindings[3];
        this.powerSourceHeader = (android.widget.RelativeLayout) bindings[1];
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
    public static FragmentPowerBatteryNotifBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentPowerBatteryNotifBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentPowerBatteryNotifBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_power_battery_notif, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentPowerBatteryNotifBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentPowerBatteryNotifBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_power_battery_notif, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentPowerBatteryNotifBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentPowerBatteryNotifBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_power_battery_notif_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentPowerBatteryNotifBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}