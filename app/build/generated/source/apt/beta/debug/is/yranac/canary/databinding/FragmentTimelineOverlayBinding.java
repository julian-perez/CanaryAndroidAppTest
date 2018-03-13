package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentTimelineOverlayBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.second_device_arrow, 6);
        sViewsWithIds.put(R.id.timeline_tutorial_desc1, 7);
        sViewsWithIds.put(R.id.timeline_tutorial_arrow1, 8);
        sViewsWithIds.put(R.id.timeline_tutorial_arrow2, 9);
        sViewsWithIds.put(R.id.timeline_tutorial_desc2, 10);
        sViewsWithIds.put(R.id.timeline_tutorial_arrow3, 11);
        sViewsWithIds.put(R.id.timeline_tutorial_desc3, 12);
    }
    // views
    @NonNull
    private final android.widget.FrameLayout mboundView0;
    @NonNull
    private final android.widget.RelativeLayout mboundView2;
    @NonNull
    private final android.widget.RelativeLayout mboundView3;
    @NonNull
    private final android.widget.RelativeLayout mboundView4;
    @NonNull
    private final android.widget.RelativeLayout mboundView5;
    @NonNull
    public final android.widget.ImageView secondDeviceArrow;
    @NonNull
    public final android.widget.ImageView timelineTutorialArrow1;
    @NonNull
    public final android.widget.ImageView timelineTutorialArrow2;
    @NonNull
    public final android.widget.ImageView timelineTutorialArrow3;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus timelineTutorialDesc1;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus timelineTutorialDesc2;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus timelineTutorialDesc3;
    @NonNull
    public final android.widget.ImageView tutorialImage;
    // variables
    @Nullable
    private is.yranac.canary.util.TutorialUtil.TutorialType mTutorialType;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentTimelineOverlayBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 13, sIncludes, sViewsWithIds);
        this.mboundView0 = (android.widget.FrameLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView2 = (android.widget.RelativeLayout) bindings[2];
        this.mboundView2.setTag(null);
        this.mboundView3 = (android.widget.RelativeLayout) bindings[3];
        this.mboundView3.setTag(null);
        this.mboundView4 = (android.widget.RelativeLayout) bindings[4];
        this.mboundView4.setTag(null);
        this.mboundView5 = (android.widget.RelativeLayout) bindings[5];
        this.mboundView5.setTag(null);
        this.secondDeviceArrow = (android.widget.ImageView) bindings[6];
        this.timelineTutorialArrow1 = (android.widget.ImageView) bindings[8];
        this.timelineTutorialArrow2 = (android.widget.ImageView) bindings[9];
        this.timelineTutorialArrow3 = (android.widget.ImageView) bindings[11];
        this.timelineTutorialDesc1 = (is.yranac.canary.ui.views.TextViewPlus) bindings[7];
        this.timelineTutorialDesc2 = (is.yranac.canary.ui.views.TextViewPlus) bindings[10];
        this.timelineTutorialDesc3 = (is.yranac.canary.ui.views.TextViewPlus) bindings[12];
        this.tutorialImage = (android.widget.ImageView) bindings[1];
        this.tutorialImage.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
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
        if (BR.tutorialType == variableId) {
            setTutorialType((is.yranac.canary.util.TutorialUtil.TutorialType) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setTutorialType(@Nullable is.yranac.canary.util.TutorialUtil.TutorialType TutorialType) {
        this.mTutorialType = TutorialType;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.tutorialType);
        super.requestRebind();
    }
    @Nullable
    public is.yranac.canary.util.TutorialUtil.TutorialType getTutorialType() {
        return mTutorialType;
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
        int tutorialTypeTutorialTypeTIMELINEFILTERViewVISIBLEViewGONE = 0;
        boolean tutorialTypeTutorialTypeTIMELINE = false;
        int tutorialTypeTutorialTypeENTRYMOREOPTIONSViewVISIBLEViewGONE = 0;
        boolean tutorialTypeTutorialTypeSECONDDEVICEBooleanTrueTutorialTypeTutorialTypeTIMELINE = false;
        int tutorialTypeTutorialTypeSECONDDEVICEViewVISIBLEViewGONE = 0;
        int tutorialTypeTutorialTypeTIMELINEViewVISIBLEViewGONE = 0;
        is.yranac.canary.util.TutorialUtil.TutorialType tutorialType = mTutorialType;
        int tutorialTypeTutorialTypeSECONDDEVICEBooleanTrueTutorialTypeTutorialTypeTIMELINEViewVISIBLEViewGONE = 0;
        boolean tutorialTypeTutorialTypeENTRYMOREOPTIONS = false;
        boolean tutorialTypeTutorialTypeSECONDDEVICE = false;
        boolean tutorialTypeTutorialTypeTIMELINEFILTER = false;

        if ((dirtyFlags & 0x3L) != 0) {



                // read tutorialType == TutorialType.TIMELINE
                tutorialTypeTutorialTypeTIMELINE = (tutorialType) == (is.yranac.canary.util.TutorialUtil.TutorialType.TIMELINE);
                // read tutorialType == TutorialType.ENTRY_MORE_OPTIONS
                tutorialTypeTutorialTypeENTRYMOREOPTIONS = (tutorialType) == (is.yranac.canary.util.TutorialUtil.TutorialType.ENTRY_MORE_OPTIONS);
                // read tutorialType == TutorialType.SECOND_DEVICE
                tutorialTypeTutorialTypeSECONDDEVICE = (tutorialType) == (is.yranac.canary.util.TutorialUtil.TutorialType.SECOND_DEVICE);
                // read tutorialType == TutorialType.TIMELINE_FILTER
                tutorialTypeTutorialTypeTIMELINEFILTER = (tutorialType) == (is.yranac.canary.util.TutorialUtil.TutorialType.TIMELINE_FILTER);
            if((dirtyFlags & 0x3L) != 0) {
                if(tutorialTypeTutorialTypeTIMELINE) {
                        dirtyFlags |= 0x800L;
                }
                else {
                        dirtyFlags |= 0x400L;
                }
            }
            if((dirtyFlags & 0x3L) != 0) {
                if(tutorialTypeTutorialTypeENTRYMOREOPTIONS) {
                        dirtyFlags |= 0x20L;
                }
                else {
                        dirtyFlags |= 0x10L;
                }
            }
            if((dirtyFlags & 0x3L) != 0) {
                if(tutorialTypeTutorialTypeSECONDDEVICE) {
                        dirtyFlags |= 0x80L;
                        dirtyFlags |= 0x200L;
                }
                else {
                        dirtyFlags |= 0x40L;
                        dirtyFlags |= 0x100L;
                }
            }
            if((dirtyFlags & 0x3L) != 0) {
                if(tutorialTypeTutorialTypeTIMELINEFILTER) {
                        dirtyFlags |= 0x8L;
                }
                else {
                        dirtyFlags |= 0x4L;
                }
            }


                // read tutorialType == TutorialType.TIMELINE ? View.VISIBLE : View.GONE
                tutorialTypeTutorialTypeTIMELINEViewVISIBLEViewGONE = ((tutorialTypeTutorialTypeTIMELINE) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
                // read tutorialType == TutorialType.ENTRY_MORE_OPTIONS ? View.VISIBLE : View.GONE
                tutorialTypeTutorialTypeENTRYMOREOPTIONSViewVISIBLEViewGONE = ((tutorialTypeTutorialTypeENTRYMOREOPTIONS) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
                // read tutorialType == TutorialType.SECOND_DEVICE ? View.VISIBLE : View.GONE
                tutorialTypeTutorialTypeSECONDDEVICEViewVISIBLEViewGONE = ((tutorialTypeTutorialTypeSECONDDEVICE) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
                // read tutorialType == TutorialType.TIMELINE_FILTER ? View.VISIBLE : View.GONE
                tutorialTypeTutorialTypeTIMELINEFILTERViewVISIBLEViewGONE = ((tutorialTypeTutorialTypeTIMELINEFILTER) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        // batch finished

        if ((dirtyFlags & 0x3L) != 0) {

                // read tutorialType == TutorialType.SECOND_DEVICE ? true : tutorialType == TutorialType.TIMELINE
                tutorialTypeTutorialTypeSECONDDEVICEBooleanTrueTutorialTypeTutorialTypeTIMELINE = ((tutorialTypeTutorialTypeSECONDDEVICE) ? (true) : (tutorialTypeTutorialTypeTIMELINE));
            if((dirtyFlags & 0x3L) != 0) {
                if(tutorialTypeTutorialTypeSECONDDEVICEBooleanTrueTutorialTypeTutorialTypeTIMELINE) {
                        dirtyFlags |= 0x2000L;
                }
                else {
                        dirtyFlags |= 0x1000L;
                }
            }


                // read tutorialType == TutorialType.SECOND_DEVICE ? true : tutorialType == TutorialType.TIMELINE ? View.VISIBLE : View.GONE
                tutorialTypeTutorialTypeSECONDDEVICEBooleanTrueTutorialTypeTutorialTypeTIMELINEViewVISIBLEViewGONE = ((tutorialTypeTutorialTypeSECONDDEVICEBooleanTrueTutorialTypeTutorialTypeTIMELINE) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            this.mboundView2.setVisibility(tutorialTypeTutorialTypeSECONDDEVICEViewVISIBLEViewGONE);
            this.mboundView3.setVisibility(tutorialTypeTutorialTypeTIMELINEViewVISIBLEViewGONE);
            this.mboundView4.setVisibility(tutorialTypeTutorialTypeTIMELINEFILTERViewVISIBLEViewGONE);
            this.mboundView5.setVisibility(tutorialTypeTutorialTypeENTRYMOREOPTIONSViewVISIBLEViewGONE);
            this.tutorialImage.setVisibility(tutorialTypeTutorialTypeSECONDDEVICEBooleanTrueTutorialTypeTutorialTypeTIMELINEViewVISIBLEViewGONE);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static FragmentTimelineOverlayBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentTimelineOverlayBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentTimelineOverlayBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_timeline_overlay, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentTimelineOverlayBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentTimelineOverlayBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_timeline_overlay, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentTimelineOverlayBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentTimelineOverlayBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_timeline_overlay_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentTimelineOverlayBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): tutorialType
        flag 1 (0x2L): null
        flag 2 (0x3L): tutorialType == TutorialType.TIMELINE_FILTER ? View.VISIBLE : View.GONE
        flag 3 (0x4L): tutorialType == TutorialType.TIMELINE_FILTER ? View.VISIBLE : View.GONE
        flag 4 (0x5L): tutorialType == TutorialType.ENTRY_MORE_OPTIONS ? View.VISIBLE : View.GONE
        flag 5 (0x6L): tutorialType == TutorialType.ENTRY_MORE_OPTIONS ? View.VISIBLE : View.GONE
        flag 6 (0x7L): tutorialType == TutorialType.SECOND_DEVICE ? true : tutorialType == TutorialType.TIMELINE
        flag 7 (0x8L): tutorialType == TutorialType.SECOND_DEVICE ? true : tutorialType == TutorialType.TIMELINE
        flag 8 (0x9L): tutorialType == TutorialType.SECOND_DEVICE ? View.VISIBLE : View.GONE
        flag 9 (0xaL): tutorialType == TutorialType.SECOND_DEVICE ? View.VISIBLE : View.GONE
        flag 10 (0xbL): tutorialType == TutorialType.TIMELINE ? View.VISIBLE : View.GONE
        flag 11 (0xcL): tutorialType == TutorialType.TIMELINE ? View.VISIBLE : View.GONE
        flag 12 (0xdL): tutorialType == TutorialType.SECOND_DEVICE ? true : tutorialType == TutorialType.TIMELINE ? View.VISIBLE : View.GONE
        flag 13 (0xeL): tutorialType == TutorialType.SECOND_DEVICE ? true : tutorialType == TutorialType.TIMELINE ? View.VISIBLE : View.GONE
    flag mapping end*/
    //end
}