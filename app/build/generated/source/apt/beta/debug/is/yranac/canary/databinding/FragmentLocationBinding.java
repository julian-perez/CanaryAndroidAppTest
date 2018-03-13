package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentLocationBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.map_container, 1);
        sViewsWithIds.put(R.id.map_view, 2);
        sViewsWithIds.put(R.id.location_button, 3);
        sViewsWithIds.put(R.id.bottom_layout, 4);
        sViewsWithIds.put(R.id.location_trail_layout, 5);
        sViewsWithIds.put(R.id.days_remaining_text, 6);
        sViewsWithIds.put(R.id.home_details_trail, 7);
        sViewsWithIds.put(R.id.location_details, 8);
        sViewsWithIds.put(R.id.membership_status, 9);
        sViewsWithIds.put(R.id.home_details, 10);
        sViewsWithIds.put(R.id.line, 11);
        sViewsWithIds.put(R.id.manage_devices, 12);
        sViewsWithIds.put(R.id.manage_members, 13);
    }
    // views
    @NonNull
    public final android.widget.LinearLayout bottomLayout;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus daysRemainingText;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus homeDetails;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus homeDetailsTrail;
    @NonNull
    public final android.view.View line;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus locationButton;
    @NonNull
    public final android.widget.FrameLayout locationDetails;
    @NonNull
    public final android.widget.LinearLayout locationTrailLayout;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel manageDevices;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus manageMembers;
    @NonNull
    public final android.widget.RelativeLayout mapContainer;
    @NonNull
    public final com.google.android.gms.maps.MapView mapView;
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus membershipStatus;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentLocationBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 14, sIncludes, sViewsWithIds);
        this.bottomLayout = (android.widget.LinearLayout) bindings[4];
        this.daysRemainingText = (is.yranac.canary.ui.views.TextViewPlus) bindings[6];
        this.homeDetails = (is.yranac.canary.ui.views.TextViewPlus) bindings[10];
        this.homeDetailsTrail = (is.yranac.canary.ui.views.TextViewPlus) bindings[7];
        this.line = (android.view.View) bindings[11];
        this.locationButton = (is.yranac.canary.ui.views.ButtonPlus) bindings[3];
        this.locationDetails = (android.widget.FrameLayout) bindings[8];
        this.locationTrailLayout = (android.widget.LinearLayout) bindings[5];
        this.manageDevices = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[12];
        this.manageMembers = (is.yranac.canary.ui.views.TextViewPlus) bindings[13];
        this.mapContainer = (android.widget.RelativeLayout) bindings[1];
        this.mapView = (com.google.android.gms.maps.MapView) bindings[2];
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.membershipStatus = (is.yranac.canary.ui.views.TextViewPlus) bindings[9];
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
    public static FragmentLocationBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentLocationBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentLocationBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_location, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentLocationBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentLocationBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_location, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentLocationBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentLocationBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_location_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentLocationBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}