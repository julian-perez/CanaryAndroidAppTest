package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentSettingsPresenceNotificationsBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.header_layout, 1);
        sViewsWithIds.put(R.id.presences_toggle_layout, 2);
        sViewsWithIds.put(R.id.presences_checkbox, 3);
        sViewsWithIds.put(R.id.auto_mode_disarm_change_desc, 4);
        sViewsWithIds.put(R.id.list_view, 5);
    }
    // views
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus autoModeDisarmChangeDesc;
    @NonNull
    public final android.widget.LinearLayout headerLayout;
    @NonNull
    public final android.widget.ListView listView;
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    @NonNull
    public final android.support.v7.widget.SwitchCompat presencesCheckbox;
    @NonNull
    public final android.widget.RelativeLayout presencesToggleLayout;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentSettingsPresenceNotificationsBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 6, sIncludes, sViewsWithIds);
        this.autoModeDisarmChangeDesc = (is.yranac.canary.ui.views.TextViewPlus) bindings[4];
        this.headerLayout = (android.widget.LinearLayout) bindings[1];
        this.listView = (android.widget.ListView) bindings[5];
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.presencesCheckbox = (android.support.v7.widget.SwitchCompat) bindings[3];
        this.presencesToggleLayout = (android.widget.RelativeLayout) bindings[2];
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
    public static FragmentSettingsPresenceNotificationsBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsPresenceNotificationsBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentSettingsPresenceNotificationsBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_settings_presence_notifications, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentSettingsPresenceNotificationsBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsPresenceNotificationsBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_settings_presence_notifications, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentSettingsPresenceNotificationsBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsPresenceNotificationsBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_settings_presence_notifications_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentSettingsPresenceNotificationsBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}