package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentSetupConnectEthernetBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.content_text, 3);
        sViewsWithIds.put(R.id.next_btn, 4);
    }
    // views
    @NonNull
    public final android.widget.ImageView contentImage2;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus contentText;
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus nextBtn;
    @NonNull
    public final android.view.TextureView textureView;
    // variables
    @Nullable
    private int mDeviceType;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentSetupConnectEthernetBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 5, sIncludes, sViewsWithIds);
        this.contentImage2 = (android.widget.ImageView) bindings[1];
        this.contentImage2.setTag(null);
        this.contentText = (is.yranac.canary.ui.views.TextViewPlus) bindings[3];
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.nextBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[4];
        this.textureView = (android.view.TextureView) bindings[2];
        this.textureView.setTag(null);
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
        if (BR.deviceType == variableId) {
            setDeviceType((int) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setDeviceType(int DeviceType) {
        this.mDeviceType = DeviceType;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.deviceType);
        super.requestRebind();
    }
    public int getDeviceType() {
        return mDeviceType;
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
        int deviceTypeInt1VVISIBLEVGONE = 0;
        int deviceTypeInt1VGONEVVISIBLE = 0;
        boolean deviceTypeInt1 = false;
        int deviceType = mDeviceType;

        if ((dirtyFlags & 0x3L) != 0) {



                // read deviceType == 1
                deviceTypeInt1 = (deviceType) == (1);
            if((dirtyFlags & 0x3L) != 0) {
                if(deviceTypeInt1) {
                        dirtyFlags |= 0x8L;
                        dirtyFlags |= 0x20L;
                }
                else {
                        dirtyFlags |= 0x4L;
                        dirtyFlags |= 0x10L;
                }
            }


                // read deviceType == 1 ? v.VISIBLE : v.GONE
                deviceTypeInt1VVISIBLEVGONE = ((deviceTypeInt1) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
                // read deviceType == 1 ? v.GONE : v.VISIBLE
                deviceTypeInt1VGONEVVISIBLE = ((deviceTypeInt1) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            this.contentImage2.setVisibility(deviceTypeInt1VVISIBLEVGONE);
            this.textureView.setVisibility(deviceTypeInt1VGONEVVISIBLE);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static FragmentSetupConnectEthernetBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSetupConnectEthernetBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentSetupConnectEthernetBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_setup_connect_ethernet, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentSetupConnectEthernetBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSetupConnectEthernetBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_setup_connect_ethernet, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentSetupConnectEthernetBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSetupConnectEthernetBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_setup_connect_ethernet_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentSetupConnectEthernetBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): deviceType
        flag 1 (0x2L): null
        flag 2 (0x3L): deviceType == 1 ? v.VISIBLE : v.GONE
        flag 3 (0x4L): deviceType == 1 ? v.VISIBLE : v.GONE
        flag 4 (0x5L): deviceType == 1 ? v.GONE : v.VISIBLE
        flag 5 (0x6L): deviceType == 1 ? v.GONE : v.VISIBLE
    flag mapping end*/
    //end
}