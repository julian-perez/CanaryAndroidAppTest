package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class SettingsListrowAvatarLayoutBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.gray_circle, 1);
        sViewsWithIds.put(R.id.customer_initials, 2);
        sViewsWithIds.put(R.id.member_image_view, 3);
    }
    // views
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus customerInitials;
    @NonNull
    public final is.yranac.canary.ui.views.CircleView grayCircle;
    @NonNull
    public final android.widget.RelativeLayout memberImageContainer;
    @NonNull
    public final is.yranac.canary.ui.views.RoundedImageView memberImageView;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public SettingsListrowAvatarLayoutBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 4, sIncludes, sViewsWithIds);
        this.customerInitials = (is.yranac.canary.ui.views.TextViewPlus) bindings[2];
        this.grayCircle = (is.yranac.canary.ui.views.CircleView) bindings[1];
        this.memberImageContainer = (android.widget.RelativeLayout) bindings[0];
        this.memberImageContainer.setTag(null);
        this.memberImageView = (is.yranac.canary.ui.views.RoundedImageView) bindings[3];
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
    public static SettingsListrowAvatarLayoutBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static SettingsListrowAvatarLayoutBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<SettingsListrowAvatarLayoutBinding>inflate(inflater, is.yranac.canary.R.layout.settings_listrow_avatar_layout, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static SettingsListrowAvatarLayoutBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static SettingsListrowAvatarLayoutBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.settings_listrow_avatar_layout, null, false), bindingComponent);
    }
    @NonNull
    public static SettingsListrowAvatarLayoutBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static SettingsListrowAvatarLayoutBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/settings_listrow_avatar_layout_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new SettingsListrowAvatarLayoutBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}