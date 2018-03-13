package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentHealthGraphBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new android.databinding.ViewDataBinding.IncludedLayouts(38);
        sIncludes.setIncludes(0, 
            new String[] {"layout_homehealth_header"},
            new int[] {1},
            new int[] {R.layout.layout_homehealth_header});
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.graph_scroll_view, 2);
        sViewsWithIds.put(R.id.temperature_info, 3);
        sViewsWithIds.put(R.id.temperature_reading_text_view, 4);
        sViewsWithIds.put(R.id.temp_chart_background, 5);
        sViewsWithIds.put(R.id.temperature_chart, 6);
        sViewsWithIds.put(R.id.temp_user_max_label, 7);
        sViewsWithIds.put(R.id.temp_user_min_label, 8);
        sViewsWithIds.put(R.id.temp_loading_text_view, 9);
        sViewsWithIds.put(R.id.temp_line, 10);
        sViewsWithIds.put(R.id.temp_circle, 11);
        sViewsWithIds.put(R.id.tutorial_frame_graph, 12);
        sViewsWithIds.put(R.id.fake_graph, 13);
        sViewsWithIds.put(R.id.fake_graph_gradient, 14);
        sViewsWithIds.put(R.id.humidity_info, 15);
        sViewsWithIds.put(R.id.humidity_reading_text_view, 16);
        sViewsWithIds.put(R.id.humidity_chart_background, 17);
        sViewsWithIds.put(R.id.humidity_chart, 18);
        sViewsWithIds.put(R.id.humidity_user_max_label, 19);
        sViewsWithIds.put(R.id.humidity_user_min_label, 20);
        sViewsWithIds.put(R.id.humidity_loading_text_view, 21);
        sViewsWithIds.put(R.id.humidity_line, 22);
        sViewsWithIds.put(R.id.humidity_circle, 23);
        sViewsWithIds.put(R.id.tutorial_frame_graph_two, 24);
        sViewsWithIds.put(R.id.fake_graph_two, 25);
        sViewsWithIds.put(R.id.fake_graph_gradient_two, 26);
        sViewsWithIds.put(R.id.air_quality_info, 27);
        sViewsWithIds.put(R.id.air_quality_reading_text_view, 28);
        sViewsWithIds.put(R.id.air_quality_chart_background, 29);
        sViewsWithIds.put(R.id.air_quality_chart, 30);
        sViewsWithIds.put(R.id.air_quality_user_max_label, 31);
        sViewsWithIds.put(R.id.air_quality_loading_text_view, 32);
        sViewsWithIds.put(R.id.air_quality_line, 33);
        sViewsWithIds.put(R.id.air_quality_circle, 34);
        sViewsWithIds.put(R.id.tutorial_frame_graph_three, 35);
        sViewsWithIds.put(R.id.fake_graph_three, 36);
        sViewsWithIds.put(R.id.fake_graph_gradient_three, 37);
    }
    // views
    @NonNull
    public final lecho.lib.hellocharts.view.LineChartView airQualityChart;
    @NonNull
    public final android.widget.RelativeLayout airQualityChartBackground;
    @NonNull
    public final is.yranac.canary.ui.views.CircleView airQualityCircle;
    @NonNull
    public final android.widget.ImageView airQualityInfo;
    @NonNull
    public final android.view.View airQualityLine;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus airQualityLoadingTextView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus airQualityReadingTextView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus airQualityUserMaxLabel;
    @NonNull
    public final android.widget.ImageView fakeGraph;
    @NonNull
    public final android.widget.ImageView fakeGraphGradient;
    @NonNull
    public final android.widget.ImageView fakeGraphGradientThree;
    @NonNull
    public final android.widget.ImageView fakeGraphGradientTwo;
    @NonNull
    public final android.widget.ImageView fakeGraphThree;
    @NonNull
    public final android.widget.ImageView fakeGraphTwo;
    @NonNull
    public final is.yranac.canary.ui.views.CustomScrollView graphScrollView;
    @Nullable
    public final is.yranac.canary.databinding.LayoutHomehealthHeaderBinding header;
    @NonNull
    public final lecho.lib.hellocharts.view.LineChartView humidityChart;
    @NonNull
    public final android.widget.RelativeLayout humidityChartBackground;
    @NonNull
    public final is.yranac.canary.ui.views.CircleView humidityCircle;
    @NonNull
    public final android.widget.ImageView humidityInfo;
    @NonNull
    public final android.view.View humidityLine;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus humidityLoadingTextView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus humidityReadingTextView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus humidityUserMaxLabel;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus humidityUserMinLabel;
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    @NonNull
    public final android.widget.RelativeLayout tempChartBackground;
    @NonNull
    public final is.yranac.canary.ui.views.CircleView tempCircle;
    @NonNull
    public final android.view.View tempLine;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus tempLoadingTextView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus tempUserMaxLabel;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus tempUserMinLabel;
    @NonNull
    public final lecho.lib.hellocharts.view.LineChartView temperatureChart;
    @NonNull
    public final android.widget.ImageView temperatureInfo;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus temperatureReadingTextView;
    @NonNull
    public final android.widget.RelativeLayout tutorialFrameGraph;
    @NonNull
    public final android.widget.RelativeLayout tutorialFrameGraphThree;
    @NonNull
    public final android.widget.RelativeLayout tutorialFrameGraphTwo;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentHealthGraphBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 1);
        final Object[] bindings = mapBindings(bindingComponent, root, 38, sIncludes, sViewsWithIds);
        this.airQualityChart = (lecho.lib.hellocharts.view.LineChartView) bindings[30];
        this.airQualityChartBackground = (android.widget.RelativeLayout) bindings[29];
        this.airQualityCircle = (is.yranac.canary.ui.views.CircleView) bindings[34];
        this.airQualityInfo = (android.widget.ImageView) bindings[27];
        this.airQualityLine = (android.view.View) bindings[33];
        this.airQualityLoadingTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[32];
        this.airQualityReadingTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[28];
        this.airQualityUserMaxLabel = (is.yranac.canary.ui.views.TextViewPlus) bindings[31];
        this.fakeGraph = (android.widget.ImageView) bindings[13];
        this.fakeGraphGradient = (android.widget.ImageView) bindings[14];
        this.fakeGraphGradientThree = (android.widget.ImageView) bindings[37];
        this.fakeGraphGradientTwo = (android.widget.ImageView) bindings[26];
        this.fakeGraphThree = (android.widget.ImageView) bindings[36];
        this.fakeGraphTwo = (android.widget.ImageView) bindings[25];
        this.graphScrollView = (is.yranac.canary.ui.views.CustomScrollView) bindings[2];
        this.header = (is.yranac.canary.databinding.LayoutHomehealthHeaderBinding) bindings[1];
        setContainedBinding(this.header);
        this.humidityChart = (lecho.lib.hellocharts.view.LineChartView) bindings[18];
        this.humidityChartBackground = (android.widget.RelativeLayout) bindings[17];
        this.humidityCircle = (is.yranac.canary.ui.views.CircleView) bindings[23];
        this.humidityInfo = (android.widget.ImageView) bindings[15];
        this.humidityLine = (android.view.View) bindings[22];
        this.humidityLoadingTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[21];
        this.humidityReadingTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[16];
        this.humidityUserMaxLabel = (is.yranac.canary.ui.views.TextViewPlus) bindings[19];
        this.humidityUserMinLabel = (is.yranac.canary.ui.views.TextViewPlus) bindings[20];
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.tempChartBackground = (android.widget.RelativeLayout) bindings[5];
        this.tempCircle = (is.yranac.canary.ui.views.CircleView) bindings[11];
        this.tempLine = (android.view.View) bindings[10];
        this.tempLoadingTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[9];
        this.tempUserMaxLabel = (is.yranac.canary.ui.views.TextViewPlus) bindings[7];
        this.tempUserMinLabel = (is.yranac.canary.ui.views.TextViewPlus) bindings[8];
        this.temperatureChart = (lecho.lib.hellocharts.view.LineChartView) bindings[6];
        this.temperatureInfo = (android.widget.ImageView) bindings[3];
        this.temperatureReadingTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[4];
        this.tutorialFrameGraph = (android.widget.RelativeLayout) bindings[12];
        this.tutorialFrameGraphThree = (android.widget.RelativeLayout) bindings[35];
        this.tutorialFrameGraphTwo = (android.widget.RelativeLayout) bindings[24];
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
        }
        header.invalidateAll();
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        if (header.hasPendingBindings()) {
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
                return onChangeHeader((is.yranac.canary.databinding.LayoutHomehealthHeaderBinding) object, fieldId);
        }
        return false;
    }
    private boolean onChangeHeader(is.yranac.canary.databinding.LayoutHomehealthHeaderBinding Header, int fieldId) {
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
        executeBindingsOn(header);
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static FragmentHealthGraphBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentHealthGraphBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentHealthGraphBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_health_graph, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentHealthGraphBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentHealthGraphBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_health_graph, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentHealthGraphBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentHealthGraphBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_health_graph_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentHealthGraphBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): header
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}