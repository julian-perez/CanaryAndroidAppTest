package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentTutorialSetupMasksBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.header_layout_text_view, 1);
        sViewsWithIds.put(R.id.device_mask_list, 2);
        sViewsWithIds.put(R.id.next_btn, 3);
        sViewsWithIds.put(R.id.help_btn, 4);
    }
    // views
    @NonNull
    public final android.widget.ListView deviceMaskList;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus headerLayoutTextView;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus helpBtn;
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus nextBtn;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentTutorialSetupMasksBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 5, sIncludes, sViewsWithIds);
        this.deviceMaskList = (android.widget.ListView) bindings[2];
        this.headerLayoutTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[1];
        this.helpBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[4];
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.nextBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[3];
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
    public static FragmentTutorialSetupMasksBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentTutorialSetupMasksBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentTutorialSetupMasksBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_tutorial_setup_masks, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentTutorialSetupMasksBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentTutorialSetupMasksBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_tutorial_setup_masks, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentTutorialSetupMasksBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentTutorialSetupMasksBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_tutorial_setup_masks_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentTutorialSetupMasksBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}