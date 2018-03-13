package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentSettingsGetHelpBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.help_center, 2);
        sViewsWithIds.put(R.id.restart_home_tutorial, 3);
        sViewsWithIds.put(R.id.restart_timeline_tutorial, 4);
    }
    // views
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel contactSupport;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel helpCenter;
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel restartHomeTutorial;
    @NonNull
    public final is.yranac.canary.ui.views.EditTextWithLabel restartTimelineTutorial;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentSettingsGetHelpBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 5, sIncludes, sViewsWithIds);
        this.contactSupport = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[1];
        this.contactSupport.setTag(null);
        this.helpCenter = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[2];
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.restartHomeTutorial = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[3];
        this.restartTimelineTutorial = (is.yranac.canary.ui.views.EditTextWithLabel) bindings[4];
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
        boolean utilsBeta = false;
        int utilsBetaViewVISIBLEViewGONE = 0;

        if ((dirtyFlags & 0x1L) != 0) {

                // read Utils.beta
                utilsBeta = is.yranac.canary.util.Utils.isBeta();
            if((dirtyFlags & 0x1L) != 0) {
                if(utilsBeta) {
                        dirtyFlags |= 0x4L;
                }
                else {
                        dirtyFlags |= 0x2L;
                }
            }


                // read Utils.beta ? View.VISIBLE : View.GONE
                utilsBetaViewVISIBLEViewGONE = ((utilsBeta) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        // batch finished
        if ((dirtyFlags & 0x1L) != 0) {
            // api target 1

            this.contactSupport.setVisibility(utilsBetaViewVISIBLEViewGONE);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static FragmentSettingsGetHelpBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsGetHelpBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentSettingsGetHelpBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_settings_get_help, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentSettingsGetHelpBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsGetHelpBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_settings_get_help, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentSettingsGetHelpBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsGetHelpBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_settings_get_help_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentSettingsGetHelpBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
        flag 1 (0x2L): Utils.beta ? View.VISIBLE : View.GONE
        flag 2 (0x3L): Utils.beta ? View.VISIBLE : View.GONE
    flag mapping end*/
    //end
}