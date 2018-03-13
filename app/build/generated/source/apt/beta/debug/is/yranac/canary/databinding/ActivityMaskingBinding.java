package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityMaskingBinding extends android.databinding.ViewDataBinding implements android.databinding.generated.callback.OnClickListener.Listener {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.background_image, 14);
        sViewsWithIds.put(R.id.no_preview_image, 15);
        sViewsWithIds.put(R.id.image_preview_text, 16);
        sViewsWithIds.put(R.id.mask_draw_view, 17);
        sViewsWithIds.put(R.id.tutorial_layout, 18);
        sViewsWithIds.put(R.id.tutorial_text, 19);
        sViewsWithIds.put(R.id.next_btn, 20);
    }
    // views
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus addMasksButtonText;
    @NonNull
    public final is.yranac.canary.ui.views.ThumbnailImageView backgroundImage;
    @NonNull
    public final android.widget.LinearLayout bottomOptions;
    @NonNull
    public final android.widget.RelativeLayout bottomOptionsAdd;
    @NonNull
    public final android.widget.RelativeLayout bottomOptionsDelete;
    @NonNull
    public final android.widget.RelativeLayout bottomOptionsSave;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus deleteMaskButtonText;
    @NonNull
    public final android.widget.TextView getHelp;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus imagePreviewText;
    @NonNull
    public final android.widget.RelativeLayout mainLayout;
    @NonNull
    public final is.yranac.canary.ui.views.masking.MaskDrawingView maskDrawView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus maskingTopMessage;
    @NonNull
    public final android.widget.RelativeLayout maskingTopMessageLayout;
    @NonNull
    private final android.widget.ImageView mboundView12;
    @NonNull
    private final is.yranac.canary.ui.views.TextViewPlus mboundView13;
    @NonNull
    private final android.widget.ImageView mboundView6;
    @NonNull
    private final android.widget.ImageView mboundView9;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus nextBtn;
    @NonNull
    public final android.widget.FrameLayout noPreviewImage;
    @NonNull
    public final android.widget.RelativeLayout tutorialLayout;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus tutorialText;
    // variables
    @Nullable
    private int mNumberOfMasks;
    @Nullable
    private is.yranac.canary.model.MaskingViewController mControlListener;
    @Nullable
    private final android.view.View.OnClickListener mCallback2;
    @Nullable
    private final android.view.View.OnClickListener mCallback1;
    @Nullable
    private final android.view.View.OnClickListener mCallback4;
    @Nullable
    private final android.view.View.OnClickListener mCallback3;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityMaskingBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 1);
        final Object[] bindings = mapBindings(bindingComponent, root, 21, sIncludes, sViewsWithIds);
        this.addMasksButtonText = (is.yranac.canary.ui.views.TextViewPlus) bindings[7];
        this.addMasksButtonText.setTag(null);
        this.backgroundImage = (is.yranac.canary.ui.views.ThumbnailImageView) bindings[14];
        this.bottomOptions = (android.widget.LinearLayout) bindings[4];
        this.bottomOptions.setTag(null);
        this.bottomOptionsAdd = (android.widget.RelativeLayout) bindings[5];
        this.bottomOptionsAdd.setTag(null);
        this.bottomOptionsDelete = (android.widget.RelativeLayout) bindings[8];
        this.bottomOptionsDelete.setTag(null);
        this.bottomOptionsSave = (android.widget.RelativeLayout) bindings[11];
        this.bottomOptionsSave.setTag(null);
        this.deleteMaskButtonText = (is.yranac.canary.ui.views.TextViewPlus) bindings[10];
        this.deleteMaskButtonText.setTag(null);
        this.getHelp = (android.widget.TextView) bindings[3];
        this.getHelp.setTag(null);
        this.imagePreviewText = (is.yranac.canary.ui.views.TextViewPlus) bindings[16];
        this.mainLayout = (android.widget.RelativeLayout) bindings[0];
        this.mainLayout.setTag(null);
        this.maskDrawView = (is.yranac.canary.ui.views.masking.MaskDrawingView) bindings[17];
        this.maskingTopMessage = (is.yranac.canary.ui.views.TextViewPlus) bindings[2];
        this.maskingTopMessage.setTag(null);
        this.maskingTopMessageLayout = (android.widget.RelativeLayout) bindings[1];
        this.maskingTopMessageLayout.setTag(null);
        this.mboundView12 = (android.widget.ImageView) bindings[12];
        this.mboundView12.setTag(null);
        this.mboundView13 = (is.yranac.canary.ui.views.TextViewPlus) bindings[13];
        this.mboundView13.setTag(null);
        this.mboundView6 = (android.widget.ImageView) bindings[6];
        this.mboundView6.setTag(null);
        this.mboundView9 = (android.widget.ImageView) bindings[9];
        this.mboundView9.setTag(null);
        this.nextBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[20];
        this.noPreviewImage = (android.widget.FrameLayout) bindings[15];
        this.tutorialLayout = (android.widget.RelativeLayout) bindings[18];
        this.tutorialText = (is.yranac.canary.ui.views.TextViewPlus) bindings[19];
        setRootTag(root);
        // listeners
        mCallback2 = new android.databinding.generated.callback.OnClickListener(this, 2);
        mCallback1 = new android.databinding.generated.callback.OnClickListener(this, 1);
        mCallback4 = new android.databinding.generated.callback.OnClickListener(this, 4);
        mCallback3 = new android.databinding.generated.callback.OnClickListener(this, 3);
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x100L;
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
        if (BR.numberOfMasks == variableId) {
            setNumberOfMasks((int) variable);
        }
        else if (BR.controlListener == variableId) {
            setControlListener((is.yranac.canary.model.MaskingViewController) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setNumberOfMasks(int NumberOfMasks) {
        this.mNumberOfMasks = NumberOfMasks;
    }
    public int getNumberOfMasks() {
        return mNumberOfMasks;
    }
    public void setControlListener(@Nullable is.yranac.canary.model.MaskingViewController ControlListener) {
        updateRegistration(0, ControlListener);
        this.mControlListener = ControlListener;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.controlListener);
        super.requestRebind();
    }
    @Nullable
    public is.yranac.canary.model.MaskingViewController getControlListener() {
        return mControlListener;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeControlListener((is.yranac.canary.model.MaskingViewController) object, fieldId);
        }
        return false;
    }
    private boolean onChangeControlListener(is.yranac.canary.model.MaskingViewController ControlListener, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        else if (fieldId == BR.showOptions) {
            synchronized(this) {
                    mDirtyFlags |= 0x4L;
            }
            return true;
        }
        else if (fieldId == BR.topMessage) {
            synchronized(this) {
                    mDirtyFlags |= 0x8L;
            }
            return true;
        }
        else if (fieldId == BR.addEnabled) {
            synchronized(this) {
                    mDirtyFlags |= 0x10L;
            }
            return true;
        }
        else if (fieldId == BR.deleteEnabled) {
            synchronized(this) {
                    mDirtyFlags |= 0x20L;
            }
            return true;
        }
        else if (fieldId == BR.saveEnabled) {
            synchronized(this) {
                    mDirtyFlags |= 0x40L;
            }
            return true;
        }
        else if (fieldId == BR.multipleMasks) {
            synchronized(this) {
                    mDirtyFlags |= 0x80L;
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
        android.graphics.drawable.Drawable controlListenerAddEnabledMboundView6AndroidDrawableAddIconMboundView6AndroidDrawableAddIconDisabled = null;
        int controlListenerAddEnabledAddMasksButtonTextAndroidColorWhiteAddMasksButtonTextAndroidColorWhiteThirty = 0;
        android.graphics.drawable.Drawable controlListenerDeleteEnabledMboundView9AndroidDrawableDeleteIconMboundView9AndroidDrawableDeleteIconDisabled = null;
        java.lang.String controlListenerMultipleMasksMboundView13AndroidStringMaskingSaveMasksMboundView13AndroidStringMaskingSaveMask = null;
        java.lang.String controlListenerTopMessage = null;
        int controlListenerDeleteEnabledDeleteMaskButtonTextAndroidColorWhiteDeleteMaskButtonTextAndroidColorWhiteThirty = 0;
        boolean controlListenerShowOptions = false;
        int controlListenerSaveEnabledMboundView13AndroidColorWhiteMboundView13AndroidColorWhiteThirty = 0;
        android.graphics.drawable.Drawable controlListenerSaveEnabledMboundView12AndroidDrawableSaveIconMboundView12AndroidDrawableSaveIconDisabled = null;
        boolean controlListenerSaveEnabled = false;
        boolean controlListenerAddEnabled = false;
        is.yranac.canary.model.MaskingViewController controlListener = mControlListener;
        boolean controlListenerMultipleMasks = false;
        boolean controlListenerDeleteEnabled = false;

        if ((dirtyFlags & 0x1fdL) != 0) {


            if ((dirtyFlags & 0x109L) != 0) {

                    if (controlListener != null) {
                        // read controlListener.topMessage
                        controlListenerTopMessage = controlListener.getTopMessage();
                    }
            }
            if ((dirtyFlags & 0x105L) != 0) {

                    if (controlListener != null) {
                        // read controlListener.showOptions
                        controlListenerShowOptions = controlListener.isShowOptions();
                    }
            }
            if ((dirtyFlags & 0x141L) != 0) {

                    if (controlListener != null) {
                        // read controlListener.saveEnabled
                        controlListenerSaveEnabled = controlListener.isSaveEnabled();
                    }
                if((dirtyFlags & 0x141L) != 0) {
                    if(controlListenerSaveEnabled) {
                            dirtyFlags |= 0x100000L;
                            dirtyFlags |= 0x400000L;
                    }
                    else {
                            dirtyFlags |= 0x80000L;
                            dirtyFlags |= 0x200000L;
                    }
                }


                    // read controlListener.saveEnabled ? @android:color/white : @android:color/white_thirty
                    controlListenerSaveEnabledMboundView13AndroidColorWhiteMboundView13AndroidColorWhiteThirty = ((controlListenerSaveEnabled) ? (getColorFromResource(mboundView13, R.color.white)) : (getColorFromResource(mboundView13, R.color.white_thirty)));
                    // read controlListener.saveEnabled ? @android:drawable/save_icon : @android:drawable/save_icon_disabled
                    controlListenerSaveEnabledMboundView12AndroidDrawableSaveIconMboundView12AndroidDrawableSaveIconDisabled = ((controlListenerSaveEnabled) ? (getDrawableFromResource(mboundView12, R.drawable.save_icon)) : (getDrawableFromResource(mboundView12, R.drawable.save_icon_disabled)));
            }
            if ((dirtyFlags & 0x111L) != 0) {

                    if (controlListener != null) {
                        // read controlListener.addEnabled
                        controlListenerAddEnabled = controlListener.isAddEnabled();
                    }
                if((dirtyFlags & 0x111L) != 0) {
                    if(controlListenerAddEnabled) {
                            dirtyFlags |= 0x400L;
                            dirtyFlags |= 0x1000L;
                    }
                    else {
                            dirtyFlags |= 0x200L;
                            dirtyFlags |= 0x800L;
                    }
                }


                    // read controlListener.addEnabled ? @android:drawable/add_icon : @android:drawable/add_icon_disabled
                    controlListenerAddEnabledMboundView6AndroidDrawableAddIconMboundView6AndroidDrawableAddIconDisabled = ((controlListenerAddEnabled) ? (getDrawableFromResource(mboundView6, R.drawable.add_icon)) : (getDrawableFromResource(mboundView6, R.drawable.add_icon_disabled)));
                    // read controlListener.addEnabled ? @android:color/white : @android:color/white_thirty
                    controlListenerAddEnabledAddMasksButtonTextAndroidColorWhiteAddMasksButtonTextAndroidColorWhiteThirty = ((controlListenerAddEnabled) ? (getColorFromResource(addMasksButtonText, R.color.white)) : (getColorFromResource(addMasksButtonText, R.color.white_thirty)));
            }
            if ((dirtyFlags & 0x181L) != 0) {

                    if (controlListener != null) {
                        // read controlListener.multipleMasks
                        controlListenerMultipleMasks = controlListener.isMultipleMasks();
                    }
                if((dirtyFlags & 0x181L) != 0) {
                    if(controlListenerMultipleMasks) {
                            dirtyFlags |= 0x10000L;
                    }
                    else {
                            dirtyFlags |= 0x8000L;
                    }
                }


                    // read controlListener.multipleMasks ? @android:string/masking_save_masks : @android:string/masking_save_mask
                    controlListenerMultipleMasksMboundView13AndroidStringMaskingSaveMasksMboundView13AndroidStringMaskingSaveMask = ((controlListenerMultipleMasks) ? (mboundView13.getResources().getString(R.string.masking_save_masks)) : (mboundView13.getResources().getString(R.string.masking_save_mask)));
            }
            if ((dirtyFlags & 0x121L) != 0) {

                    if (controlListener != null) {
                        // read controlListener.deleteEnabled
                        controlListenerDeleteEnabled = controlListener.isDeleteEnabled();
                    }
                if((dirtyFlags & 0x121L) != 0) {
                    if(controlListenerDeleteEnabled) {
                            dirtyFlags |= 0x4000L;
                            dirtyFlags |= 0x40000L;
                    }
                    else {
                            dirtyFlags |= 0x2000L;
                            dirtyFlags |= 0x20000L;
                    }
                }


                    // read controlListener.deleteEnabled ? @android:drawable/delete_icon : @android:drawable/delete_icon_disabled
                    controlListenerDeleteEnabledMboundView9AndroidDrawableDeleteIconMboundView9AndroidDrawableDeleteIconDisabled = ((controlListenerDeleteEnabled) ? (getDrawableFromResource(mboundView9, R.drawable.delete_icon)) : (getDrawableFromResource(mboundView9, R.drawable.delete_icon_disabled)));
                    // read controlListener.deleteEnabled ? @android:color/white : @android:color/white_thirty
                    controlListenerDeleteEnabledDeleteMaskButtonTextAndroidColorWhiteDeleteMaskButtonTextAndroidColorWhiteThirty = ((controlListenerDeleteEnabled) ? (getColorFromResource(deleteMaskButtonText, R.color.white)) : (getColorFromResource(deleteMaskButtonText, R.color.white_thirty)));
            }
        }
        // batch finished
        if ((dirtyFlags & 0x111L) != 0) {
            // api target 1

            this.addMasksButtonText.setTextColor(controlListenerAddEnabledAddMasksButtonTextAndroidColorWhiteAddMasksButtonTextAndroidColorWhiteThirty);
            android.databinding.adapters.ImageViewBindingAdapter.setImageDrawable(this.mboundView6, controlListenerAddEnabledMboundView6AndroidDrawableAddIconMboundView6AndroidDrawableAddIconDisabled);
        }
        if ((dirtyFlags & 0x105L) != 0) {
            // api target 1

            is.yranac.canary.model.MaskingViewController.setSlideVisible(this.bottomOptions, controlListenerShowOptions);
            is.yranac.canary.model.MaskingViewController.setSlideVisible(this.maskingTopMessageLayout, controlListenerShowOptions);
        }
        if ((dirtyFlags & 0x100L) != 0) {
            // api target 1

            this.bottomOptionsAdd.setOnClickListener(mCallback2);
            this.bottomOptionsDelete.setOnClickListener(mCallback3);
            this.bottomOptionsSave.setOnClickListener(mCallback4);
            this.getHelp.setOnClickListener(mCallback1);
        }
        if ((dirtyFlags & 0x121L) != 0) {
            // api target 1

            this.deleteMaskButtonText.setTextColor(controlListenerDeleteEnabledDeleteMaskButtonTextAndroidColorWhiteDeleteMaskButtonTextAndroidColorWhiteThirty);
            android.databinding.adapters.ImageViewBindingAdapter.setImageDrawable(this.mboundView9, controlListenerDeleteEnabledMboundView9AndroidDrawableDeleteIconMboundView9AndroidDrawableDeleteIconDisabled);
        }
        if ((dirtyFlags & 0x109L) != 0) {
            // api target 1

            android.databinding.adapters.TextViewBindingAdapter.setText(this.maskingTopMessage, controlListenerTopMessage);
        }
        if ((dirtyFlags & 0x141L) != 0) {
            // api target 1

            android.databinding.adapters.ImageViewBindingAdapter.setImageDrawable(this.mboundView12, controlListenerSaveEnabledMboundView12AndroidDrawableSaveIconMboundView12AndroidDrawableSaveIconDisabled);
            this.mboundView13.setTextColor(controlListenerSaveEnabledMboundView13AndroidColorWhiteMboundView13AndroidColorWhiteThirty);
        }
        if ((dirtyFlags & 0x181L) != 0) {
            // api target 1

            android.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView13, controlListenerMultipleMasksMboundView13AndroidStringMaskingSaveMasksMboundView13AndroidStringMaskingSaveMask);
        }
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnClick(int sourceId , android.view.View callbackArg_0) {
        switch(sourceId) {
            case 2: {
                // localize variables for thread safety
                // controlListener != null
                boolean controlListenerJavaLangObjectNull = false;
                // controlListener
                is.yranac.canary.model.MaskingViewController controlListener = mControlListener;



                controlListenerJavaLangObjectNull = (controlListener) != (null);
                if (controlListenerJavaLangObjectNull) {



                    controlListener.optionsClicked(callbackArg_0);
                }
                break;
            }
            case 1: {
                // localize variables for thread safety
                // controlListener != null
                boolean controlListenerJavaLangObjectNull = false;
                // controlListener
                is.yranac.canary.model.MaskingViewController controlListener = mControlListener;



                controlListenerJavaLangObjectNull = (controlListener) != (null);
                if (controlListenerJavaLangObjectNull) {



                    controlListener.getHelp(callbackArg_0);
                }
                break;
            }
            case 4: {
                // localize variables for thread safety
                // controlListener != null
                boolean controlListenerJavaLangObjectNull = false;
                // controlListener
                is.yranac.canary.model.MaskingViewController controlListener = mControlListener;



                controlListenerJavaLangObjectNull = (controlListener) != (null);
                if (controlListenerJavaLangObjectNull) {



                    controlListener.optionsClicked(callbackArg_0);
                }
                break;
            }
            case 3: {
                // localize variables for thread safety
                // controlListener != null
                boolean controlListenerJavaLangObjectNull = false;
                // controlListener
                is.yranac.canary.model.MaskingViewController controlListener = mControlListener;



                controlListenerJavaLangObjectNull = (controlListener) != (null);
                if (controlListenerJavaLangObjectNull) {



                    controlListener.optionsClicked(callbackArg_0);
                }
                break;
            }
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static ActivityMaskingBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ActivityMaskingBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<ActivityMaskingBinding>inflate(inflater, is.yranac.canary.R.layout.activity_masking, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static ActivityMaskingBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ActivityMaskingBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.activity_masking, null, false), bindingComponent);
    }
    @NonNull
    public static ActivityMaskingBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ActivityMaskingBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/activity_masking_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new ActivityMaskingBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): controlListener
        flag 1 (0x2L): numberOfMasks
        flag 2 (0x3L): controlListener.showOptions
        flag 3 (0x4L): controlListener.topMessage
        flag 4 (0x5L): controlListener.addEnabled
        flag 5 (0x6L): controlListener.deleteEnabled
        flag 6 (0x7L): controlListener.saveEnabled
        flag 7 (0x8L): controlListener.multipleMasks
        flag 8 (0x9L): null
        flag 9 (0xaL): controlListener.addEnabled ? @android:drawable/add_icon : @android:drawable/add_icon_disabled
        flag 10 (0xbL): controlListener.addEnabled ? @android:drawable/add_icon : @android:drawable/add_icon_disabled
        flag 11 (0xcL): controlListener.addEnabled ? @android:color/white : @android:color/white_thirty
        flag 12 (0xdL): controlListener.addEnabled ? @android:color/white : @android:color/white_thirty
        flag 13 (0xeL): controlListener.deleteEnabled ? @android:drawable/delete_icon : @android:drawable/delete_icon_disabled
        flag 14 (0xfL): controlListener.deleteEnabled ? @android:drawable/delete_icon : @android:drawable/delete_icon_disabled
        flag 15 (0x10L): controlListener.multipleMasks ? @android:string/masking_save_masks : @android:string/masking_save_mask
        flag 16 (0x11L): controlListener.multipleMasks ? @android:string/masking_save_masks : @android:string/masking_save_mask
        flag 17 (0x12L): controlListener.deleteEnabled ? @android:color/white : @android:color/white_thirty
        flag 18 (0x13L): controlListener.deleteEnabled ? @android:color/white : @android:color/white_thirty
        flag 19 (0x14L): controlListener.saveEnabled ? @android:color/white : @android:color/white_thirty
        flag 20 (0x15L): controlListener.saveEnabled ? @android:color/white : @android:color/white_thirty
        flag 21 (0x16L): controlListener.saveEnabled ? @android:drawable/save_icon : @android:drawable/save_icon_disabled
        flag 22 (0x17L): controlListener.saveEnabled ? @android:drawable/save_icon : @android:drawable/save_icon_disabled
    flag mapping end*/
    //end
}