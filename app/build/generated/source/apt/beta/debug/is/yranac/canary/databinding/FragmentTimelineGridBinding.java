package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentTimelineGridBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.empty_view, 3);
        sViewsWithIds.put(R.id.loading_view, 4);
        sViewsWithIds.put(R.id.empty_view_no_entries, 5);
        sViewsWithIds.put(R.id.empty_image_view, 6);
        sViewsWithIds.put(R.id.empty_text_view, 7);
        sViewsWithIds.put(R.id.timeline_grid_view, 8);
        sViewsWithIds.put(R.id.black_overlay_view, 9);
        sViewsWithIds.put(R.id.calender_background, 10);
        sViewsWithIds.put(R.id.month_label, 11);
        sViewsWithIds.put(R.id.calender_view_pager, 12);
        sViewsWithIds.put(R.id.header_title_text_view, 13);
        sViewsWithIds.put(R.id.triangle_view, 14);
    }
    // views
    @NonNull
    public final android.view.View blackOverlayView;
    @NonNull
    public final android.view.View calenderBackground;
    @NonNull
    public final android.widget.RelativeLayout calenderLayout;
    @NonNull
    public final android.support.v4.view.ViewPager calenderViewPager;
    @NonNull
    public final android.widget.ImageView emptyImageView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus emptyTextView;
    @NonNull
    public final android.widget.RelativeLayout emptyView;
    @NonNull
    public final android.widget.LinearLayout emptyViewNoEntries;
    @NonNull
    public final android.widget.RelativeLayout gridLayout;
    @NonNull
    public final android.widget.LinearLayout header;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus headerTitleTextView;
    @NonNull
    public final android.widget.FrameLayout loadingView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus monthLabel;
    @NonNull
    public final android.widget.GridView timelineGridView;
    @NonNull
    public final is.yranac.canary.ui.views.TriangleView triangleView;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentTimelineGridBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 15, sIncludes, sViewsWithIds);
        this.blackOverlayView = (android.view.View) bindings[9];
        this.calenderBackground = (android.view.View) bindings[10];
        this.calenderLayout = (android.widget.RelativeLayout) bindings[1];
        this.calenderLayout.setTag(null);
        this.calenderViewPager = (android.support.v4.view.ViewPager) bindings[12];
        this.emptyImageView = (android.widget.ImageView) bindings[6];
        this.emptyTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[7];
        this.emptyView = (android.widget.RelativeLayout) bindings[3];
        this.emptyViewNoEntries = (android.widget.LinearLayout) bindings[5];
        this.gridLayout = (android.widget.RelativeLayout) bindings[0];
        this.gridLayout.setTag(null);
        this.header = (android.widget.LinearLayout) bindings[2];
        this.header.setTag(null);
        this.headerTitleTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[13];
        this.loadingView = (android.widget.FrameLayout) bindings[4];
        this.monthLabel = (is.yranac.canary.ui.views.TextViewPlus) bindings[11];
        this.timelineGridView = (android.widget.GridView) bindings[8];
        this.triangleView = (is.yranac.canary.ui.views.TriangleView) bindings[14];
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
    public static FragmentTimelineGridBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentTimelineGridBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentTimelineGridBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_timeline_grid, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentTimelineGridBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentTimelineGridBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_timeline_grid, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentTimelineGridBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentTimelineGridBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_timeline_grid_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentTimelineGridBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}