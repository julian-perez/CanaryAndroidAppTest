package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ListrowEntrySubscriptionFooterBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.progress_view, 1);
        sViewsWithIds.put(R.id.footer_layout, 2);
        sViewsWithIds.put(R.id.subscription_name_text_view, 3);
        sViewsWithIds.put(R.id.subscription_dsc_text_view, 4);
        sViewsWithIds.put(R.id.add_membership_button, 5);
        sViewsWithIds.put(R.id.back_to_top_button, 6);
    }
    // views
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus addMembershipButton;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus backToTopButton;
    @NonNull
    public final android.widget.LinearLayout footerLayout;
    @NonNull
    public final android.widget.LinearLayout listviewFooterMessage;
    @NonNull
    public final android.widget.ProgressBar progressView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus subscriptionDscTextView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus subscriptionNameTextView;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ListrowEntrySubscriptionFooterBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 7, sIncludes, sViewsWithIds);
        this.addMembershipButton = (is.yranac.canary.ui.views.ButtonPlus) bindings[5];
        this.backToTopButton = (is.yranac.canary.ui.views.ButtonPlus) bindings[6];
        this.footerLayout = (android.widget.LinearLayout) bindings[2];
        this.listviewFooterMessage = (android.widget.LinearLayout) bindings[0];
        this.listviewFooterMessage.setTag(null);
        this.progressView = (android.widget.ProgressBar) bindings[1];
        this.subscriptionDscTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[4];
        this.subscriptionNameTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[3];
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
    public static ListrowEntrySubscriptionFooterBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ListrowEntrySubscriptionFooterBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<ListrowEntrySubscriptionFooterBinding>inflate(inflater, is.yranac.canary.R.layout.listrow_entry_subscription_footer, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static ListrowEntrySubscriptionFooterBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ListrowEntrySubscriptionFooterBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.listrow_entry_subscription_footer, null, false), bindingComponent);
    }
    @NonNull
    public static ListrowEntrySubscriptionFooterBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ListrowEntrySubscriptionFooterBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/listrow_entry_subscription_footer_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new ListrowEntrySubscriptionFooterBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}