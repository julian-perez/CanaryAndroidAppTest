package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentSetupGetHelpBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new android.databinding.ViewDataBinding.IncludedLayouts(17);
        sIncludes.setIncludes(0, 
            new String[] {"layout_header"},
            new int[] {15},
            new int[] {R.layout.layout_header});
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.sub_header_text_view, 16);
    }
    // views
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus contactSupportText;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus ctaBtnFive;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus ctaBtnFour;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus ctaBtnOne;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus ctaBtnSix;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus ctaBtnThree;
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus ctaBtnTwo;
    @Nullable
    public final is.yranac.canary.databinding.LayoutHeaderBinding header;
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    @NonNull
    private final android.widget.LinearLayout mboundView1;
    @NonNull
    private final android.widget.LinearLayout mboundView11;
    @NonNull
    private final android.widget.LinearLayout mboundView3;
    @NonNull
    private final android.widget.LinearLayout mboundView5;
    @NonNull
    private final android.widget.LinearLayout mboundView7;
    @NonNull
    private final android.widget.LinearLayout mboundView9;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus subHeaderTextView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus visetHelpCenterText;
    // variables
    @Nullable
    private is.yranac.canary.fragments.setup.GetHelpFragment.MyHandlers mHandlers;
    @Nullable
    private is.yranac.canary.fragments.setup.GetHelpFragment.GetHelpStringTypes mStringTypes;
    // values
    // listeners
    private OnClickListenerImpl mHandlersLinkZendeskAndroidViewViewOnClickListener;
    private OnClickListenerImpl1 mHandlersLinkThreeAndroidViewViewOnClickListener;
    private OnClickListenerImpl2 mHandlersLinkSixAndroidViewViewOnClickListener;
    private OnClickListenerImpl3 mHandlersLinkTwoAndroidViewViewOnClickListener;
    private OnClickListenerImpl4 mHandlersLinkHelpCenterAndroidViewViewOnClickListener;
    private OnClickListenerImpl5 mHandlersLinkFourAndroidViewViewOnClickListener;
    private OnClickListenerImpl6 mHandlersLinkFiveAndroidViewViewOnClickListener;
    private OnClickListenerImpl7 mHandlersLinkOneAndroidViewViewOnClickListener;
    // Inverse Binding Event Handlers

    public FragmentSetupGetHelpBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 1);
        final Object[] bindings = mapBindings(bindingComponent, root, 17, sIncludes, sViewsWithIds);
        this.contactSupportText = (is.yranac.canary.ui.views.TextViewPlus) bindings[14];
        this.contactSupportText.setTag(null);
        this.ctaBtnFive = (is.yranac.canary.ui.views.ButtonPlus) bindings[10];
        this.ctaBtnFive.setTag(null);
        this.ctaBtnFour = (is.yranac.canary.ui.views.ButtonPlus) bindings[8];
        this.ctaBtnFour.setTag(null);
        this.ctaBtnOne = (is.yranac.canary.ui.views.ButtonPlus) bindings[2];
        this.ctaBtnOne.setTag(null);
        this.ctaBtnSix = (is.yranac.canary.ui.views.ButtonPlus) bindings[12];
        this.ctaBtnSix.setTag(null);
        this.ctaBtnThree = (is.yranac.canary.ui.views.ButtonPlus) bindings[6];
        this.ctaBtnThree.setTag(null);
        this.ctaBtnTwo = (is.yranac.canary.ui.views.ButtonPlus) bindings[4];
        this.ctaBtnTwo.setTag(null);
        this.header = (is.yranac.canary.databinding.LayoutHeaderBinding) bindings[15];
        setContainedBinding(this.header);
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView1 = (android.widget.LinearLayout) bindings[1];
        this.mboundView1.setTag(null);
        this.mboundView11 = (android.widget.LinearLayout) bindings[11];
        this.mboundView11.setTag(null);
        this.mboundView3 = (android.widget.LinearLayout) bindings[3];
        this.mboundView3.setTag(null);
        this.mboundView5 = (android.widget.LinearLayout) bindings[5];
        this.mboundView5.setTag(null);
        this.mboundView7 = (android.widget.LinearLayout) bindings[7];
        this.mboundView7.setTag(null);
        this.mboundView9 = (android.widget.LinearLayout) bindings[9];
        this.mboundView9.setTag(null);
        this.subHeaderTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[16];
        this.visetHelpCenterText = (is.yranac.canary.ui.views.TextViewPlus) bindings[13];
        this.visetHelpCenterText.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x8L;
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
        if (BR.handlers == variableId) {
            setHandlers((is.yranac.canary.fragments.setup.GetHelpFragment.MyHandlers) variable);
        }
        else if (BR.stringTypes == variableId) {
            setStringTypes((is.yranac.canary.fragments.setup.GetHelpFragment.GetHelpStringTypes) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setHandlers(@Nullable is.yranac.canary.fragments.setup.GetHelpFragment.MyHandlers Handlers) {
        this.mHandlers = Handlers;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.handlers);
        super.requestRebind();
    }
    @Nullable
    public is.yranac.canary.fragments.setup.GetHelpFragment.MyHandlers getHandlers() {
        return mHandlers;
    }
    public void setStringTypes(@Nullable is.yranac.canary.fragments.setup.GetHelpFragment.GetHelpStringTypes StringTypes) {
        this.mStringTypes = StringTypes;
        synchronized(this) {
            mDirtyFlags |= 0x4L;
        }
        notifyPropertyChanged(BR.stringTypes);
        super.requestRebind();
    }
    @Nullable
    public is.yranac.canary.fragments.setup.GetHelpFragment.GetHelpStringTypes getStringTypes() {
        return mStringTypes;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeHeader((is.yranac.canary.databinding.LayoutHeaderBinding) object, fieldId);
        }
        return false;
    }
    private boolean onChangeHeader(is.yranac.canary.databinding.LayoutHeaderBinding Header, int fieldId) {
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
        int textUtilsIsEmptyStringTypesButtonThreeStringViewGONEViewVISIBLE = 0;
        java.lang.String stringTypesButtonThreeString = null;
        android.view.View.OnClickListener handlersLinkZendeskAndroidViewViewOnClickListener = null;
        android.view.View.OnClickListener handlersLinkThreeAndroidViewViewOnClickListener = null;
        android.view.View.OnClickListener handlersLinkSixAndroidViewViewOnClickListener = null;
        java.lang.String stringTypesButtonOneString = null;
        android.view.View.OnClickListener handlersLinkTwoAndroidViewViewOnClickListener = null;
        boolean textUtilsIsEmptyStringTypesButtonThreeString = false;
        android.view.View.OnClickListener handlersLinkHelpCenterAndroidViewViewOnClickListener = null;
        int textUtilsIsEmptyStringTypesButtonSixStringViewGONEViewVISIBLE = 0;
        java.lang.String stringTypesButtonFiveString = null;
        java.lang.String stringTypesButtonFourString = null;
        android.view.View.OnClickListener handlersLinkFourAndroidViewViewOnClickListener = null;
        int textUtilsIsEmptyStringTypesButtonTwoStringViewGONEViewVISIBLE = 0;
        int textUtilsIsEmptyStringTypesButtonFourStringViewGONEViewVISIBLE = 0;
        boolean textUtilsIsEmptyStringTypesButtonFiveString = false;
        java.lang.String stringTypesButtonSixString = null;
        android.view.View.OnClickListener handlersLinkFiveAndroidViewViewOnClickListener = null;
        java.lang.String stringTypesButtonTwoString = null;
        int textUtilsIsEmptyStringTypesButtonFiveStringViewGONEViewVISIBLE = 0;
        is.yranac.canary.fragments.setup.GetHelpFragment.MyHandlers handlers = mHandlers;
        android.view.View.OnClickListener handlersLinkOneAndroidViewViewOnClickListener = null;
        boolean textUtilsIsEmptyStringTypesButtonOneString = false;
        boolean textUtilsIsEmptyStringTypesButtonFourString = false;
        boolean textUtilsIsEmptyStringTypesButtonTwoString = false;
        boolean textUtilsIsEmptyStringTypesButtonSixString = false;
        int textUtilsIsEmptyStringTypesButtonOneStringViewGONEViewVISIBLE = 0;
        is.yranac.canary.fragments.setup.GetHelpFragment.GetHelpStringTypes stringTypes = mStringTypes;

        if ((dirtyFlags & 0xaL) != 0) {



                if (handlers != null) {
                    // read handlers::linkZendesk
                    handlersLinkZendeskAndroidViewViewOnClickListener = (((mHandlersLinkZendeskAndroidViewViewOnClickListener == null) ? (mHandlersLinkZendeskAndroidViewViewOnClickListener = new OnClickListenerImpl()) : mHandlersLinkZendeskAndroidViewViewOnClickListener).setValue(handlers));
                    // read handlers::linkThree
                    handlersLinkThreeAndroidViewViewOnClickListener = (((mHandlersLinkThreeAndroidViewViewOnClickListener == null) ? (mHandlersLinkThreeAndroidViewViewOnClickListener = new OnClickListenerImpl1()) : mHandlersLinkThreeAndroidViewViewOnClickListener).setValue(handlers));
                    // read handlers::linkSix
                    handlersLinkSixAndroidViewViewOnClickListener = (((mHandlersLinkSixAndroidViewViewOnClickListener == null) ? (mHandlersLinkSixAndroidViewViewOnClickListener = new OnClickListenerImpl2()) : mHandlersLinkSixAndroidViewViewOnClickListener).setValue(handlers));
                    // read handlers::linkTwo
                    handlersLinkTwoAndroidViewViewOnClickListener = (((mHandlersLinkTwoAndroidViewViewOnClickListener == null) ? (mHandlersLinkTwoAndroidViewViewOnClickListener = new OnClickListenerImpl3()) : mHandlersLinkTwoAndroidViewViewOnClickListener).setValue(handlers));
                    // read handlers::linkHelpCenter
                    handlersLinkHelpCenterAndroidViewViewOnClickListener = (((mHandlersLinkHelpCenterAndroidViewViewOnClickListener == null) ? (mHandlersLinkHelpCenterAndroidViewViewOnClickListener = new OnClickListenerImpl4()) : mHandlersLinkHelpCenterAndroidViewViewOnClickListener).setValue(handlers));
                    // read handlers::linkFour
                    handlersLinkFourAndroidViewViewOnClickListener = (((mHandlersLinkFourAndroidViewViewOnClickListener == null) ? (mHandlersLinkFourAndroidViewViewOnClickListener = new OnClickListenerImpl5()) : mHandlersLinkFourAndroidViewViewOnClickListener).setValue(handlers));
                    // read handlers::linkFive
                    handlersLinkFiveAndroidViewViewOnClickListener = (((mHandlersLinkFiveAndroidViewViewOnClickListener == null) ? (mHandlersLinkFiveAndroidViewViewOnClickListener = new OnClickListenerImpl6()) : mHandlersLinkFiveAndroidViewViewOnClickListener).setValue(handlers));
                    // read handlers::linkOne
                    handlersLinkOneAndroidViewViewOnClickListener = (((mHandlersLinkOneAndroidViewViewOnClickListener == null) ? (mHandlersLinkOneAndroidViewViewOnClickListener = new OnClickListenerImpl7()) : mHandlersLinkOneAndroidViewViewOnClickListener).setValue(handlers));
                }
        }
        if ((dirtyFlags & 0xcL) != 0) {



                if (stringTypes != null) {
                    // read stringTypes.buttonThreeString
                    stringTypesButtonThreeString = stringTypes.buttonThreeString;
                    // read stringTypes.buttonOneString
                    stringTypesButtonOneString = stringTypes.buttonOneString;
                    // read stringTypes.buttonFiveString
                    stringTypesButtonFiveString = stringTypes.buttonFiveString;
                    // read stringTypes.buttonFourString
                    stringTypesButtonFourString = stringTypes.buttonFourString;
                    // read stringTypes.buttonSixString
                    stringTypesButtonSixString = stringTypes.buttonSixString;
                    // read stringTypes.buttonTwoString
                    stringTypesButtonTwoString = stringTypes.buttonTwoString;
                }


                // read TextUtils.isEmpty(stringTypes.buttonThreeString)
                textUtilsIsEmptyStringTypesButtonThreeString = android.text.TextUtils.isEmpty(stringTypesButtonThreeString);
                // read TextUtils.isEmpty(stringTypes.buttonOneString)
                textUtilsIsEmptyStringTypesButtonOneString = android.text.TextUtils.isEmpty(stringTypesButtonOneString);
                // read TextUtils.isEmpty(stringTypes.buttonFiveString)
                textUtilsIsEmptyStringTypesButtonFiveString = android.text.TextUtils.isEmpty(stringTypesButtonFiveString);
                // read TextUtils.isEmpty(stringTypes.buttonFourString)
                textUtilsIsEmptyStringTypesButtonFourString = android.text.TextUtils.isEmpty(stringTypesButtonFourString);
                // read TextUtils.isEmpty(stringTypes.buttonSixString)
                textUtilsIsEmptyStringTypesButtonSixString = android.text.TextUtils.isEmpty(stringTypesButtonSixString);
                // read TextUtils.isEmpty(stringTypes.buttonTwoString)
                textUtilsIsEmptyStringTypesButtonTwoString = android.text.TextUtils.isEmpty(stringTypesButtonTwoString);
            if((dirtyFlags & 0xcL) != 0) {
                if(textUtilsIsEmptyStringTypesButtonThreeString) {
                        dirtyFlags |= 0x20L;
                }
                else {
                        dirtyFlags |= 0x10L;
                }
            }
            if((dirtyFlags & 0xcL) != 0) {
                if(textUtilsIsEmptyStringTypesButtonOneString) {
                        dirtyFlags |= 0x8000L;
                }
                else {
                        dirtyFlags |= 0x4000L;
                }
            }
            if((dirtyFlags & 0xcL) != 0) {
                if(textUtilsIsEmptyStringTypesButtonFiveString) {
                        dirtyFlags |= 0x2000L;
                }
                else {
                        dirtyFlags |= 0x1000L;
                }
            }
            if((dirtyFlags & 0xcL) != 0) {
                if(textUtilsIsEmptyStringTypesButtonFourString) {
                        dirtyFlags |= 0x800L;
                }
                else {
                        dirtyFlags |= 0x400L;
                }
            }
            if((dirtyFlags & 0xcL) != 0) {
                if(textUtilsIsEmptyStringTypesButtonSixString) {
                        dirtyFlags |= 0x80L;
                }
                else {
                        dirtyFlags |= 0x40L;
                }
            }
            if((dirtyFlags & 0xcL) != 0) {
                if(textUtilsIsEmptyStringTypesButtonTwoString) {
                        dirtyFlags |= 0x200L;
                }
                else {
                        dirtyFlags |= 0x100L;
                }
            }


                // read TextUtils.isEmpty(stringTypes.buttonThreeString) ? View.GONE : View.VISIBLE
                textUtilsIsEmptyStringTypesButtonThreeStringViewGONEViewVISIBLE = ((textUtilsIsEmptyStringTypesButtonThreeString) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
                // read TextUtils.isEmpty(stringTypes.buttonOneString) ? View.GONE : View.VISIBLE
                textUtilsIsEmptyStringTypesButtonOneStringViewGONEViewVISIBLE = ((textUtilsIsEmptyStringTypesButtonOneString) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
                // read TextUtils.isEmpty(stringTypes.buttonFiveString) ? View.GONE : View.VISIBLE
                textUtilsIsEmptyStringTypesButtonFiveStringViewGONEViewVISIBLE = ((textUtilsIsEmptyStringTypesButtonFiveString) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
                // read TextUtils.isEmpty(stringTypes.buttonFourString) ? View.GONE : View.VISIBLE
                textUtilsIsEmptyStringTypesButtonFourStringViewGONEViewVISIBLE = ((textUtilsIsEmptyStringTypesButtonFourString) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
                // read TextUtils.isEmpty(stringTypes.buttonSixString) ? View.GONE : View.VISIBLE
                textUtilsIsEmptyStringTypesButtonSixStringViewGONEViewVISIBLE = ((textUtilsIsEmptyStringTypesButtonSixString) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
                // read TextUtils.isEmpty(stringTypes.buttonTwoString) ? View.GONE : View.VISIBLE
                textUtilsIsEmptyStringTypesButtonTwoStringViewGONEViewVISIBLE = ((textUtilsIsEmptyStringTypesButtonTwoString) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
        }
        // batch finished
        if ((dirtyFlags & 0xaL) != 0) {
            // api target 1

            this.contactSupportText.setOnClickListener(handlersLinkZendeskAndroidViewViewOnClickListener);
            this.ctaBtnFive.setOnClickListener(handlersLinkFiveAndroidViewViewOnClickListener);
            this.ctaBtnFour.setOnClickListener(handlersLinkFourAndroidViewViewOnClickListener);
            this.ctaBtnOne.setOnClickListener(handlersLinkOneAndroidViewViewOnClickListener);
            this.ctaBtnSix.setOnClickListener(handlersLinkSixAndroidViewViewOnClickListener);
            this.ctaBtnThree.setOnClickListener(handlersLinkThreeAndroidViewViewOnClickListener);
            this.ctaBtnTwo.setOnClickListener(handlersLinkTwoAndroidViewViewOnClickListener);
            this.visetHelpCenterText.setOnClickListener(handlersLinkHelpCenterAndroidViewViewOnClickListener);
        }
        if ((dirtyFlags & 0xcL) != 0) {
            // api target 1

            android.databinding.adapters.TextViewBindingAdapter.setText(this.ctaBtnFive, stringTypesButtonFiveString);
            android.databinding.adapters.TextViewBindingAdapter.setText(this.ctaBtnFour, stringTypesButtonFourString);
            android.databinding.adapters.TextViewBindingAdapter.setText(this.ctaBtnOne, stringTypesButtonOneString);
            android.databinding.adapters.TextViewBindingAdapter.setText(this.ctaBtnSix, stringTypesButtonSixString);
            android.databinding.adapters.TextViewBindingAdapter.setText(this.ctaBtnThree, stringTypesButtonThreeString);
            android.databinding.adapters.TextViewBindingAdapter.setText(this.ctaBtnTwo, stringTypesButtonTwoString);
            this.mboundView1.setVisibility(textUtilsIsEmptyStringTypesButtonOneStringViewGONEViewVISIBLE);
            this.mboundView11.setVisibility(textUtilsIsEmptyStringTypesButtonSixStringViewGONEViewVISIBLE);
            this.mboundView3.setVisibility(textUtilsIsEmptyStringTypesButtonTwoStringViewGONEViewVISIBLE);
            this.mboundView5.setVisibility(textUtilsIsEmptyStringTypesButtonThreeStringViewGONEViewVISIBLE);
            this.mboundView7.setVisibility(textUtilsIsEmptyStringTypesButtonFourStringViewGONEViewVISIBLE);
            this.mboundView9.setVisibility(textUtilsIsEmptyStringTypesButtonFiveStringViewGONEViewVISIBLE);
        }
        executeBindingsOn(header);
    }
    // Listener Stub Implementations
    public static class OnClickListenerImpl implements android.view.View.OnClickListener{
        private is.yranac.canary.fragments.setup.GetHelpFragment.MyHandlers value;
        public OnClickListenerImpl setValue(is.yranac.canary.fragments.setup.GetHelpFragment.MyHandlers value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.linkZendesk(arg0);
        }
    }
    public static class OnClickListenerImpl1 implements android.view.View.OnClickListener{
        private is.yranac.canary.fragments.setup.GetHelpFragment.MyHandlers value;
        public OnClickListenerImpl1 setValue(is.yranac.canary.fragments.setup.GetHelpFragment.MyHandlers value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.linkThree(arg0);
        }
    }
    public static class OnClickListenerImpl2 implements android.view.View.OnClickListener{
        private is.yranac.canary.fragments.setup.GetHelpFragment.MyHandlers value;
        public OnClickListenerImpl2 setValue(is.yranac.canary.fragments.setup.GetHelpFragment.MyHandlers value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.linkSix(arg0);
        }
    }
    public static class OnClickListenerImpl3 implements android.view.View.OnClickListener{
        private is.yranac.canary.fragments.setup.GetHelpFragment.MyHandlers value;
        public OnClickListenerImpl3 setValue(is.yranac.canary.fragments.setup.GetHelpFragment.MyHandlers value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.linkTwo(arg0);
        }
    }
    public static class OnClickListenerImpl4 implements android.view.View.OnClickListener{
        private is.yranac.canary.fragments.setup.GetHelpFragment.MyHandlers value;
        public OnClickListenerImpl4 setValue(is.yranac.canary.fragments.setup.GetHelpFragment.MyHandlers value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.linkHelpCenter(arg0);
        }
    }
    public static class OnClickListenerImpl5 implements android.view.View.OnClickListener{
        private is.yranac.canary.fragments.setup.GetHelpFragment.MyHandlers value;
        public OnClickListenerImpl5 setValue(is.yranac.canary.fragments.setup.GetHelpFragment.MyHandlers value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.linkFour(arg0);
        }
    }
    public static class OnClickListenerImpl6 implements android.view.View.OnClickListener{
        private is.yranac.canary.fragments.setup.GetHelpFragment.MyHandlers value;
        public OnClickListenerImpl6 setValue(is.yranac.canary.fragments.setup.GetHelpFragment.MyHandlers value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.linkFive(arg0);
        }
    }
    public static class OnClickListenerImpl7 implements android.view.View.OnClickListener{
        private is.yranac.canary.fragments.setup.GetHelpFragment.MyHandlers value;
        public OnClickListenerImpl7 setValue(is.yranac.canary.fragments.setup.GetHelpFragment.MyHandlers value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.linkOne(arg0);
        }
    }
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static FragmentSetupGetHelpBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSetupGetHelpBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentSetupGetHelpBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_setup_get_help, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentSetupGetHelpBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSetupGetHelpBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_setup_get_help, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentSetupGetHelpBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSetupGetHelpBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_setup_get_help_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentSetupGetHelpBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): header
        flag 1 (0x2L): handlers
        flag 2 (0x3L): stringTypes
        flag 3 (0x4L): null
        flag 4 (0x5L): TextUtils.isEmpty(stringTypes.buttonThreeString) ? View.GONE : View.VISIBLE
        flag 5 (0x6L): TextUtils.isEmpty(stringTypes.buttonThreeString) ? View.GONE : View.VISIBLE
        flag 6 (0x7L): TextUtils.isEmpty(stringTypes.buttonSixString) ? View.GONE : View.VISIBLE
        flag 7 (0x8L): TextUtils.isEmpty(stringTypes.buttonSixString) ? View.GONE : View.VISIBLE
        flag 8 (0x9L): TextUtils.isEmpty(stringTypes.buttonTwoString) ? View.GONE : View.VISIBLE
        flag 9 (0xaL): TextUtils.isEmpty(stringTypes.buttonTwoString) ? View.GONE : View.VISIBLE
        flag 10 (0xbL): TextUtils.isEmpty(stringTypes.buttonFourString) ? View.GONE : View.VISIBLE
        flag 11 (0xcL): TextUtils.isEmpty(stringTypes.buttonFourString) ? View.GONE : View.VISIBLE
        flag 12 (0xdL): TextUtils.isEmpty(stringTypes.buttonFiveString) ? View.GONE : View.VISIBLE
        flag 13 (0xeL): TextUtils.isEmpty(stringTypes.buttonFiveString) ? View.GONE : View.VISIBLE
        flag 14 (0xfL): TextUtils.isEmpty(stringTypes.buttonOneString) ? View.GONE : View.VISIBLE
        flag 15 (0x10L): TextUtils.isEmpty(stringTypes.buttonOneString) ? View.GONE : View.VISIBLE
    flag mapping end*/
    //end
}