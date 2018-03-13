package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentVideoPlaybackBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.offline_container, 1);
        sViewsWithIds.put(R.id.text_canary_status, 2);
        sViewsWithIds.put(R.id.event_device_thumbnail_container, 3);
        sViewsWithIds.put(R.id.event_device_thumbnail_cropped, 4);
        sViewsWithIds.put(R.id.event_device_thumbnail, 5);
        sViewsWithIds.put(R.id.play_btn, 6);
        sViewsWithIds.put(R.id.video_loading_overlay, 7);
        sViewsWithIds.put(R.id.flex_loading_progress_bar, 8);
        sViewsWithIds.put(R.id.flex_loading_text, 9);
    }
    // views
    @NonNull
    public final is.yranac.canary.ui.views.ThumbnailImageView eventDeviceThumbnail;
    @NonNull
    public final android.widget.FrameLayout eventDeviceThumbnailContainer;
    @NonNull
    public final android.widget.ImageView eventDeviceThumbnailCropped;
    @NonNull
    public final android.widget.ProgressBar flexLoadingProgressBar;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus flexLoadingText;
    @NonNull
    private final android.widget.FrameLayout mboundView0;
    @NonNull
    public final android.widget.FrameLayout offlineContainer;
    @NonNull
    public final android.widget.ImageView playBtn;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus textCanaryStatus;
    @NonNull
    public final android.widget.FrameLayout videoLoadingOverlay;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentVideoPlaybackBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 10, sIncludes, sViewsWithIds);
        this.eventDeviceThumbnail = (is.yranac.canary.ui.views.ThumbnailImageView) bindings[5];
        this.eventDeviceThumbnailContainer = (android.widget.FrameLayout) bindings[3];
        this.eventDeviceThumbnailCropped = (android.widget.ImageView) bindings[4];
        this.flexLoadingProgressBar = (android.widget.ProgressBar) bindings[8];
        this.flexLoadingText = (is.yranac.canary.ui.views.TextViewPlus) bindings[9];
        this.mboundView0 = (android.widget.FrameLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.offlineContainer = (android.widget.FrameLayout) bindings[1];
        this.playBtn = (android.widget.ImageView) bindings[6];
        this.textCanaryStatus = (is.yranac.canary.ui.views.TextViewPlus) bindings[2];
        this.videoLoadingOverlay = (android.widget.FrameLayout) bindings[7];
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
    public static FragmentVideoPlaybackBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentVideoPlaybackBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentVideoPlaybackBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_video_playback, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentVideoPlaybackBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentVideoPlaybackBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_video_playback, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentVideoPlaybackBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentVideoPlaybackBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_video_playback_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentVideoPlaybackBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}