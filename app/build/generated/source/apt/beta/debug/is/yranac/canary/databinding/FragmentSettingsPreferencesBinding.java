package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentSettingsPreferencesBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.temperature_layout, 1);
        sViewsWithIds.put(R.id.setting_toggle, 2);
        sViewsWithIds.put(R.id.fahrenheit, 3);
        sViewsWithIds.put(R.id.celsius, 4);
        sViewsWithIds.put(R.id.locale_layout, 5);
        sViewsWithIds.put(R.id.sound_layout, 6);
    }
    // views
    @NonNull
    public final android.widget.ImageView celsius;
    @NonNull
    public final android.widget.ImageView fahrenheit;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel localeLayout;
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    @NonNull
    public final android.widget.LinearLayout settingToggle;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel soundLayout;
    @NonNull
    public final android.widget.RelativeLayout temperatureLayout;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentSettingsPreferencesBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 7, sIncludes, sViewsWithIds);
        this.celsius = (android.widget.ImageView) bindings[4];
        this.fahrenheit = (android.widget.ImageView) bindings[3];
        this.localeLayout = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[5];
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.settingToggle = (android.widget.LinearLayout) bindings[2];
        this.soundLayout = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[6];
        this.temperatureLayout = (android.widget.RelativeLayout) bindings[1];
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
    public static FragmentSettingsPreferencesBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsPreferencesBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentSettingsPreferencesBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_settings_preferences, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentSettingsPreferencesBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsPreferencesBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_settings_preferences, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentSettingsPreferencesBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsPreferencesBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_settings_preferences_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentSettingsPreferencesBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}