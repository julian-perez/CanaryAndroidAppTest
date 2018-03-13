package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentSettingsProfileBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new android.databinding.ViewDataBinding.IncludedLayouts(9);
        sIncludes.setIncludes(1, 
            new String[] {"settings_listrow_avatar_layout"},
            new int[] {2},
            new int[] {R.layout.settings_listrow_avatar_layout});
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.add_photo_label, 3);
        sViewsWithIds.put(R.id.first_name_layout, 4);
        sViewsWithIds.put(R.id.last_name_layout, 5);
        sViewsWithIds.put(R.id.email_address, 6);
        sViewsWithIds.put(R.id.country_code_layout, 7);
        sViewsWithIds.put(R.id.mobile_phone_layout, 8);
    }
    // views
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus addPhotoLabel;
    @Nullable
    public final is.yranac.canary.databinding.SettingsListrowAvatarLayoutBinding avatarImageLayout;
    @NonNull
    public final android.widget.LinearLayout avatarLayout;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel countryCodeLayout;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel emailAddress;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel firstNameLayout;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel lastNameLayout;
    @NonNull
    private final android.widget.ScrollView mboundView0;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel mobilePhoneLayout;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentSettingsProfileBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 1);
        final Object[] bindings = mapBindings(bindingComponent, root, 9, sIncludes, sViewsWithIds);
        this.addPhotoLabel = (is.yranac.canary.ui.views.TextViewPlus) bindings[3];
        this.avatarImageLayout = (is.yranac.canary.databinding.SettingsListrowAvatarLayoutBinding) bindings[2];
        setContainedBinding(this.avatarImageLayout);
        this.avatarLayout = (android.widget.LinearLayout) bindings[1];
        this.avatarLayout.setTag(null);
        this.countryCodeLayout = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[7];
        this.emailAddress = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[6];
        this.firstNameLayout = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[4];
        this.lastNameLayout = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[5];
        this.mboundView0 = (android.widget.ScrollView) bindings[0];
        this.mboundView0.setTag(null);
        this.mobilePhoneLayout = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[8];
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
        }
        avatarImageLayout.invalidateAll();
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        if (avatarImageLayout.hasPendingBindings()) {
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
                return onChangeAvatarImageLayout((is.yranac.canary.databinding.SettingsListrowAvatarLayoutBinding) object, fieldId);
        }
        return false;
    }
    private boolean onChangeAvatarImageLayout(is.yranac.canary.databinding.SettingsListrowAvatarLayoutBinding AvatarImageLayout, int fieldId) {
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
        executeBindingsOn(avatarImageLayout);
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static FragmentSettingsProfileBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsProfileBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentSettingsProfileBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_settings_profile, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentSettingsProfileBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsProfileBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_settings_profile, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentSettingsProfileBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsProfileBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_settings_profile_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentSettingsProfileBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): avatarImageLayout
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}