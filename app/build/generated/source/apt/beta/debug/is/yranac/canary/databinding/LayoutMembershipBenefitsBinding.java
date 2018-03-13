package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class LayoutMembershipBenefitsBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.header_text_view, 3);
    }
    // views
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus extendedWarrantyTextView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus headerTextView;
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    @NonNull
    private final is.yranac.canary.ui.views.TextViewPlus mboundView2;
    // variables
    @Nullable
    private android.view.View mV;
    @Nullable
    private is.yranac.canary.model.location.Location mLocation;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public LayoutMembershipBenefitsBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 4, sIncludes, sViewsWithIds);
        this.extendedWarrantyTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[1];
        this.extendedWarrantyTextView.setTag(null);
        this.headerTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[3];
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView2 = (is.yranac.canary.ui.views.TextViewPlus) bindings[2];
        this.mboundView2.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x4L;
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
        if (BR.v == variableId) {
            setV((android.view.View) variable);
        }
        else if (BR.location == variableId) {
            setLocation((is.yranac.canary.model.location.Location) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setV(@Nullable android.view.View V) {
        this.mV = V;
    }
    @Nullable
    public android.view.View getV() {
        return mV;
    }
    public void setLocation(@Nullable is.yranac.canary.model.location.Location Location) {
        this.mLocation = Location;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.location);
        super.requestRebind();
    }
    @Nullable
    public is.yranac.canary.model.location.Location getLocation() {
        return mLocation;
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
        int locationUnitedStatesVVISIBLEVGONE = 0;
        is.yranac.canary.model.location.Location location = mLocation;
        boolean locationUnitedStates = false;

        if ((dirtyFlags & 0x6L) != 0) {



                if (location != null) {
                    // read location.unitedStates
                    locationUnitedStates = location.isUnitedStates();
                }
            if((dirtyFlags & 0x6L) != 0) {
                if(locationUnitedStates) {
                        dirtyFlags |= 0x10L;
                }
                else {
                        dirtyFlags |= 0x8L;
                }
            }


                // read location.unitedStates ? View.VISIBLE : View.GONE
                locationUnitedStatesVVISIBLEVGONE = ((locationUnitedStates) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        // batch finished
        if ((dirtyFlags & 0x6L) != 0) {
            // api target 1

            this.extendedWarrantyTextView.setVisibility(locationUnitedStatesVVISIBLEVGONE);
            this.mboundView2.setVisibility(locationUnitedStatesVVISIBLEVGONE);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static LayoutMembershipBenefitsBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static LayoutMembershipBenefitsBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<LayoutMembershipBenefitsBinding>inflate(inflater, is.yranac.canary.R.layout.layout_membership_benefits, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static LayoutMembershipBenefitsBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static LayoutMembershipBenefitsBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.layout_membership_benefits, null, false), bindingComponent);
    }
    @NonNull
    public static LayoutMembershipBenefitsBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static LayoutMembershipBenefitsBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/layout_membership_benefits_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new LayoutMembershipBenefitsBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): v
        flag 1 (0x2L): location
        flag 2 (0x3L): null
        flag 3 (0x4L): location.unitedStates ? View.VISIBLE : View.GONE
        flag 4 (0x5L): location.unitedStates ? View.VISIBLE : View.GONE
    flag mapping end*/
    //end
}