package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentMainSettingsBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.top_layout, 1);
        sViewsWithIds.put(R.id.banner_background, 2);
        sViewsWithIds.put(R.id.alert_text_view, 3);
        sViewsWithIds.put(R.id.action_text_view, 4);
        sViewsWithIds.put(R.id.about_button, 5);
        sViewsWithIds.put(R.id.help_button, 6);
        sViewsWithIds.put(R.id.account_button, 7);
        sViewsWithIds.put(R.id.appCompatImageView, 8);
        sViewsWithIds.put(R.id.view_pager, 9);
        sViewsWithIds.put(R.id.indicator, 10);
    }
    // views
    @NonNull
    public final android.widget.LinearLayout aboutButton;
    @NonNull
    public final android.widget.LinearLayout accountButton;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus actionTextView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus alertTextView;
    @NonNull
    public final android.support.v7.widget.AppCompatImageView appCompatImageView;
    @NonNull
    public final android.widget.RelativeLayout bannerBackground;
    @NonNull
    public final android.widget.LinearLayout helpButton;
    @NonNull
    public final is.yranac.canary.ui.views.viewpagerindicator.CirclePageIndicator indicator;
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    @NonNull
    public final android.widget.LinearLayout topLayout;
    @NonNull
    public final is.yranac.canary.ui.views.CustomViewPager viewPager;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentMainSettingsBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 11, sIncludes, sViewsWithIds);
        this.aboutButton = (android.widget.LinearLayout) bindings[5];
        this.accountButton = (android.widget.LinearLayout) bindings[7];
        this.actionTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[4];
        this.alertTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[3];
        this.appCompatImageView = (android.support.v7.widget.AppCompatImageView) bindings[8];
        this.bannerBackground = (android.widget.RelativeLayout) bindings[2];
        this.helpButton = (android.widget.LinearLayout) bindings[6];
        this.indicator = (is.yranac.canary.ui.views.viewpagerindicator.CirclePageIndicator) bindings[10];
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.topLayout = (android.widget.LinearLayout) bindings[1];
        this.viewPager = (is.yranac.canary.ui.views.CustomViewPager) bindings[9];
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
    public static FragmentMainSettingsBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentMainSettingsBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentMainSettingsBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_main_settings, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentMainSettingsBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentMainSettingsBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_main_settings, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentMainSettingsBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentMainSettingsBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_main_settings_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentMainSettingsBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}