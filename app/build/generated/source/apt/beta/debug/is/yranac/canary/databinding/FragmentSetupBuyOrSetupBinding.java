package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentSetupBuyOrSetupBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.canary_selection, 1);
        sViewsWithIds.put(R.id.canary_selection_image, 2);
        sViewsWithIds.put(R.id.canary_bottom, 3);
        sViewsWithIds.put(R.id.flex_selection, 4);
        sViewsWithIds.put(R.id.flex_selection_image, 5);
        sViewsWithIds.put(R.id.canary_flex_bottom, 6);
        sViewsWithIds.put(R.id.canary_view_selection, 7);
        sViewsWithIds.put(R.id.canary_view_selection_image, 8);
        sViewsWithIds.put(R.id.canary_view_bottom, 9);
    }
    // views
    @NonNull
    public final android.widget.LinearLayout canaryBottom;
    @NonNull
    public final android.widget.LinearLayout canaryFlexBottom;
    @NonNull
    public final android.widget.RelativeLayout canarySelection;
    @NonNull
    public final android.widget.ImageView canarySelectionImage;
    @NonNull
    public final android.widget.LinearLayout canaryViewBottom;
    @NonNull
    public final android.widget.RelativeLayout canaryViewSelection;
    @NonNull
    public final android.widget.ImageView canaryViewSelectionImage;
    @NonNull
    public final android.widget.RelativeLayout flexSelection;
    @NonNull
    public final android.widget.ImageView flexSelectionImage;
    @NonNull
    private final android.widget.ScrollView mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentSetupBuyOrSetupBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 10, sIncludes, sViewsWithIds);
        this.canaryBottom = (android.widget.LinearLayout) bindings[3];
        this.canaryFlexBottom = (android.widget.LinearLayout) bindings[6];
        this.canarySelection = (android.widget.RelativeLayout) bindings[1];
        this.canarySelectionImage = (android.widget.ImageView) bindings[2];
        this.canaryViewBottom = (android.widget.LinearLayout) bindings[9];
        this.canaryViewSelection = (android.widget.RelativeLayout) bindings[7];
        this.canaryViewSelectionImage = (android.widget.ImageView) bindings[8];
        this.flexSelection = (android.widget.RelativeLayout) bindings[4];
        this.flexSelectionImage = (android.widget.ImageView) bindings[5];
        this.mboundView0 = (android.widget.ScrollView) bindings[0];
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
    public static FragmentSetupBuyOrSetupBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSetupBuyOrSetupBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentSetupBuyOrSetupBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_setup_buy_or_setup, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentSetupBuyOrSetupBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSetupBuyOrSetupBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_setup_buy_or_setup, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentSetupBuyOrSetupBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSetupBuyOrSetupBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_setup_buy_or_setup_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentSetupBuyOrSetupBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}