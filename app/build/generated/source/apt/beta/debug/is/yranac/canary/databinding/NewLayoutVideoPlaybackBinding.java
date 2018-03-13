package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class NewLayoutVideoPlaybackBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.video_view_pager, 1);
        sViewsWithIds.put(R.id.textureView_container, 2);
        sViewsWithIds.put(R.id.textureView, 3);
        sViewsWithIds.put(R.id.watermark, 4);
    }
    // views
    @NonNull
    public final is.yranac.canary.ui.views.ZoomTextureView textureView;
    @NonNull
    public final android.widget.RelativeLayout textureViewContainer;
    @NonNull
    public final android.widget.FrameLayout videoFrame;
    @NonNull
    public final android.support.v4.view.ViewPager videoViewPager;
    @NonNull
    public final android.widget.ImageView watermark;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public NewLayoutVideoPlaybackBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 5, sIncludes, sViewsWithIds);
        this.textureView = (is.yranac.canary.ui.views.ZoomTextureView) bindings[3];
        this.textureViewContainer = (android.widget.RelativeLayout) bindings[2];
        this.videoFrame = (android.widget.FrameLayout) bindings[0];
        this.videoFrame.setTag(null);
        this.videoViewPager = (android.support.v4.view.ViewPager) bindings[1];
        this.watermark = (android.widget.ImageView) bindings[4];
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
    public static NewLayoutVideoPlaybackBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static NewLayoutVideoPlaybackBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<NewLayoutVideoPlaybackBinding>inflate(inflater, is.yranac.canary.R.layout.new_layout_video_playback, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static NewLayoutVideoPlaybackBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static NewLayoutVideoPlaybackBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.new_layout_video_playback, null, false), bindingComponent);
    }
    @NonNull
    public static NewLayoutVideoPlaybackBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static NewLayoutVideoPlaybackBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/new_layout_video_playback_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new NewLayoutVideoPlaybackBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}