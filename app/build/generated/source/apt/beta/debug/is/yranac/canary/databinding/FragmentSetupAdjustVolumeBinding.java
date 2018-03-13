package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentSetupAdjustVolumeBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.allow_access_text_view, 1);
        sViewsWithIds.put(R.id.audio_pulse_image, 2);
        sViewsWithIds.put(R.id.allow_access_dsc_text_view, 3);
        sViewsWithIds.put(R.id.adjust_volume_layout, 4);
        sViewsWithIds.put(R.id.volume_left, 5);
        sViewsWithIds.put(R.id.seek_bar, 6);
        sViewsWithIds.put(R.id.volume_right, 7);
        sViewsWithIds.put(R.id.allow_access_btn, 8);
    }
    // views
    @NonNull
    public final android.widget.RelativeLayout adjustVolumeLayout;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus allowAccessBtn;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus allowAccessDscTextView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus allowAccessTextView;
    @NonNull
    public final android.widget.ImageView audioPulseImage;
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    @NonNull
    public final android.widget.SeekBar seekBar;
    @NonNull
    public final android.widget.ImageView volumeLeft;
    @NonNull
    public final android.widget.ImageView volumeRight;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentSetupAdjustVolumeBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 9, sIncludes, sViewsWithIds);
        this.adjustVolumeLayout = (android.widget.RelativeLayout) bindings[4];
        this.allowAccessBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[8];
        this.allowAccessDscTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[3];
        this.allowAccessTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[1];
        this.audioPulseImage = (android.widget.ImageView) bindings[2];
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.seekBar = (android.widget.SeekBar) bindings[6];
        this.volumeLeft = (android.widget.ImageView) bindings[5];
        this.volumeRight = (android.widget.ImageView) bindings[7];
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
    public static FragmentSetupAdjustVolumeBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSetupAdjustVolumeBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentSetupAdjustVolumeBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_setup_adjust_volume, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentSetupAdjustVolumeBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSetupAdjustVolumeBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_setup_adjust_volume, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentSetupAdjustVolumeBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSetupAdjustVolumeBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_setup_adjust_volume_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentSetupAdjustVolumeBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}