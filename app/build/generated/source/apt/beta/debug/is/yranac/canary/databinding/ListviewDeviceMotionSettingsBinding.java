package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ListviewDeviceMotionSettingsBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.disarm_text_view, 1);
        sViewsWithIds.put(R.id.notification_progress_bar, 2);
        sViewsWithIds.put(R.id.slider_value, 3);
        sViewsWithIds.put(R.id.motion_notification_dsc_text_view, 4);
    }
    // views
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus disarmTextView;
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus motionNotificationDscTextView;
    @NonNull
    public final is.yranac.canary.ui.views.TickSeekBar notificationProgressBar;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus sliderValue;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ListviewDeviceMotionSettingsBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 5, sIncludes, sViewsWithIds);
        this.disarmTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[1];
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.motionNotificationDscTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[4];
        this.notificationProgressBar = (is.yranac.canary.ui.views.TickSeekBar) bindings[2];
        this.sliderValue = (is.yranac.canary.ui.views.TextViewPlus) bindings[3];
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
    public static ListviewDeviceMotionSettingsBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ListviewDeviceMotionSettingsBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<ListviewDeviceMotionSettingsBinding>inflate(inflater, is.yranac.canary.R.layout.listview_device_motion_settings, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static ListviewDeviceMotionSettingsBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ListviewDeviceMotionSettingsBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.listview_device_motion_settings, null, false), bindingComponent);
    }
    @NonNull
    public static ListviewDeviceMotionSettingsBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ListviewDeviceMotionSettingsBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/listview_device_motion_settings_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new ListviewDeviceMotionSettingsBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}