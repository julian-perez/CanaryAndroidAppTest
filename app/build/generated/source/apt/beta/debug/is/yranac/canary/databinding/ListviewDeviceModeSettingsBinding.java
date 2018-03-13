package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ListviewDeviceModeSettingsBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.disarm_text_view, 4);
        sViewsWithIds.put(R.id.in_mode_text_view, 5);
        sViewsWithIds.put(R.id.record_mode_layout, 6);
        sViewsWithIds.put(R.id.radio_record, 7);
        sViewsWithIds.put(R.id.notification_toggle, 8);
        sViewsWithIds.put(R.id.private_mode_layout, 9);
        sViewsWithIds.put(R.id.radio_private, 10);
        sViewsWithIds.put(R.id.watch_live_toggle, 11);
    }
    // views
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus disarmTextView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus inModeTextView;
    @NonNull
    private final android.widget.FrameLayout mboundView0;
    @NonNull
    private final android.widget.LinearLayout mboundView1;
    @NonNull
    public final android.widget.LinearLayout notificationModeLayout;
    @NonNull
    public final android.support.v7.widget.SwitchCompat notificationToggle;
    @NonNull
    public final android.widget.RelativeLayout privateModeLayout;
    @NonNull
    public final android.widget.RadioButton radioPrivate;
    @NonNull
    public final android.widget.RadioButton radioRecord;
    @NonNull
    public final android.widget.RelativeLayout recordModeLayout;
    @NonNull
    public final android.widget.LinearLayout watchLiveModeLayout;
    @NonNull
    public final android.support.v7.widget.SwitchCompat watchLiveToggle;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ListviewDeviceModeSettingsBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 12, sIncludes, sViewsWithIds);
        this.disarmTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[4];
        this.inModeTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[5];
        this.mboundView0 = (android.widget.FrameLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView1 = (android.widget.LinearLayout) bindings[1];
        this.mboundView1.setTag(null);
        this.notificationModeLayout = (android.widget.LinearLayout) bindings[2];
        this.notificationModeLayout.setTag(null);
        this.notificationToggle = (android.support.v7.widget.SwitchCompat) bindings[8];
        this.privateModeLayout = (android.widget.RelativeLayout) bindings[9];
        this.radioPrivate = (android.widget.RadioButton) bindings[10];
        this.radioRecord = (android.widget.RadioButton) bindings[7];
        this.recordModeLayout = (android.widget.RelativeLayout) bindings[6];
        this.watchLiveModeLayout = (android.widget.LinearLayout) bindings[3];
        this.watchLiveModeLayout.setTag(null);
        this.watchLiveToggle = (android.support.v7.widget.SwitchCompat) bindings[11];
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
    public static ListviewDeviceModeSettingsBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ListviewDeviceModeSettingsBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<ListviewDeviceModeSettingsBinding>inflate(inflater, is.yranac.canary.R.layout.listview_device_mode_settings, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static ListviewDeviceModeSettingsBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ListviewDeviceModeSettingsBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.listview_device_mode_settings, null, false), bindingComponent);
    }
    @NonNull
    public static ListviewDeviceModeSettingsBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ListviewDeviceModeSettingsBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/listview_device_mode_settings_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new ListviewDeviceModeSettingsBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}