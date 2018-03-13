package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentLocationServicesDebuggingBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.location_debug_layout, 2);
        sViewsWithIds.put(R.id.setting_checkbox, 3);
        sViewsWithIds.put(R.id.location_notification_checkbox, 4);
    }
    // views
    @NonNull
    public final android.widget.LinearLayout locationDebugLayout;
    @NonNull
    public final android.support.v7.widget.SwitchCompat locationNotificationCheckbox;
    @NonNull
    public final android.widget.LinearLayout locationNotificationLayout;
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    @NonNull
    public final android.support.v7.widget.SwitchCompat settingCheckbox;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentLocationServicesDebuggingBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 5, sIncludes, sViewsWithIds);
        this.locationDebugLayout = (android.widget.LinearLayout) bindings[2];
        this.locationNotificationCheckbox = (android.support.v7.widget.SwitchCompat) bindings[4];
        this.locationNotificationLayout = (android.widget.LinearLayout) bindings[1];
        this.locationNotificationLayout.setTag(null);
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.settingCheckbox = (android.support.v7.widget.SwitchCompat) bindings[3];
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
        boolean utilsForInternalTestingAndDevelopment = false;
        int utilsForInternalTestingAndDevelopmentViewVISIBLEViewGONE = 0;

        if ((dirtyFlags & 0x1L) != 0) {

                // read Utils.forInternalTestingAndDevelopment
                utilsForInternalTestingAndDevelopment = is.yranac.canary.util.Utils.isForInternalTestingAndDevelopment();
            if((dirtyFlags & 0x1L) != 0) {
                if(utilsForInternalTestingAndDevelopment) {
                        dirtyFlags |= 0x4L;
                }
                else {
                        dirtyFlags |= 0x2L;
                }
            }


                // read Utils.forInternalTestingAndDevelopment ? View.VISIBLE : View.GONE
                utilsForInternalTestingAndDevelopmentViewVISIBLEViewGONE = ((utilsForInternalTestingAndDevelopment) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        // batch finished
        if ((dirtyFlags & 0x1L) != 0) {
            // api target 1

            this.locationNotificationLayout.setVisibility(utilsForInternalTestingAndDevelopmentViewVISIBLEViewGONE);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static FragmentLocationServicesDebuggingBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentLocationServicesDebuggingBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentLocationServicesDebuggingBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_location_services_debugging, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentLocationServicesDebuggingBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentLocationServicesDebuggingBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_location_services_debugging, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentLocationServicesDebuggingBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentLocationServicesDebuggingBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_location_services_debugging_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentLocationServicesDebuggingBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
        flag 1 (0x2L): Utils.forInternalTestingAndDevelopment ? View.VISIBLE : View.GONE
        flag 2 (0x3L): Utils.forInternalTestingAndDevelopment ? View.VISIBLE : View.GONE
    flag mapping end*/
    //end
}