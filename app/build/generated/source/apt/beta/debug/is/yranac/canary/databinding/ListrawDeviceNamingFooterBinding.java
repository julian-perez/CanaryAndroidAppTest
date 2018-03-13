package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ListrawDeviceNamingFooterBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.name_custom, 1);
        sViewsWithIds.put(R.id.check_mark, 2);
    }
    // views
    @NonNull
    public final android.support.v7.widget.AppCompatRadioButton checkMark;
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextPlus nameCustom;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ListrawDeviceNamingFooterBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 3, sIncludes, sViewsWithIds);
        this.checkMark = (android.support.v7.widget.AppCompatRadioButton) bindings[2];
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.nameCustom = (is.yranac.canary.ui.views.EditTextPlus) bindings[1];
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
    public static ListrawDeviceNamingFooterBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ListrawDeviceNamingFooterBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<ListrawDeviceNamingFooterBinding>inflate(inflater, is.yranac.canary.R.layout.listraw_device_naming_footer, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static ListrawDeviceNamingFooterBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ListrawDeviceNamingFooterBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.listraw_device_naming_footer, null, false), bindingComponent);
    }
    @NonNull
    public static ListrawDeviceNamingFooterBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ListrawDeviceNamingFooterBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/listraw_device_naming_footer_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new ListrawDeviceNamingFooterBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}