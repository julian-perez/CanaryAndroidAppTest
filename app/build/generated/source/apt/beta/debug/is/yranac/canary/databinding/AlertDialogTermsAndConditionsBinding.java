package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AlertDialogTermsAndConditionsBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.terms_btn, 1);
        sViewsWithIds.put(R.id.privacy_policy_btn, 2);
        sViewsWithIds.put(R.id.end_user_btn, 3);
        sViewsWithIds.put(R.id.i_accept_btn, 4);
        sViewsWithIds.put(R.id.cancel_btn, 5);
    }
    // views
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus cancelBtn;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus endUserBtn;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus iAcceptBtn;
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus privacyPolicyBtn;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus termsBtn;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AlertDialogTermsAndConditionsBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 6, sIncludes, sViewsWithIds);
        this.cancelBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[5];
        this.endUserBtn = (is.yranac.canary.ui.views.TextViewPlus) bindings[3];
        this.iAcceptBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[4];
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.privacyPolicyBtn = (is.yranac.canary.ui.views.TextViewPlus) bindings[2];
        this.termsBtn = (is.yranac.canary.ui.views.TextViewPlus) bindings[1];
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
    public static AlertDialogTermsAndConditionsBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static AlertDialogTermsAndConditionsBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<AlertDialogTermsAndConditionsBinding>inflate(inflater, is.yranac.canary.R.layout.alert_dialog_terms_and_conditions, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static AlertDialogTermsAndConditionsBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static AlertDialogTermsAndConditionsBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.alert_dialog_terms_and_conditions, null, false), bindingComponent);
    }
    @NonNull
    public static AlertDialogTermsAndConditionsBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static AlertDialogTermsAndConditionsBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/alert_dialog_terms_and_conditions_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new AlertDialogTermsAndConditionsBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}