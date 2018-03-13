package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentEntrydetailTagBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.tag_header_layout, 1);
        sViewsWithIds.put(R.id.done_button, 2);
        sViewsWithIds.put(R.id.help_your_canary_text_view, 3);
        sViewsWithIds.put(R.id.this_contains_text_view, 4);
        sViewsWithIds.put(R.id.entrydetail_tag_list, 5);
    }
    // views
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus doneButton;
    @NonNull
    public final android.widget.ListView entrydetailTagList;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus helpYourCanaryTextView;
    @NonNull
    public final android.widget.RelativeLayout parent;
    @NonNull
    public final android.widget.LinearLayout tagHeaderLayout;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus thisContainsTextView;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentEntrydetailTagBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 6, sIncludes, sViewsWithIds);
        this.doneButton = (is.yranac.canary.ui.views.ButtonPlus) bindings[2];
        this.entrydetailTagList = (android.widget.ListView) bindings[5];
        this.helpYourCanaryTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[3];
        this.parent = (android.widget.RelativeLayout) bindings[0];
        this.parent.setTag(null);
        this.tagHeaderLayout = (android.widget.LinearLayout) bindings[1];
        this.thisContainsTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[4];
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
    public static FragmentEntrydetailTagBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentEntrydetailTagBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentEntrydetailTagBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_entrydetail_tag, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentEntrydetailTagBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentEntrydetailTagBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_entrydetail_tag, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentEntrydetailTagBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentEntrydetailTagBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_entrydetail_tag_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentEntrydetailTagBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}