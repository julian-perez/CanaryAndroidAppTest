package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentSettingsLocationMemberBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.member_image_container, 1);
        sViewsWithIds.put(R.id.gray_circle, 2);
        sViewsWithIds.put(R.id.customer_initials, 3);
        sViewsWithIds.put(R.id.member_image_view, 4);
        sViewsWithIds.put(R.id.name_layout, 5);
        sViewsWithIds.put(R.id.phone_layout, 6);
        sViewsWithIds.put(R.id.email_layout, 7);
        sViewsWithIds.put(R.id.remove_member_btn, 8);
    }
    // views
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus customerInitials;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel emailLayout;
    @NonNull
    public final is.yranac.canary.ui.views.CircleView grayCircle;
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    @NonNull
    public final android.widget.FrameLayout memberImageContainer;
    @NonNull
    public final is.yranac.canary.ui.views.RoundedImageView memberImageView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus nameLayout;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel phoneLayout;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus removeMemberBtn;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentSettingsLocationMemberBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 9, sIncludes, sViewsWithIds);
        this.customerInitials = (is.yranac.canary.ui.views.TextViewPlus) bindings[3];
        this.emailLayout = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[7];
        this.grayCircle = (is.yranac.canary.ui.views.CircleView) bindings[2];
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.memberImageContainer = (android.widget.FrameLayout) bindings[1];
        this.memberImageView = (is.yranac.canary.ui.views.RoundedImageView) bindings[4];
        this.nameLayout = (is.yranac.canary.ui.views.TextViewPlus) bindings[5];
        this.phoneLayout = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[6];
        this.removeMemberBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[8];
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
    public static FragmentSettingsLocationMemberBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsLocationMemberBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentSettingsLocationMemberBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_settings_location_member, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentSettingsLocationMemberBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsLocationMemberBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_settings_location_member, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentSettingsLocationMemberBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsLocationMemberBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_settings_location_member_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentSettingsLocationMemberBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}