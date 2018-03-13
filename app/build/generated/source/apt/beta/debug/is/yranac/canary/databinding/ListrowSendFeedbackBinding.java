package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ListrowSendFeedbackBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.how_is_your_experience_layout, 1);
        sViewsWithIds.put(R.id.not_great_btn, 2);
        sViewsWithIds.put(R.id.excellent_btn, 3);
        sViewsWithIds.put(R.id.spread_the_word_layout, 4);
        sViewsWithIds.put(R.id.no_thanks_btn, 5);
        sViewsWithIds.put(R.id.write_a_review, 6);
        sViewsWithIds.put(R.id.give_us_feedback_layout, 7);
        sViewsWithIds.put(R.id.no_thanks_feedback_btn, 8);
        sViewsWithIds.put(R.id.yes_sure_btn, 9);
        sViewsWithIds.put(R.id.thanks_for_letting_us_know, 10);
    }
    // views
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus excellentBtn;
    @NonNull
    public final android.widget.LinearLayout giveUsFeedbackLayout;
    @NonNull
    public final android.widget.LinearLayout howIsYourExperienceLayout;
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus noThanksBtn;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus noThanksFeedbackBtn;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus notGreatBtn;
    @NonNull
    public final android.widget.LinearLayout spreadTheWordLayout;
    @NonNull
    public final android.widget.LinearLayout thanksForLettingUsKnow;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus writeAReview;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus yesSureBtn;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ListrowSendFeedbackBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 11, sIncludes, sViewsWithIds);
        this.excellentBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[3];
        this.giveUsFeedbackLayout = (android.widget.LinearLayout) bindings[7];
        this.howIsYourExperienceLayout = (android.widget.LinearLayout) bindings[1];
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.noThanksBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[5];
        this.noThanksFeedbackBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[8];
        this.notGreatBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[2];
        this.spreadTheWordLayout = (android.widget.LinearLayout) bindings[4];
        this.thanksForLettingUsKnow = (android.widget.LinearLayout) bindings[10];
        this.writeAReview = (is.yranac.canary.ui.views.ButtonPlus) bindings[6];
        this.yesSureBtn = (is.yranac.canary.ui.views.ButtonPlus) bindings[9];
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
    public static ListrowSendFeedbackBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ListrowSendFeedbackBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<ListrowSendFeedbackBinding>inflate(inflater, is.yranac.canary.R.layout.listrow_send_feedback, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static ListrowSendFeedbackBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ListrowSendFeedbackBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.listrow_send_feedback, null, false), bindingComponent);
    }
    @NonNull
    public static ListrowSendFeedbackBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ListrowSendFeedbackBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/listrow_send_feedback_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new ListrowSendFeedbackBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}