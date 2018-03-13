package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentSettingsFlexConnectivityBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.wifi_level_text_view, 1);
        sViewsWithIds.put(R.id.wifi_description_text_view, 2);
        sViewsWithIds.put(R.id.flex_connectivity_btn, 3);
        sViewsWithIds.put(R.id.wifi_notification_settings_btn, 4);
        sViewsWithIds.put(R.id.wifi_icon_background, 5);
        sViewsWithIds.put(R.id.wifi_icon_image_view, 6);
        sViewsWithIds.put(R.id.battery_level_label_text_view, 7);
        sViewsWithIds.put(R.id.battery_level_text_view, 8);
        sViewsWithIds.put(R.id.battery_description_text_view, 9);
        sViewsWithIds.put(R.id.flex_battery_btn, 10);
        sViewsWithIds.put(R.id.battery_notification_settings_btn, 11);
        sViewsWithIds.put(R.id.battery_icon_background, 12);
        sViewsWithIds.put(R.id.statistics_battery_view, 13);
    }
    // views
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus batteryDescriptionTextView;
    @NonNull
    public final is.yranac.canary.ui.views.CircleView batteryIconBackground;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus batteryLevelLabelTextView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus batteryLevelTextView;
    @NonNull
    public final android.support.v7.widget.AppCompatImageView batteryNotificationSettingsBtn;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus flexBatteryBtn;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus flexConnectivityBtn;
    @NonNull
    private final android.widget.ScrollView mboundView0;
    @NonNull
    public final is.yranac.canary.ui.views.BatteryIndicator statisticsBatteryView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus wifiDescriptionTextView;
    @NonNull
    public final is.yranac.canary.ui.views.CircleView wifiIconBackground;
    @NonNull
    public final android.support.v7.widget.AppCompatImageView wifiIconImageView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus wifiLevelTextView;
    @NonNull
    public final android.support.v7.widget.AppCompatImageView wifiNotificationSettingsBtn;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentSettingsFlexConnectivityBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 14, sIncludes, sViewsWithIds);
        this.batteryDescriptionTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[9];
        this.batteryIconBackground = (is.yranac.canary.ui.views.CircleView) bindings[12];
        this.batteryLevelLabelTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[7];
        this.batteryLevelTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[8];
        this.batteryNotificationSettingsBtn = (android.support.v7.widget.AppCompatImageView) bindings[11];
        this.flexBatteryBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[10];
        this.flexConnectivityBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[3];
        this.mboundView0 = (android.widget.ScrollView) bindings[0];
        this.mboundView0.setTag(null);
        this.statisticsBatteryView = (is.yranac.canary.ui.views.BatteryIndicator) bindings[13];
        this.wifiDescriptionTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[2];
        this.wifiIconBackground = (is.yranac.canary.ui.views.CircleView) bindings[5];
        this.wifiIconImageView = (android.support.v7.widget.AppCompatImageView) bindings[6];
        this.wifiLevelTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[1];
        this.wifiNotificationSettingsBtn = (android.support.v7.widget.AppCompatImageView) bindings[4];
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
    public static FragmentSettingsFlexConnectivityBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsFlexConnectivityBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentSettingsFlexConnectivityBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_settings_flex_connectivity, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentSettingsFlexConnectivityBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsFlexConnectivityBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_settings_flex_connectivity, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentSettingsFlexConnectivityBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsFlexConnectivityBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_settings_flex_connectivity_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentSettingsFlexConnectivityBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}