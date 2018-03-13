package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentMembershipModesUpsellBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new android.databinding.ViewDataBinding.IncludedLayouts(7);
        sIncludes.setIncludes(1, 
            new String[] {"layout_membership_benefits"},
            new int[] {2},
            new int[] {R.layout.layout_membership_benefits});
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.progress_indication, 3);
        sViewsWithIds.put(R.id.root_view, 4);
        sViewsWithIds.put(R.id.mode_type_upsell, 5);
        sViewsWithIds.put(R.id.add_membership_btn, 6);
    }
    // views
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus addMembershipBtn;
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    @NonNull
    private final android.widget.LinearLayout mboundView1;
    @Nullable
    public final is.yranac.canary.databinding.LayoutMembershipBenefitsBinding membershipDetailsLayout;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus modeTypeUpsell;
    @NonNull
    public final is.yranac.canary.ui.views.CustomProgressView progressIndication;
    @NonNull
    public final android.widget.RelativeLayout rootView;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentMembershipModesUpsellBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 1);
        final Object[] bindings = mapBindings(bindingComponent, root, 7, sIncludes, sViewsWithIds);
        this.addMembershipBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[6];
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView1 = (android.widget.LinearLayout) bindings[1];
        this.mboundView1.setTag(null);
        this.membershipDetailsLayout = (is.yranac.canary.databinding.LayoutMembershipBenefitsBinding) bindings[2];
        setContainedBinding(this.membershipDetailsLayout);
        this.modeTypeUpsell = (is.yranac.canary.ui.views.TextViewPlus) bindings[5];
        this.progressIndication = (is.yranac.canary.ui.views.CustomProgressView) bindings[3];
        this.rootView = (android.widget.RelativeLayout) bindings[4];
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
        }
        membershipDetailsLayout.invalidateAll();
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        if (membershipDetailsLayout.hasPendingBindings()) {
            return true;
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
            case 0 :
                return onChangeMembershipDetailsLayout((is.yranac.canary.databinding.LayoutMembershipBenefitsBinding) object, fieldId);
        }
        return false;
    }
    private boolean onChangeMembershipDetailsLayout(is.yranac.canary.databinding.LayoutMembershipBenefitsBinding MembershipDetailsLayout, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
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
        executeBindingsOn(membershipDetailsLayout);
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static FragmentMembershipModesUpsellBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentMembershipModesUpsellBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentMembershipModesUpsellBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_membership_modes_upsell, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentMembershipModesUpsellBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentMembershipModesUpsellBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_membership_modes_upsell, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentMembershipModesUpsellBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentMembershipModesUpsellBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_membership_modes_upsell_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentMembershipModesUpsellBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): membershipDetailsLayout
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}