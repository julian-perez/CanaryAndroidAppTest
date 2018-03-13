package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AlertDialogEmergencyNumbersBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.call_police_btn, 1);
        sViewsWithIds.put(R.id.call_medical_btn, 2);
        sViewsWithIds.put(R.id.call_fire_btn, 3);
        sViewsWithIds.put(R.id.add_more_numbers_btn, 4);
        sViewsWithIds.put(R.id.cancel_btn, 5);
    }
    // views
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus addMoreNumbersBtn;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus callFireBtn;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus callMedicalBtn;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus callPoliceBtn;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus cancelBtn;
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AlertDialogEmergencyNumbersBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 6, sIncludes, sViewsWithIds);
        this.addMoreNumbersBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[4];
        this.callFireBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[3];
        this.callMedicalBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[2];
        this.callPoliceBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[1];
        this.cancelBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[5];
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
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
    public static AlertDialogEmergencyNumbersBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static AlertDialogEmergencyNumbersBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<AlertDialogEmergencyNumbersBinding>inflate(inflater, is.yranac.canary.R.layout.alert_dialog_emergency_numbers, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static AlertDialogEmergencyNumbersBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static AlertDialogEmergencyNumbersBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.alert_dialog_emergency_numbers, null, false), bindingComponent);
    }
    @NonNull
    public static AlertDialogEmergencyNumbersBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static AlertDialogEmergencyNumbersBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/alert_dialog_emergency_numbers_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new AlertDialogEmergencyNumbersBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}