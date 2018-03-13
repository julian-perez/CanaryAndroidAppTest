package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivitySetupFragmentStackBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new android.databinding.ViewDataBinding.IncludedLayouts(4);
        sIncludes.setIncludes(0, 
            new String[] {"layout_header"},
            new int[] {1},
            new int[] {R.layout.layout_header});
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.graph_scroll_view, 2);
        sViewsWithIds.put(R.id.modal_view_frame_layout, 3);
    }
    // views
    @NonNull
    public final android.widget.FrameLayout graphScrollView;
    @Nullable
    public final is.yranac.canary.databinding.LayoutHeaderBinding header;
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    @NonNull
    public final android.widget.FrameLayout modalViewFrameLayout;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivitySetupFragmentStackBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 1);
        final Object[] bindings = mapBindings(bindingComponent, root, 4, sIncludes, sViewsWithIds);
        this.graphScrollView = (android.widget.FrameLayout) bindings[2];
        this.header = (is.yranac.canary.databinding.LayoutHeaderBinding) bindings[1];
        setContainedBinding(this.header);
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.modalViewFrameLayout = (android.widget.FrameLayout) bindings[3];
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
        }
        header.invalidateAll();
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        if (header.hasPendingBindings()) {
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
                return onChangeHeader((is.yranac.canary.databinding.LayoutHeaderBinding) object, fieldId);
        }
        return false;
    }
    private boolean onChangeHeader(is.yranac.canary.databinding.LayoutHeaderBinding Header, int fieldId) {
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
        executeBindingsOn(header);
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static ActivitySetupFragmentStackBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ActivitySetupFragmentStackBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<ActivitySetupFragmentStackBinding>inflate(inflater, is.yranac.canary.R.layout.activity_setup_fragment_stack, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static ActivitySetupFragmentStackBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ActivitySetupFragmentStackBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.activity_setup_fragment_stack, null, false), bindingComponent);
    }
    @NonNull
    public static ActivitySetupFragmentStackBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ActivitySetupFragmentStackBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/activity_setup_fragment_stack_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new ActivitySetupFragmentStackBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): header
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}