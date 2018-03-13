package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ListrowEntrySimpleBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.main_layout, 4);
        sViewsWithIds.put(R.id.top_pad_terminator, 5);
        sViewsWithIds.put(R.id.bottom_pad_terminator, 6);
        sViewsWithIds.put(R.id.entry_image_view, 7);
        sViewsWithIds.put(R.id.customer_circle_container, 8);
        sViewsWithIds.put(R.id.gray_circle, 9);
        sViewsWithIds.put(R.id.customer_initials, 10);
        sViewsWithIds.put(R.id.listrow_icon, 11);
    }
    // views
    @NonNull
    public final android.view.View bottomPadTerminator;
    @NonNull
    public final android.widget.FrameLayout customerCircleContainer;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus customerInitials;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus dateTextView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus detailTextView;
    @NonNull
    public final is.yranac.canary.ui.views.CircleView entryImageView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus entrySummary;
    @NonNull
    public final is.yranac.canary.ui.views.CircleView grayCircle;
    @NonNull
    public final is.yranac.canary.ui.views.RoundedImageView listrowIcon;
    @NonNull
    public final android.widget.FrameLayout mainLayout;
    @NonNull
    private final android.widget.FrameLayout mboundView0;
    @NonNull
    public final android.view.View topPadTerminator;
    // variables
    @Nullable
    private is.yranac.canary.model.entry.Entry mEntry;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ListrowEntrySimpleBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 12, sIncludes, sViewsWithIds);
        this.bottomPadTerminator = (android.view.View) bindings[6];
        this.customerCircleContainer = (android.widget.FrameLayout) bindings[8];
        this.customerInitials = (is.yranac.canary.ui.views.TextViewPlus) bindings[10];
        this.dateTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[1];
        this.dateTextView.setTag(null);
        this.detailTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[3];
        this.detailTextView.setTag(null);
        this.entryImageView = (is.yranac.canary.ui.views.CircleView) bindings[7];
        this.entrySummary = (is.yranac.canary.ui.views.TextViewPlus) bindings[2];
        this.entrySummary.setTag(null);
        this.grayCircle = (is.yranac.canary.ui.views.CircleView) bindings[9];
        this.listrowIcon = (is.yranac.canary.ui.views.RoundedImageView) bindings[11];
        this.mainLayout = (android.widget.FrameLayout) bindings[4];
        this.mboundView0 = (android.widget.FrameLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.topPadTerminator = (android.view.View) bindings[5];
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
        if (BR.entry == variableId) {
            setEntry((is.yranac.canary.model.entry.Entry) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setEntry(@Nullable is.yranac.canary.model.entry.Entry Entry) {
        this.mEntry = Entry;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.entry);
        super.requestRebind();
    }
    @Nullable
    public is.yranac.canary.model.entry.Entry getEntry() {
        return mEntry;
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
        is.yranac.canary.model.entry.Entry entry = mEntry;
        java.lang.String entryEntryType = null;
        java.util.Date entryStartTime = null;
        boolean entryEntryTypeEqualsIgnoreCaseJavaLangStringDisconnect = false;
        int entryEntryTypeEqualsIgnoreCaseJavaLangStringDisconnectVVISIBLEVGONE = 0;
        java.lang.String dUUtcDateToDisplayStringEntryStartTime = null;
        java.lang.String entryDescription = null;

        if ((dirtyFlags & 0x3L) != 0) {



                if (entry != null) {
                    // read entry.entryType
                    entryEntryType = entry.entryType;
                    // read entry.startTime
                    entryStartTime = entry.startTime;
                    // read entry.description
                    entryDescription = entry.description;
                }


                if (entryEntryType != null) {
                    // read entry.entryType.equalsIgnoreCase("disconnect")
                    entryEntryTypeEqualsIgnoreCaseJavaLangStringDisconnect = entryEntryType.equalsIgnoreCase("disconnect");
                }
            if((dirtyFlags & 0x3L) != 0) {
                if(entryEntryTypeEqualsIgnoreCaseJavaLangStringDisconnect) {
                        dirtyFlags |= 0x8L;
                }
                else {
                        dirtyFlags |= 0x4L;
                }
            }
                // read DU.utcDateToDisplayString(entry.startTime)
                dUUtcDateToDisplayStringEntryStartTime = is.yranac.canary.util.DateUtil.utcDateToDisplayString(entryStartTime);


                // read entry.entryType.equalsIgnoreCase("disconnect") ? v.VISIBLE : v.GONE
                entryEntryTypeEqualsIgnoreCaseJavaLangStringDisconnectVVISIBLEVGONE = ((entryEntryTypeEqualsIgnoreCaseJavaLangStringDisconnect) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            android.databinding.adapters.TextViewBindingAdapter.setText(this.dateTextView, dUUtcDateToDisplayStringEntryStartTime);
            this.detailTextView.setVisibility(entryEntryTypeEqualsIgnoreCaseJavaLangStringDisconnectVVISIBLEVGONE);
            android.databinding.adapters.TextViewBindingAdapter.setText(this.entrySummary, entryDescription);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static ListrowEntrySimpleBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ListrowEntrySimpleBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<ListrowEntrySimpleBinding>inflate(inflater, is.yranac.canary.R.layout.listrow_entry_simple, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static ListrowEntrySimpleBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ListrowEntrySimpleBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.listrow_entry_simple, null, false), bindingComponent);
    }
    @NonNull
    public static ListrowEntrySimpleBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ListrowEntrySimpleBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/listrow_entry_simple_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new ListrowEntrySimpleBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): entry
        flag 1 (0x2L): null
        flag 2 (0x3L): entry.entryType.equalsIgnoreCase("disconnect") ? v.VISIBLE : v.GONE
        flag 3 (0x4L): entry.entryType.equalsIgnoreCase("disconnect") ? v.VISIBLE : v.GONE
    flag mapping end*/
    //end
}