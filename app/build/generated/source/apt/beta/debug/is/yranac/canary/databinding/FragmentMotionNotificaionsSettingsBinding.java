package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentMotionNotificaionsSettingsBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new android.databinding.ViewDataBinding.IncludedLayouts(6);
        sIncludes.setIncludes(1, 
            new String[] {"layout_segmented"},
            new int[] {2},
            new int[] {R.layout.layout_segmented});
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.top_layout, 3);
        sViewsWithIds.put(R.id.connectivity_settings_toggle_layout, 4);
        sViewsWithIds.put(R.id.notification_settings_layout, 5);
    }
    // views
    @NonNull
    public final android.widget.RelativeLayout connectivitySettingsToggleLayout;
    @NonNull
    private final android.widget.ScrollView mboundView0;
    @NonNull
    private final android.support.percent.PercentFrameLayout mboundView1;
    @NonNull
    public final android.widget.LinearLayout notificationSettingsLayout;
    @Nullable
    public final is.yranac.canary.databinding.LayoutSegmentedBinding segmentedLayout;
    @NonNull
    public final android.widget.LinearLayout topLayout;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentMotionNotificaionsSettingsBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 1);
        final Object[] bindings = mapBindings(bindingComponent, root, 6, sIncludes, sViewsWithIds);
        this.connectivitySettingsToggleLayout = (android.widget.RelativeLayout) bindings[4];
        this.mboundView0 = (android.widget.ScrollView) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView1 = (android.support.percent.PercentFrameLayout) bindings[1];
        this.mboundView1.setTag(null);
        this.notificationSettingsLayout = (android.widget.LinearLayout) bindings[5];
        this.segmentedLayout = (is.yranac.canary.databinding.LayoutSegmentedBinding) bindings[2];
        setContainedBinding(this.segmentedLayout);
        this.topLayout = (android.widget.LinearLayout) bindings[3];
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
        }
        segmentedLayout.invalidateAll();
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        if (segmentedLayout.hasPendingBindings()) {
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
                return onChangeSegmentedLayout((is.yranac.canary.databinding.LayoutSegmentedBinding) object, fieldId);
        }
        return false;
    }
    private boolean onChangeSegmentedLayout(is.yranac.canary.databinding.LayoutSegmentedBinding SegmentedLayout, int fieldId) {
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
        executeBindingsOn(segmentedLayout);
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static FragmentMotionNotificaionsSettingsBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentMotionNotificaionsSettingsBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentMotionNotificaionsSettingsBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_motion_notificaions_settings, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentMotionNotificaionsSettingsBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentMotionNotificaionsSettingsBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_motion_notificaions_settings, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentMotionNotificaionsSettingsBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentMotionNotificaionsSettingsBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_motion_notificaions_settings_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentMotionNotificaionsSettingsBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): segmentedLayout
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}