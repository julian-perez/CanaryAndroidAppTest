package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class MapHeaderBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.location_address, 1);
        sViewsWithIds.put(R.id.membership_status, 2);
        sViewsWithIds.put(R.id.bottom_buttons, 3);
        sViewsWithIds.put(R.id.membership_layout, 4);
        sViewsWithIds.put(R.id.settings_layout, 5);
        sViewsWithIds.put(R.id.selected_view, 6);
        sViewsWithIds.put(R.id.line, 7);
    }
    // views
    @NonNull
    public final android.widget.LinearLayout bottomButtons;
    @NonNull
    public final android.view.View line;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus locationAddress;
    @NonNull
    public final android.widget.RelativeLayout mapContainer;
    @NonNull
    public final android.widget.LinearLayout membershipLayout;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus membershipStatus;
    @NonNull
    public final android.view.View selectedView;
    @NonNull
    public final android.widget.LinearLayout settingsLayout;
    // variables
    @Nullable
    private is.yranac.canary.model.subscription.Subscription mSubscription;
    @Nullable
    private android.view.View mV;
    @Nullable
    private is.yranac.canary.model.location.Location mLocation;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public MapHeaderBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 8, sIncludes, sViewsWithIds);
        this.bottomButtons = (android.widget.LinearLayout) bindings[3];
        this.line = (android.view.View) bindings[7];
        this.locationAddress = (is.yranac.canary.ui.views.TextViewPlus) bindings[1];
        this.mapContainer = (android.widget.RelativeLayout) bindings[0];
        this.mapContainer.setTag(null);
        this.membershipLayout = (android.widget.LinearLayout) bindings[4];
        this.membershipStatus = (is.yranac.canary.ui.views.TextViewPlus) bindings[2];
        this.selectedView = (android.view.View) bindings[6];
        this.settingsLayout = (android.widget.LinearLayout) bindings[5];
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x8L;
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
        if (BR.subscription == variableId) {
            setSubscription((is.yranac.canary.model.subscription.Subscription) variable);
        }
        else if (BR.v == variableId) {
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

    public void setSubscription(@Nullable is.yranac.canary.model.subscription.Subscription Subscription) {
        this.mSubscription = Subscription;
    }
    @Nullable
    public is.yranac.canary.model.subscription.Subscription getSubscription() {
        return mSubscription;
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
        // batch finished
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static MapHeaderBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static MapHeaderBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<MapHeaderBinding>inflate(inflater, is.yranac.canary.R.layout.map_header, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static MapHeaderBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static MapHeaderBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.map_header, null, false), bindingComponent);
    }
    @NonNull
    public static MapHeaderBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static MapHeaderBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/map_header_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new MapHeaderBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): subscription
        flag 1 (0x2L): v
        flag 2 (0x3L): location
        flag 3 (0x4L): null
    flag mapping end*/
    //end
}