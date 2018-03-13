package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentSettingsLocationBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.manage_membership, 1);
        sViewsWithIds.put(R.id.edit_address, 2);
        sViewsWithIds.put(R.id.edit_geofence, 3);
        sViewsWithIds.put(R.id.emergency_numbers, 4);
        sViewsWithIds.put(R.id.insurance, 5);
        sViewsWithIds.put(R.id.remove_location_btn, 6);
    }
    // views
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel editAddress;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel editGeofence;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel emergencyNumbers;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel insurance;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel manageMembership;
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus removeLocationBtn;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentSettingsLocationBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 7, sIncludes, sViewsWithIds);
        this.editAddress = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[2];
        this.editGeofence = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[3];
        this.emergencyNumbers = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[4];
        this.insurance = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[5];
        this.manageMembership = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[1];
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.removeLocationBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[6];
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
    public static FragmentSettingsLocationBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsLocationBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentSettingsLocationBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_settings_location, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentSettingsLocationBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsLocationBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_settings_location, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentSettingsLocationBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsLocationBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_settings_location_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentSettingsLocationBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}