package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentSettingsGeofencePositionBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.top_layout, 1);
        sViewsWithIds.put(R.id.set_postion_text_view, 2);
        sViewsWithIds.put(R.id.set_postion_dsc_text_view, 3);
        sViewsWithIds.put(R.id.map_view, 4);
        sViewsWithIds.put(R.id.map_locate_btn, 5);
        sViewsWithIds.put(R.id.map_style_btn, 6);
        sViewsWithIds.put(R.id.save_btn, 7);
    }
    // views
    @NonNull
    public final android.support.v7.widget.AppCompatImageView mapLocateBtn;
    @NonNull
    public final android.support.v7.widget.AppCompatImageView mapStyleBtn;
    @NonNull
    public final com.google.android.gms.maps.MapView mapView;
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus saveBtn;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus setPostionDscTextView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus setPostionTextView;
    @NonNull
    public final android.widget.LinearLayout topLayout;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentSettingsGeofencePositionBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 8, sIncludes, sViewsWithIds);
        this.mapLocateBtn = (android.support.v7.widget.AppCompatImageView) bindings[5];
        this.mapStyleBtn = (android.support.v7.widget.AppCompatImageView) bindings[6];
        this.mapView = (com.google.android.gms.maps.MapView) bindings[4];
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.saveBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[7];
        this.setPostionDscTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[3];
        this.setPostionTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[2];
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
    public static FragmentSettingsGeofencePositionBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsGeofencePositionBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentSettingsGeofencePositionBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_settings_geofence_position, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentSettingsGeofencePositionBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsGeofencePositionBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_settings_geofence_position, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentSettingsGeofencePositionBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsGeofencePositionBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_settings_geofence_position_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentSettingsGeofencePositionBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}