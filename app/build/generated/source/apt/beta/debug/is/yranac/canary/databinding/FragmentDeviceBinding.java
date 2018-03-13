package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentDeviceBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new android.databinding.ViewDataBinding.IncludedLayouts(5);
        sIncludes.setIncludes(0, 
            new String[] {"layout_device_statatics"},
            new int[] {1},
            new int[] {R.layout.layout_device_statatics});
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.image_view, 2);
        sViewsWithIds.put(R.id.progress_bar, 3);
        sViewsWithIds.put(R.id.tutorial_view, 4);
    }
    // views
    @Nullable
    public final is.yranac.canary.databinding.LayoutDeviceStataticsBinding deviceInfoLayout;
    @NonNull
    public final android.widget.ImageView imageView;
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    @NonNull
    public final android.widget.ProgressBar progressBar;
    @NonNull
    public final android.widget.RelativeLayout tutorialView;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentDeviceBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 1);
        final Object[] bindings = mapBindings(bindingComponent, root, 5, sIncludes, sViewsWithIds);
        this.deviceInfoLayout = (is.yranac.canary.databinding.LayoutDeviceStataticsBinding) bindings[1];
        setContainedBinding(this.deviceInfoLayout);
        this.imageView = (android.widget.ImageView) bindings[2];
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.progressBar = (android.widget.ProgressBar) bindings[3];
        this.tutorialView = (android.widget.RelativeLayout) bindings[4];
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
        }
        deviceInfoLayout.invalidateAll();
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        if (deviceInfoLayout.hasPendingBindings()) {
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
                return onChangeDeviceInfoLayout((is.yranac.canary.databinding.LayoutDeviceStataticsBinding) object, fieldId);
        }
        return false;
    }
    private boolean onChangeDeviceInfoLayout(is.yranac.canary.databinding.LayoutDeviceStataticsBinding DeviceInfoLayout, int fieldId) {
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
        executeBindingsOn(deviceInfoLayout);
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static FragmentDeviceBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentDeviceBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentDeviceBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_device, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentDeviceBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentDeviceBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_device, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentDeviceBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentDeviceBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_device_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentDeviceBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): deviceInfoLayout
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}