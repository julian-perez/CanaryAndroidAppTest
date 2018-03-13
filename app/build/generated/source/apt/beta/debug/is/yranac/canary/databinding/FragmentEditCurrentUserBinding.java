package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentEditCurrentUserBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new android.databinding.ViewDataBinding.IncludedLayouts(13);
        sIncludes.setIncludes(1, 
            new String[] {"settings_listrow_avatar_layout"},
            new int[] {2},
            new int[] {R.layout.settings_listrow_avatar_layout});
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.avatar_layout, 3);
        sViewsWithIds.put(R.id.add_photo_label, 4);
        sViewsWithIds.put(R.id.line, 5);
        sViewsWithIds.put(R.id.firstname_layout, 6);
        sViewsWithIds.put(R.id.last_name_layout, 7);
        sViewsWithIds.put(R.id.language_layout, 8);
        sViewsWithIds.put(R.id.country_code_layout, 9);
        sViewsWithIds.put(R.id.mobile_phone_layout, 10);
        sViewsWithIds.put(R.id.next_btn, 11);
        sViewsWithIds.put(R.id.sign_in_btn, 12);
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
    public final is.yranac.canary.ui.views.EditTextWithLabel firstnameLayout;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel languageLayout;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel lastNameLayout;
    @NonNull
    public final android.view.View line;
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    @NonNull
    private final android.widget.LinearLayout mboundView1;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel mobilePhoneLayout;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus nextBtn;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus signInBtn;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentEditCurrentUserBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 1);
        final Object[] bindings = mapBindings(bindingComponent, root, 13, sIncludes, sViewsWithIds);
        this.addPhotoLabel = (is.yranac.canary.ui.views.TextViewPlus) bindings[4];
        this.avatarImageLayout = (is.yranac.canary.databinding.SettingsListrowAvatarLayoutBinding) bindings[2];
        setContainedBinding(this.avatarImageLayout);
        this.avatarLayout = (android.widget.LinearLayout) bindings[3];
        this.countryCodeLayout = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[9];
        this.firstnameLayout = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[6];
        this.languageLayout = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[8];
        this.lastNameLayout = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[7];
        this.line = (android.view.View) bindings[5];
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView1 = (android.widget.LinearLayout) bindings[1];
        this.mboundView1.setTag(null);
        this.mobilePhoneLayout = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[10];
        this.nextBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[11];
        this.signInBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[12];
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
    public static FragmentEditCurrentUserBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentEditCurrentUserBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentEditCurrentUserBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_edit_current_user, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentEditCurrentUserBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentEditCurrentUserBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_edit_current_user, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentEditCurrentUserBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentEditCurrentUserBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_edit_current_user_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentEditCurrentUserBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): avatarImageLayout
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}