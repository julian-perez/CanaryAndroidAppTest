package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentSettingsGeofenceRadiusBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.top_layout, 1);
        sViewsWithIds.put(R.id.set_size_text_view, 2);
        sViewsWithIds.put(R.id.set_size_dsc_text_view, 3);
        sViewsWithIds.put(R.id.small_layout, 4);
        sViewsWithIds.put(R.id.medium_layout, 5);
        sViewsWithIds.put(R.id.large_layout, 6);
        sViewsWithIds.put(R.id.selected_level, 7);
        sViewsWithIds.put(R.id.map_view, 8);
        sViewsWithIds.put(R.id.save_btn, 9);
    }
    // views
    @NonNull
    public final android.widget.LinearLayout largeLayout;
    @NonNull
    public final com.google.android.gms.maps.MapView mapView;
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    @NonNull
    public final android.widget.LinearLayout mediumLayout;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus saveBtn;
    @NonNull
    public final android.view.View selectedLevel;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus setSizeDscTextView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus setSizeTextView;
    @NonNull
    public final android.widget.LinearLayout smallLayout;
    @NonNull
    public final android.widget.LinearLayout topLayout;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentSettingsGeofenceRadiusBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 10, sIncludes, sViewsWithIds);
        this.largeLayout = (android.widget.LinearLayout) bindings[6];
        this.mapView = (com.google.android.gms.maps.MapView) bindings[8];
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mediumLayout = (android.widget.LinearLayout) bindings[5];
        this.saveBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[9];
        this.selectedLevel = (android.view.View) bindings[7];
        this.setSizeDscTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[3];
        this.setSizeTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[2];
        this.smallLayout = (android.widget.LinearLayout) bindings[4];
        this.topLayout = (android.widget.LinearLayout) bindings[1];
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
    public static FragmentSettingsGeofenceRadiusBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsGeofenceRadiusBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentSettingsGeofenceRadiusBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_settings_geofence_radius, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentSettingsGeofenceRadiusBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsGeofenceRadiusBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_settings_geofence_radius, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentSettingsGeofenceRadiusBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsGeofenceRadiusBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_settings_geofence_radius_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentSettingsGeofenceRadiusBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}