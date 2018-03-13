package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentSettingsEditAddressBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.location_name, 2);
        sViewsWithIds.put(R.id.location_address, 3);
        sViewsWithIds.put(R.id.location_address_two, 4);
        sViewsWithIds.put(R.id.location_city, 5);
        sViewsWithIds.put(R.id.location_state, 6);
        sViewsWithIds.put(R.id.location_postal_code, 7);
        sViewsWithIds.put(R.id.location_country, 8);
    }
    // views
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
    private final android.widget.RelativeLayout mboundView0;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus nextBtn;
    // variables
    @Nullable
    private boolean mIsLocationSetup;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentSettingsEditAddressBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 9, sIncludes, sViewsWithIds);
        this.locationAddress = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[3];
        this.locationAddressTwo = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[4];
        this.locationCity = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[5];
        this.locationCountry = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[8];
        this.locationName = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[2];
        this.locationPostalCode = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[7];
        this.locationState = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[6];
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.nextBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[1];
        this.nextBtn.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
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
        if (BR.isLocationSetup == variableId) {
            setIsLocationSetup((boolean) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setIsLocationSetup(boolean IsLocationSetup) {
        this.mIsLocationSetup = IsLocationSetup;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.isLocationSetup);
        super.requestRebind();
    }
    public boolean getIsLocationSetup() {
        return mIsLocationSetup;
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
        boolean isLocationSetup = mIsLocationSetup;
        int isLocationSetupViewVISIBLEViewGONE = 0;

        if ((dirtyFlags & 0x3L) != 0) {

            if((dirtyFlags & 0x3L) != 0) {
                if(isLocationSetup) {
                        dirtyFlags |= 0x8L;
                }
                else {
                        dirtyFlags |= 0x4L;
                }
            }


                // read isLocationSetup ? View.VISIBLE : View.GONE
                isLocationSetupViewVISIBLEViewGONE = ((isLocationSetup) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            this.nextBtn.setVisibility(isLocationSetupViewVISIBLEViewGONE);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static FragmentSettingsEditAddressBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsEditAddressBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentSettingsEditAddressBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_settings_edit_address, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentSettingsEditAddressBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsEditAddressBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_settings_edit_address, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentSettingsEditAddressBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsEditAddressBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_settings_edit_address_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentSettingsEditAddressBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): isLocationSetup
        flag 1 (0x2L): null
        flag 2 (0x3L): isLocationSetup ? View.VISIBLE : View.GONE
        flag 3 (0x4L): isLocationSetup ? View.VISIBLE : View.GONE
    flag mapping end*/
    //end
}