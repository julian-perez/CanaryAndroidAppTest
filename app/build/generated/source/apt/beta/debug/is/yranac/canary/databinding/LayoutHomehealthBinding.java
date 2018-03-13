package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class LayoutHomehealthBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.temp_container, 1);
        sViewsWithIds.put(R.id.temp_icon, 2);
        sViewsWithIds.put(R.id.device_temp_textview, 3);
        sViewsWithIds.put(R.id.humidity_container, 4);
        sViewsWithIds.put(R.id.humidity_icon, 5);
        sViewsWithIds.put(R.id.device_humidity_textview, 6);
        sViewsWithIds.put(R.id.air_quality_container, 7);
        sViewsWithIds.put(R.id.device_air_quality_icon, 8);
        sViewsWithIds.put(R.id.device_air_quality_offline_icon, 9);
        sViewsWithIds.put(R.id.device_air_textview, 10);
    }
    // views
    @NonNull
    public final android.widget.LinearLayout airQualityContainer;
    @NonNull
    public final android.widget.ImageView deviceAirQualityIcon;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus deviceAirQualityOfflineIcon;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus deviceAirTextview;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus deviceHumidityTextview;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus deviceTempTextview;
    @NonNull
    public final android.widget.LinearLayout humidityContainer;
    @NonNull
    public final android.widget.ImageView humidityIcon;
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    @NonNull
    public final android.widget.LinearLayout tempContainer;
    @NonNull
    public final android.widget.ImageView tempIcon;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public LayoutHomehealthBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 11, sIncludes, sViewsWithIds);
        this.airQualityContainer = (android.widget.LinearLayout) bindings[7];
        this.deviceAirQualityIcon = (android.widget.ImageView) bindings[8];
        this.deviceAirQualityOfflineIcon = (is.yranac.canary.ui.views.TextViewPlus) bindings[9];
        this.deviceAirTextview = (is.yranac.canary.ui.views.TextViewPlus) bindings[10];
        this.deviceHumidityTextview = (is.yranac.canary.ui.views.TextViewPlus) bindings[6];
        this.deviceTempTextview = (is.yranac.canary.ui.views.TextViewPlus) bindings[3];
        this.humidityContainer = (android.widget.LinearLayout) bindings[4];
        this.humidityIcon = (android.widget.ImageView) bindings[5];
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.tempContainer = (android.widget.LinearLayout) bindings[1];
        this.tempIcon = (android.widget.ImageView) bindings[2];
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
    public static LayoutHomehealthBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static LayoutHomehealthBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<LayoutHomehealthBinding>inflate(inflater, is.yranac.canary.R.layout.layout_homehealth, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static LayoutHomehealthBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static LayoutHomehealthBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.layout_homehealth, null, false), bindingComponent);
    }
    @NonNull
    public static LayoutHomehealthBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static LayoutHomehealthBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/layout_homehealth_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new LayoutHomehealthBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}