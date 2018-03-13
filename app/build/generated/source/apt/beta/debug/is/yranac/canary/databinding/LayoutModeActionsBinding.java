package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class LayoutModeActionsBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.location_setting_title, 1);
        sViewsWithIds.put(R.id.location_setting_title_two, 2);
        sViewsWithIds.put(R.id.mode_action_night_start, 3);
        sViewsWithIds.put(R.id.night_mode_start_time, 4);
        sViewsWithIds.put(R.id.mode_action__night_end, 5);
        sViewsWithIds.put(R.id.night_mode_end_time, 6);
        sViewsWithIds.put(R.id.mode_action_night_set, 7);
        sViewsWithIds.put(R.id.mode_action_one, 8);
        sViewsWithIds.put(R.id.mode_action_two, 9);
        sViewsWithIds.put(R.id.mode_action_three, 10);
    }
    // views
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus locationSettingTitle;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus locationSettingTitleTwo;
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    @NonNull
    public final android.widget.RelativeLayout modeActionNightEnd;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus modeActionNightSet;
    @NonNull
    public final android.widget.RelativeLayout modeActionNightStart;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus modeActionOne;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus modeActionThree;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus modeActionTwo;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus nightModeEndTime;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus nightModeStartTime;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public LayoutModeActionsBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 11, sIncludes, sViewsWithIds);
        this.locationSettingTitle = (is.yranac.canary.ui.views.TextViewPlus) bindings[1];
        this.locationSettingTitleTwo = (is.yranac.canary.ui.views.TextViewPlus) bindings[2];
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.modeActionNightEnd = (android.widget.RelativeLayout) bindings[5];
        this.modeActionNightSet = (is.yranac.canary.ui.views.ButtonPlus) bindings[7];
        this.modeActionNightStart = (android.widget.RelativeLayout) bindings[3];
        this.modeActionOne = (is.yranac.canary.ui.views.ButtonPlus) bindings[8];
        this.modeActionThree = (is.yranac.canary.ui.views.ButtonPlus) bindings[10];
        this.modeActionTwo = (is.yranac.canary.ui.views.ButtonPlus) bindings[9];
        this.nightModeEndTime = (is.yranac.canary.ui.views.TextViewPlus) bindings[6];
        this.nightModeStartTime = (is.yranac.canary.ui.views.TextViewPlus) bindings[4];
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
    public static LayoutModeActionsBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static LayoutModeActionsBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<LayoutModeActionsBinding>inflate(inflater, is.yranac.canary.R.layout.layout_mode_actions, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static LayoutModeActionsBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static LayoutModeActionsBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.layout_mode_actions, null, false), bindingComponent);
    }
    @NonNull
    public static LayoutModeActionsBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static LayoutModeActionsBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/layout_mode_actions_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new LayoutModeActionsBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}