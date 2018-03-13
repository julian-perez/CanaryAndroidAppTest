package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class LayoutSirenLightBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.siren_layout, 1);
        sViewsWithIds.put(R.id.sound_siren_btn, 2);
        sViewsWithIds.put(R.id.siren_sounding_layout, 3);
        sViewsWithIds.put(R.id.siren_countdown_progress_wheel, 4);
        sViewsWithIds.put(R.id.sound_siren_layout, 5);
        sViewsWithIds.put(R.id.emergency_call_layout, 6);
        sViewsWithIds.put(R.id.emergency_call_btn, 7);
        sViewsWithIds.put(R.id.emergency_phone_icon, 8);
        sViewsWithIds.put(R.id.emergency_text_view, 9);
    }
    // views
    @NonNull
    public final android.view.View emergencyCallBtn;
    @NonNull
    public final android.widget.RelativeLayout emergencyCallLayout;
    @NonNull
    public final android.support.v7.widget.AppCompatImageView emergencyPhoneIcon;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus emergencyTextView;
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    @NonNull
    public final is.yranac.canary.ui.views.ProgressWheel sirenCountdownProgressWheel;
    @NonNull
    public final android.widget.RelativeLayout sirenLayout;
    @NonNull
    public final android.widget.LinearLayout sirenSoundingLayout;
    @NonNull
    public final android.view.View soundSirenBtn;
    @NonNull
    public final android.widget.LinearLayout soundSirenLayout;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public LayoutSirenLightBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 10, sIncludes, sViewsWithIds);
        this.emergencyCallBtn = (android.view.View) bindings[7];
        this.emergencyCallLayout = (android.widget.RelativeLayout) bindings[6];
        this.emergencyPhoneIcon = (android.support.v7.widget.AppCompatImageView) bindings[8];
        this.emergencyTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[9];
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.sirenCountdownProgressWheel = (is.yranac.canary.ui.views.ProgressWheel) bindings[4];
        this.sirenLayout = (android.widget.RelativeLayout) bindings[1];
        this.sirenSoundingLayout = (android.widget.LinearLayout) bindings[3];
        this.soundSirenBtn = (android.view.View) bindings[2];
        this.soundSirenLayout = (android.widget.LinearLayout) bindings[5];
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
    public static LayoutSirenLightBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static LayoutSirenLightBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<LayoutSirenLightBinding>inflate(inflater, is.yranac.canary.R.layout.layout_siren_light, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static LayoutSirenLightBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static LayoutSirenLightBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.layout_siren_light, null, false), bindingComponent);
    }
    @NonNull
    public static LayoutSirenLightBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static LayoutSirenLightBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/layout_siren_light_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new LayoutSirenLightBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}