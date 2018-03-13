package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentCreateCurrentUserBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.email_address, 1);
        sViewsWithIds.put(R.id.confirm_email_address, 2);
        sViewsWithIds.put(R.id.password, 3);
        sViewsWithIds.put(R.id.confirm_password, 4);
        sViewsWithIds.put(R.id.next_btn, 5);
        sViewsWithIds.put(R.id.sign_in_btn, 6);
    }
    // views
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel confirmEmailAddress;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel confirmPassword;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel emailAddress;
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus nextBtn;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel password;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus signInBtn;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentCreateCurrentUserBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 7, sIncludes, sViewsWithIds);
        this.confirmEmailAddress = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[2];
        this.confirmPassword = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[4];
        this.emailAddress = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[1];
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.nextBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[5];
        this.password = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[3];
        this.signInBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[6];
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
    public static FragmentCreateCurrentUserBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentCreateCurrentUserBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentCreateCurrentUserBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_create_current_user, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentCreateCurrentUserBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentCreateCurrentUserBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_create_current_user, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentCreateCurrentUserBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentCreateCurrentUserBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_create_current_user_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentCreateCurrentUserBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}