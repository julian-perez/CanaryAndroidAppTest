package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentFindBluetoothDevicesBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.header_layout, 5);
        sViewsWithIds.put(R.id.pair_title_two, 6);
        sViewsWithIds.put(R.id.pair_dsc_two, 7);
        sViewsWithIds.put(R.id.main_list_view, 8);
        sViewsWithIds.put(R.id.empty_view, 9);
        sViewsWithIds.put(R.id.empty_linear_layout, 10);
        sViewsWithIds.put(R.id.pair_title, 11);
        sViewsWithIds.put(R.id.pair_dsc, 12);
    }
    // views
    @NonNull
    public final android.widget.ImageView canaryPairImageView;
    @NonNull
    public final android.widget.LinearLayout emptyLinearLayout;
    @NonNull
    public final android.widget.RelativeLayout emptyView;
    @NonNull
    public final android.widget.LinearLayout headerLayout;
    @NonNull
    public final android.widget.ListView mainListView;
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus pairDsc;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus pairDscTwo;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus pairTitle;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus pairTitleTwo;
    @NonNull
    public final android.widget.ProgressBar progressBarBottom;
    @NonNull
    public final android.widget.ProgressBar progressBarTop;
    @NonNull
    public final android.view.TextureView textureView;
    // variables
    @Nullable
    private int mDeviceType;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentFindBluetoothDevicesBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 13, sIncludes, sViewsWithIds);
        this.canaryPairImageView = (android.widget.ImageView) bindings[1];
        this.canaryPairImageView.setTag(null);
        this.emptyLinearLayout = (android.widget.LinearLayout) bindings[10];
        this.emptyView = (android.widget.RelativeLayout) bindings[9];
        this.headerLayout = (android.widget.LinearLayout) bindings[5];
        this.mainListView = (android.widget.ListView) bindings[8];
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.pairDsc = (is.yranac.canary.ui.views.TextViewPlus) bindings[12];
        this.pairDscTwo = (is.yranac.canary.ui.views.TextViewPlus) bindings[7];
        this.pairTitle = (is.yranac.canary.ui.views.TextViewPlus) bindings[11];
        this.pairTitleTwo = (is.yranac.canary.ui.views.TextViewPlus) bindings[6];
        this.progressBarBottom = (android.widget.ProgressBar) bindings[4];
        this.progressBarBottom.setTag(null);
        this.progressBarTop = (android.widget.ProgressBar) bindings[3];
        this.progressBarTop.setTag(null);
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
        boolean deviceTypeInt1BooleanTrueDeviceTypeInt4 = false;
        int deviceTypeInt1BooleanTrueDeviceTypeInt4VVISIBLEVGONE = 0;
        boolean deviceTypeInt4 = false;
        boolean deviceTypeInt1 = false;
        int deviceType = mDeviceType;
        int deviceTypeInt1BooleanTrueDeviceTypeInt4VGONEVVISIBLE = 0;

        if ((dirtyFlags & 0x3L) != 0) {



                // read deviceType == 1
                deviceTypeInt1 = (deviceType) == (1);
            if((dirtyFlags & 0x3L) != 0) {
                if(deviceTypeInt1) {
                        dirtyFlags |= 0x8L;
                }
                else {
                        dirtyFlags |= 0x4L;
                }
            }
        }
        // batch finished

        if ((dirtyFlags & 0x4L) != 0) {

                // read deviceType == 4
                deviceTypeInt4 = (deviceType) == (4);
        }

        if ((dirtyFlags & 0x3L) != 0) {

                // read deviceType == 1 ? true : deviceType == 4
                deviceTypeInt1BooleanTrueDeviceTypeInt4 = ((deviceTypeInt1) ? (true) : (deviceTypeInt4));
            if((dirtyFlags & 0x3L) != 0) {
                if(deviceTypeInt1BooleanTrueDeviceTypeInt4) {
                        dirtyFlags |= 0x20L;
                        dirtyFlags |= 0x80L;
                }
                else {
                        dirtyFlags |= 0x10L;
                        dirtyFlags |= 0x40L;
                }
            }


                // read deviceType == 1 ? true : deviceType == 4 ? v.VISIBLE : v.GONE
                deviceTypeInt1BooleanTrueDeviceTypeInt4VVISIBLEVGONE = ((deviceTypeInt1BooleanTrueDeviceTypeInt4) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
                // read deviceType == 1 ? true : deviceType == 4 ? v.GONE : v.VISIBLE
                deviceTypeInt1BooleanTrueDeviceTypeInt4VGONEVVISIBLE = ((deviceTypeInt1BooleanTrueDeviceTypeInt4) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            this.canaryPairImageView.setVisibility(deviceTypeInt1BooleanTrueDeviceTypeInt4VVISIBLEVGONE);
            this.progressBarBottom.setVisibility(deviceTypeInt1BooleanTrueDeviceTypeInt4VVISIBLEVGONE);
            this.progressBarTop.setVisibility(deviceTypeInt1BooleanTrueDeviceTypeInt4VGONEVVISIBLE);
            this.textureView.setVisibility(deviceTypeInt1BooleanTrueDeviceTypeInt4VGONEVVISIBLE);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static FragmentFindBluetoothDevicesBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentFindBluetoothDevicesBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentFindBluetoothDevicesBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_find_bluetooth_devices, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentFindBluetoothDevicesBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentFindBluetoothDevicesBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_find_bluetooth_devices, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentFindBluetoothDevicesBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentFindBluetoothDevicesBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_find_bluetooth_devices_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentFindBluetoothDevicesBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): deviceType
        flag 1 (0x2L): null
        flag 2 (0x3L): deviceType == 1 ? true : deviceType == 4
        flag 3 (0x4L): deviceType == 1 ? true : deviceType == 4
        flag 4 (0x5L): deviceType == 1 ? true : deviceType == 4 ? v.VISIBLE : v.GONE
        flag 5 (0x6L): deviceType == 1 ? true : deviceType == 4 ? v.VISIBLE : v.GONE
        flag 6 (0x7L): deviceType == 1 ? true : deviceType == 4 ? v.GONE : v.VISIBLE
        flag 7 (0x8L): deviceType == 1 ? true : deviceType == 4 ? v.GONE : v.VISIBLE
    flag mapping end*/
    //end
}