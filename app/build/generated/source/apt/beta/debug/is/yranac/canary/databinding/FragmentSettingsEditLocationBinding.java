package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentSettingsEditLocationBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.map_container, 1);
        sViewsWithIds.put(R.id.map_view, 2);
        sViewsWithIds.put(R.id.myLocationButton, 3);
        sViewsWithIds.put(R.id.hint_button, 4);
        sViewsWithIds.put(R.id.edit_location_textarea, 5);
        sViewsWithIds.put(R.id.location_name, 6);
        sViewsWithIds.put(R.id.location_address, 7);
        sViewsWithIds.put(R.id.location_address_two, 8);
        sViewsWithIds.put(R.id.location_city, 9);
        sViewsWithIds.put(R.id.location_state, 10);
        sViewsWithIds.put(R.id.location_postal_code, 11);
        sViewsWithIds.put(R.id.location_country, 12);
    }
    // views
    @NonNull
    public final android.widget.ScrollView editLocationTextarea;
    @NonNull
    public final android.widget.ImageButton hintButton;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel locationAddress;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel locationAddressTwo;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel locationCity;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel locationCountry;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel locationName;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel locationPostalCode;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel locationState;
    @NonNull
    public final android.widget.FrameLayout mapContainer;
    @NonNull
    public final com.google.android.gms.maps.MapView mapView;
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    @NonNull
    public final android.widget.ImageButton myLocationButton;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentSettingsEditLocationBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 13, sIncludes, sViewsWithIds);
        this.editLocationTextarea = (android.widget.ScrollView) bindings[5];
        this.hintButton = (android.widget.ImageButton) bindings[4];
        this.locationAddress = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[7];
        this.locationAddressTwo = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[8];
        this.locationCity = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[9];
        this.locationCountry = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[12];
        this.locationName = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[6];
        this.locationPostalCode = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[11];
        this.locationState = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[10];
        this.mapContainer = (android.widget.FrameLayout) bindings[1];
        this.mapView = (com.google.android.gms.maps.MapView) bindings[2];
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.myLocationButton = (android.widget.ImageButton) bindings[3];
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
    public static FragmentSettingsEditLocationBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsEditLocationBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentSettingsEditLocationBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_settings_edit_location, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentSettingsEditLocationBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsEditLocationBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_settings_edit_location, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentSettingsEditLocationBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsEditLocationBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_settings_edit_location_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentSettingsEditLocationBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}