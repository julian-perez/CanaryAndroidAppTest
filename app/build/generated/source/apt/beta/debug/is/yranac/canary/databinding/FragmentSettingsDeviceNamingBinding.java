package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentSettingsDeviceNamingBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.list_view, 4);
        sViewsWithIds.put(R.id.next_btn, 5);
    }
    // views
    @NonNull
    public final android.widget.RelativeLayout deviceNamingHeader;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus headerTitleTextView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus headerTitleTextViewSmall;
    @NonNull
    public final android.widget.ListView listView;
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus nextBtn;
    // variables
    @Nullable
    private boolean mIsSetup;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentSettingsDeviceNamingBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 6, sIncludes, sViewsWithIds);
        this.deviceNamingHeader = (android.widget.RelativeLayout) bindings[1];
        this.deviceNamingHeader.setTag(null);
        this.headerTitleTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[2];
        this.headerTitleTextView.setTag(null);
        this.headerTitleTextViewSmall = (is.yranac.canary.ui.views.TextViewPlus) bindings[3];
        this.headerTitleTextViewSmall.setTag(null);
        this.listView = (android.widget.ListView) bindings[4];
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.nextBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[5];
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
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
        if (BR.isSetup == variableId) {
            setIsSetup((boolean) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setIsSetup(boolean IsSetup) {
        this.mIsSetup = IsSetup;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.isSetup);
        super.requestRebind();
    }
    public boolean getIsSetup() {
        return mIsSetup;
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
        int isSetupViewGONEViewVISIBLE = 0;
        boolean isSetup = mIsSetup;
        int isSetupViewVISIBLEViewGONE = 0;

        if ((dirtyFlags & 0x3L) != 0) {

            if((dirtyFlags & 0x3L) != 0) {
                if(isSetup) {
                        dirtyFlags |= 0x8L;
                        dirtyFlags |= 0x20L;
                }
                else {
                        dirtyFlags |= 0x4L;
                        dirtyFlags |= 0x10L;
                }
            }


                // read isSetup ? View.GONE : View.VISIBLE
                isSetupViewGONEViewVISIBLE = ((isSetup) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
                // read isSetup ? View.VISIBLE : View.GONE
                isSetupViewVISIBLEViewGONE = ((isSetup) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            this.headerTitleTextView.setVisibility(isSetupViewVISIBLEViewGONE);
            this.headerTitleTextViewSmall.setVisibility(isSetupViewGONEViewVISIBLE);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static FragmentSettingsDeviceNamingBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsDeviceNamingBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentSettingsDeviceNamingBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_settings_device_naming, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentSettingsDeviceNamingBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsDeviceNamingBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_settings_device_naming, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentSettingsDeviceNamingBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsDeviceNamingBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_settings_device_naming_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentSettingsDeviceNamingBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): isSetup
        flag 1 (0x2L): null
        flag 2 (0x3L): isSetup ? View.GONE : View.VISIBLE
        flag 3 (0x4L): isSetup ? View.GONE : View.VISIBLE
        flag 4 (0x5L): isSetup ? View.VISIBLE : View.GONE
        flag 5 (0x6L): isSetup ? View.VISIBLE : View.GONE
    flag mapping end*/
    //end
}