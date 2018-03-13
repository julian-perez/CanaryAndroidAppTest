package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentSettingsHomeModeBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.mode_dsc_text_view, 3);
        sViewsWithIds.put(R.id.night_mode_schedule_layout, 4);
        sViewsWithIds.put(R.id.night_mode_toggle_layout, 5);
        sViewsWithIds.put(R.id.night_mode_schedule_toggle, 6);
        sViewsWithIds.put(R.id.start_night_mode, 7);
        sViewsWithIds.put(R.id.night_mode_start_time, 8);
        sViewsWithIds.put(R.id.end_night_mode, 9);
        sViewsWithIds.put(R.id.night_mode_end_time, 10);
        sViewsWithIds.put(R.id.segmented_group, 11);
        sViewsWithIds.put(R.id.set_all_layout, 12);
        sViewsWithIds.put(R.id.set_each_layout, 13);
        sViewsWithIds.put(R.id.selected_level, 14);
        sViewsWithIds.put(R.id.notification_settings_layout, 15);
    }
    // views
    @NonNull
    public final android.widget.RelativeLayout endNightMode;
    @NonNull
    private final android.widget.ScrollView mboundView0;
    @NonNull
    private final android.widget.LinearLayout mboundView1;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus modeDscTextView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus nightModeEndTime;
    @NonNull
    public final android.widget.LinearLayout nightModeScheduleLayout;
    @NonNull
    public final android.support.v7.widget.SwitchCompat nightModeScheduleToggle;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus nightModeStartTime;
    @NonNull
    public final android.widget.LinearLayout nightModeTimeLayout;
    @NonNull
    public final android.widget.RelativeLayout nightModeToggleLayout;
    @NonNull
    public final android.widget.LinearLayout notificationSettingsLayout;
    @NonNull
    public final android.support.percent.PercentRelativeLayout segmentedGroup;
    @NonNull
    public final android.view.View selectedLevel;
    @NonNull
    public final android.widget.LinearLayout setAllLayout;
    @NonNull
    public final android.widget.LinearLayout setEachLayout;
    @NonNull
    public final android.widget.RelativeLayout startNightMode;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentSettingsHomeModeBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 16, sIncludes, sViewsWithIds);
        this.endNightMode = (android.widget.RelativeLayout) bindings[9];
        this.mboundView0 = (android.widget.ScrollView) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView1 = (android.widget.LinearLayout) bindings[1];
        this.mboundView1.setTag(null);
        this.modeDscTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[3];
        this.nightModeEndTime = (is.yranac.canary.ui.views.TextViewPlus) bindings[10];
        this.nightModeScheduleLayout = (android.widget.LinearLayout) bindings[4];
        this.nightModeScheduleToggle = (android.support.v7.widget.SwitchCompat) bindings[6];
        this.nightModeStartTime = (is.yranac.canary.ui.views.TextViewPlus) bindings[8];
        this.nightModeTimeLayout = (android.widget.LinearLayout) bindings[2];
        this.nightModeTimeLayout.setTag(null);
        this.nightModeToggleLayout = (android.widget.RelativeLayout) bindings[5];
        this.notificationSettingsLayout = (android.widget.LinearLayout) bindings[15];
        this.segmentedGroup = (android.support.percent.PercentRelativeLayout) bindings[11];
        this.selectedLevel = (android.view.View) bindings[14];
        this.setAllLayout = (android.widget.LinearLayout) bindings[12];
        this.setEachLayout = (android.widget.LinearLayout) bindings[13];
        this.startNightMode = (android.widget.RelativeLayout) bindings[7];
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
    public static FragmentSettingsHomeModeBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsHomeModeBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentSettingsHomeModeBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_settings_home_mode, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentSettingsHomeModeBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsHomeModeBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_settings_home_mode, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentSettingsHomeModeBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsHomeModeBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_settings_home_mode_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentSettingsHomeModeBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}