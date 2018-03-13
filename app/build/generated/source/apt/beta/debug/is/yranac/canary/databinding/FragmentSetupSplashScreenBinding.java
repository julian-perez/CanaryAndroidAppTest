package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentSetupSplashScreenBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.intro_video_view, 3);
        sViewsWithIds.put(R.id.text_layout, 4);
        sViewsWithIds.put(R.id.canary_logo, 5);
        sViewsWithIds.put(R.id.sign_up_btn, 6);
        sViewsWithIds.put(R.id.go_to_sign_in_btn, 7);
        sViewsWithIds.put(R.id.header_title_text_view, 8);
        sViewsWithIds.put(R.id.email, 9);
        sViewsWithIds.put(R.id.password, 10);
        sViewsWithIds.put(R.id.password_show_checkbox, 11);
        sViewsWithIds.put(R.id.password_checkbox, 12);
        sViewsWithIds.put(R.id.sign_in_btn, 13);
        sViewsWithIds.put(R.id.forgot_password_text_view, 14);
        sViewsWithIds.put(R.id.forgot_password_layout, 15);
        sViewsWithIds.put(R.id.email_forgot_password, 16);
        sViewsWithIds.put(R.id.forgot_password_btn, 17);
    }
    // views
    @NonNull
    public final android.widget.ImageView canaryLogo;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel email;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel emailForgotPassword;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus forgotPasswordBtn;
    @NonNull
    public final android.widget.LinearLayout forgotPasswordLayout;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus forgotPasswordTextView;
    @NonNull
    public final android.widget.Button goToSignInBtn;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus headerTitleTextView;
    @NonNull
    public final android.view.TextureView introVideoView;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel password;
    @NonNull
    public final android.support.v7.widget.SwitchCompat passwordCheckbox;
    @NonNull
    public final android.widget.LinearLayout passwordShowCheckbox;
    @NonNull
    public final android.widget.Button preferecnesBtn;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus signInBtn;
    @NonNull
    public final android.widget.LinearLayout signInLayout;
    @NonNull
    public final android.widget.RelativeLayout signInRoot;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus signUpBtn;
    @NonNull
    public final android.widget.RelativeLayout textLayout;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentSetupSplashScreenBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 18, sIncludes, sViewsWithIds);
        this.canaryLogo = (android.widget.ImageView) bindings[5];
        this.email = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[9];
        this.emailForgotPassword = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[16];
        this.forgotPasswordBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[17];
        this.forgotPasswordLayout = (android.widget.LinearLayout) bindings[15];
        this.forgotPasswordTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[14];
        this.goToSignInBtn = (android.widget.Button) bindings[7];
        this.headerTitleTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[8];
        this.introVideoView = (android.view.TextureView) bindings[3];
        this.password = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[10];
        this.passwordCheckbox = (android.support.v7.widget.SwitchCompat) bindings[12];
        this.passwordShowCheckbox = (android.widget.LinearLayout) bindings[11];
        this.preferecnesBtn = (android.widget.Button) bindings[1];
        this.preferecnesBtn.setTag(null);
        this.signInBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[13];
        this.signInLayout = (android.widget.LinearLayout) bindings[2];
        this.signInLayout.setTag(null);
        this.signInRoot = (android.widget.RelativeLayout) bindings[0];
        this.signInRoot.setTag(null);
        this.signUpBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[6];
        this.textLayout = (android.widget.RelativeLayout) bindings[4];
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

        if ((dirtyFlags & 0x1L) != 0) {

            if((dirtyFlags & 0x1L) != 0) {
                if(is.yranac.canary.util.Utils.isDev()) {
                        dirtyFlags |= 0x4L;
                }
                else {
                        dirtyFlags |= 0x2L;
                }
            }
        }
        // batch finished
        if ((dirtyFlags & 0x1L) != 0) {
            // api target 1

            this.preferecnesBtn.setVisibility(((is.yranac.canary.util.Utils.isDev()) ? (android.view.View.VISIBLE) : (android.view.View.GONE)));
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static FragmentSetupSplashScreenBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSetupSplashScreenBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentSetupSplashScreenBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_setup_splash_screen, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentSetupSplashScreenBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSetupSplashScreenBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_setup_splash_screen, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentSetupSplashScreenBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSetupSplashScreenBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_setup_splash_screen_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentSetupSplashScreenBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
        flag 1 (0x2L): Utils.isDev() ? v.VISIBLE : v.GONE
        flag 2 (0x3L): Utils.isDev() ? v.VISIBLE : v.GONE
    flag mapping end*/
    //end
}