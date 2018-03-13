package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentSetupConfirmNetworkBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.wifi_name_text_view, 2);
        sViewsWithIds.put(R.id.wifi_name_container, 3);
        sViewsWithIds.put(R.id.wifi_password_container, 4);
        sViewsWithIds.put(R.id.show_password_container, 5);
        sViewsWithIds.put(R.id.show_wifi_password_check_box, 6);
        sViewsWithIds.put(R.id.next_btn, 7);
    }
    // views
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    @NonNull
    private final android.widget.LinearLayout mboundView1;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus nextBtn;
    @NonNull
    public final android.widget.RelativeLayout showPasswordContainer;
    @NonNull
    public final android.support.v7.widget.SwitchCompat showWifiPasswordCheckBox;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel wifiNameContainer;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus wifiNameTextView;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel wifiPasswordContainer;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentSetupConfirmNetworkBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 8, sIncludes, sViewsWithIds);
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView1 = (android.widget.LinearLayout) bindings[1];
        this.mboundView1.setTag(null);
        this.nextBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[7];
        this.showPasswordContainer = (android.widget.RelativeLayout) bindings[5];
        this.showWifiPasswordCheckBox = (android.support.v7.widget.SwitchCompat) bindings[6];
        this.wifiNameContainer = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[3];
        this.wifiNameTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[2];
        this.wifiPasswordContainer = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[4];
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
    public static FragmentSetupConfirmNetworkBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSetupConfirmNetworkBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentSetupConfirmNetworkBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_setup_confirm_network, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentSetupConfirmNetworkBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSetupConfirmNetworkBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_setup_confirm_network, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentSetupConfirmNetworkBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSetupConfirmNetworkBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_setup_confirm_network_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentSetupConfirmNetworkBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}