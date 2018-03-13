package is.yranac.canary.databinding;
import is.yranac.canary.R;
import is.yranac.canary.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentSettingsMembershipBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.page_1, 11);
        sViewsWithIds.put(R.id.page_1_image, 12);
        sViewsWithIds.put(R.id.page_1_text, 13);
        sViewsWithIds.put(R.id.page_2, 14);
        sViewsWithIds.put(R.id.page_2_image, 15);
        sViewsWithIds.put(R.id.page_2_text, 16);
        sViewsWithIds.put(R.id.page_3, 17);
        sViewsWithIds.put(R.id.page_3_image, 18);
        sViewsWithIds.put(R.id.page_3_text, 19);
        sViewsWithIds.put(R.id.page_4, 20);
        sViewsWithIds.put(R.id.page_4_image, 21);
        sViewsWithIds.put(R.id.page_4_text, 22);
        sViewsWithIds.put(R.id.page_6, 23);
        sViewsWithIds.put(R.id.page_6_image, 24);
        sViewsWithIds.put(R.id.page_6_text, 25);
        sViewsWithIds.put(R.id.page_7, 26);
        sViewsWithIds.put(R.id.page_7_image, 27);
        sViewsWithIds.put(R.id.page_7_text, 28);
        sViewsWithIds.put(R.id.extended_warranty_dsc_text_view, 29);
        sViewsWithIds.put(R.id.bottom_arrow_up, 30);
    }
    // views
    @NonNull
    public final is.yranac.canary.ui.views.ButtonPlus addMemberButton;
    @NonNull
    public final android.widget.LinearLayout backToTopLayout;
    @NonNull
    public final android.support.v7.widget.AppCompatImageView bottomArrow;
    @NonNull
    public final android.support.v7.widget.AppCompatImageView bottomArrowUp;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus extendedWarrantyDscTextView;
    @NonNull
    public final is.yranac.canary.ui.views.TextViewPlus extendedWarrantyTextView;
    @NonNull
    public final android.widget.LinearLayout incidentSupportBtn;
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    @NonNull
    private final android.widget.RelativeLayout mboundView4;
    @NonNull
    private final is.yranac.canary.ui.views.TextViewPlus mboundView6;
    @NonNull
    private final android.view.View mboundView7;
    @NonNull
    public final android.widget.RelativeLayout page1;
    @NonNull
    public final android.support.v7.widget.AppCompatImageView page1Image;
    @NonNull
    public final android.widget.LinearLayout page1Text;
    @NonNull
    public final android.widget.RelativeLayout page2;
    @NonNull
    public final android.widget.ImageView page2Image;
    @NonNull
    public final android.widget.LinearLayout page2Text;
    @NonNull
    public final android.widget.RelativeLayout page3;
    @NonNull
    public final android.widget.ImageView page3Image;
    @NonNull
    public final android.widget.LinearLayout page3Text;
    @NonNull
    public final android.widget.RelativeLayout page4;
    @NonNull
    public final android.widget.ImageView page4Image;
    @NonNull
    public final android.widget.LinearLayout page4Text;
    @NonNull
    public final android.widget.RelativeLayout page6;
    @NonNull
    public final android.widget.ImageView page6Image;
    @NonNull
    public final android.widget.LinearLayout page6Text;
    @NonNull
    public final android.widget.RelativeLayout page7;
    @NonNull
    public final android.widget.ImageView page7Image;
    @NonNull
    public final android.widget.LinearLayout page7Text;
    @NonNull
    public final android.widget.RelativeLayout scrollTransition;
    @NonNull
    public final is.yranac.canary.ui.views.VerticalViewPager viewPager;
    // variables
    @Nullable
    private is.yranac.canary.model.subscription.Subscription mSubscription;
    @Nullable
    private android.view.View mV;
    @Nullable
    private is.yranac.canary.model.location.Location mLocation;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentSettingsMembershipBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 31, sIncludes, sViewsWithIds);
        this.addMemberButton = (is.yranac.canary.ui.views.ButtonPlus) bindings[3];
        this.addMemberButton.setTag(null);
        this.backToTopLayout = (android.widget.LinearLayout) bindings[10];
        this.backToTopLayout.setTag(null);
        this.bottomArrow = (android.support.v7.widget.AppCompatImageView) bindings[9];
        this.bottomArrow.setTag(null);
        this.bottomArrowUp = (android.support.v7.widget.AppCompatImageView) bindings[30];
        this.extendedWarrantyDscTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[29];
        this.extendedWarrantyTextView = (is.yranac.canary.ui.views.TextViewPlus) bindings[5];
        this.extendedWarrantyTextView.setTag(null);
        this.incidentSupportBtn = (android.widget.LinearLayout) bindings[8];
        this.incidentSupportBtn.setTag(null);
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView4 = (android.widget.RelativeLayout) bindings[4];
        this.mboundView4.setTag(null);
        this.mboundView6 = (is.yranac.canary.ui.views.TextViewPlus) bindings[6];
        this.mboundView6.setTag(null);
        this.mboundView7 = (android.view.View) bindings[7];
        this.mboundView7.setTag(null);
        this.page1 = (android.widget.RelativeLayout) bindings[11];
        this.page1Image = (android.support.v7.widget.AppCompatImageView) bindings[12];
        this.page1Text = (android.widget.LinearLayout) bindings[13];
        this.page2 = (android.widget.RelativeLayout) bindings[14];
        this.page2Image = (android.widget.ImageView) bindings[15];
        this.page2Text = (android.widget.LinearLayout) bindings[16];
        this.page3 = (android.widget.RelativeLayout) bindings[17];
        this.page3Image = (android.widget.ImageView) bindings[18];
        this.page3Text = (android.widget.LinearLayout) bindings[19];
        this.page4 = (android.widget.RelativeLayout) bindings[20];
        this.page4Image = (android.widget.ImageView) bindings[21];
        this.page4Text = (android.widget.LinearLayout) bindings[22];
        this.page6 = (android.widget.RelativeLayout) bindings[23];
        this.page6Image = (android.widget.ImageView) bindings[24];
        this.page6Text = (android.widget.LinearLayout) bindings[25];
        this.page7 = (android.widget.RelativeLayout) bindings[26];
        this.page7Image = (android.widget.ImageView) bindings[27];
        this.page7Text = (android.widget.LinearLayout) bindings[28];
        this.scrollTransition = (android.widget.RelativeLayout) bindings[2];
        this.scrollTransition.setTag(null);
        this.viewPager = (is.yranac.canary.ui.views.VerticalViewPager) bindings[1];
        this.viewPager.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x8L;
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
        if (BR.subscription == variableId) {
            setSubscription((is.yranac.canary.model.subscription.Subscription) variable);
        }
        else if (BR.v == variableId) {
            setV((android.view.View) variable);
        }
        else if (BR.location == variableId) {
            setLocation((is.yranac.canary.model.location.Location) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setSubscription(@Nullable is.yranac.canary.model.subscription.Subscription Subscription) {
        this.mSubscription = Subscription;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.subscription);
        super.requestRebind();
    }
    @Nullable
    public is.yranac.canary.model.subscription.Subscription getSubscription() {
        return mSubscription;
    }
    public void setV(@Nullable android.view.View V) {
        this.mV = V;
    }
    @Nullable
    public android.view.View getV() {
        return mV;
    }
    public void setLocation(@Nullable is.yranac.canary.model.location.Location Location) {
        this.mLocation = Location;
        synchronized(this) {
            mDirtyFlags |= 0x4L;
        }
        notifyPropertyChanged(BR.location);
        super.requestRebind();
    }
    @Nullable
    public is.yranac.canary.model.location.Location getLocation() {
        return mLocation;
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
        int subscriptionHasMembershipVGONEVVISIBLE = 0;
        boolean locationUnitedStatesSubscriptionHasMembershipBooleanFalse = false;
        is.yranac.canary.model.subscription.Subscription subscription = mSubscription;
        int locationUnitedStatesVVISIBLEVGONE = 0;
        int locationUnitedStatesSubscriptionHasMembershipBooleanFalseVVISIBLEVGONE = 0;
        boolean subscriptionHasMembership = false;
        int subscriptionHasMembershipVVISIBLEVGONE = 0;
        is.yranac.canary.model.location.Location location = mLocation;
        boolean locationUnitedStates = false;

        if ((dirtyFlags & 0x9L) != 0) {



                if (subscription != null) {
                    // read subscription.hasMembership
                    subscriptionHasMembership = subscription.hasMembership;
                }
            if((dirtyFlags & 0x9L) != 0) {
                if(subscriptionHasMembership) {
                        dirtyFlags |= 0x20L;
                        dirtyFlags |= 0x2000L;
                }
                else {
                        dirtyFlags |= 0x10L;
                        dirtyFlags |= 0x1000L;
                }
            }


                // read subscription.hasMembership ? View.GONE : View.VISIBLE
                subscriptionHasMembershipVGONEVVISIBLE = ((subscriptionHasMembership) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
                // read subscription.hasMembership ? View.VISIBLE : View.GONE
                subscriptionHasMembershipVVISIBLEVGONE = ((subscriptionHasMembership) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        if ((dirtyFlags & 0xdL) != 0) {



                if (location != null) {
                    // read location.unitedStates
                    locationUnitedStates = location.isUnitedStates();
                }
            if((dirtyFlags & 0xdL) != 0) {
                if(locationUnitedStates) {
                        dirtyFlags |= 0x80L;
                }
                else {
                        dirtyFlags |= 0x40L;
                }
            }
            if((dirtyFlags & 0xcL) != 0) {
                if(locationUnitedStates) {
                        dirtyFlags |= 0x200L;
                }
                else {
                        dirtyFlags |= 0x100L;
                }
            }

            if ((dirtyFlags & 0xcL) != 0) {

                    // read location.unitedStates ? View.VISIBLE : View.GONE
                    locationUnitedStatesVVISIBLEVGONE = ((locationUnitedStates) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
            }
        }
        // batch finished

        if ((dirtyFlags & 0x80L) != 0) {



                if (subscription != null) {
                    // read subscription.hasMembership
                    subscriptionHasMembership = subscription.hasMembership;
                }
            if((dirtyFlags & 0x9L) != 0) {
                if(subscriptionHasMembership) {
                        dirtyFlags |= 0x20L;
                        dirtyFlags |= 0x2000L;
                }
                else {
                        dirtyFlags |= 0x10L;
                        dirtyFlags |= 0x1000L;
                }
            }
        }

        if ((dirtyFlags & 0xdL) != 0) {

                // read location.unitedStates ? subscription.hasMembership : false
                locationUnitedStatesSubscriptionHasMembershipBooleanFalse = ((locationUnitedStates) ? (subscriptionHasMembership) : (false));
            if((dirtyFlags & 0xdL) != 0) {
                if(locationUnitedStatesSubscriptionHasMembershipBooleanFalse) {
                        dirtyFlags |= 0x800L;
                }
                else {
                        dirtyFlags |= 0x400L;
                }
            }


                // read location.unitedStates ? subscription.hasMembership : false ? View.VISIBLE : View.GONE
                locationUnitedStatesSubscriptionHasMembershipBooleanFalseVVISIBLEVGONE = ((locationUnitedStatesSubscriptionHasMembershipBooleanFalse) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        // batch finished
        if ((dirtyFlags & 0x9L) != 0) {
            // api target 1

            this.addMemberButton.setVisibility(subscriptionHasMembershipVGONEVVISIBLE);
            this.backToTopLayout.setVisibility(subscriptionHasMembershipVGONEVVISIBLE);
            this.bottomArrow.setVisibility(subscriptionHasMembershipVGONEVVISIBLE);
            this.mboundView4.setVisibility(subscriptionHasMembershipVVISIBLEVGONE);
            this.mboundView7.setVisibility(subscriptionHasMembershipVVISIBLEVGONE);
            this.scrollTransition.setVisibility(subscriptionHasMembershipVGONEVVISIBLE);
            this.viewPager.setVisibility(subscriptionHasMembershipVGONEVVISIBLE);
        }
        if ((dirtyFlags & 0xcL) != 0) {
            // api target 1

            this.extendedWarrantyTextView.setVisibility(locationUnitedStatesVVISIBLEVGONE);
            this.mboundView6.setVisibility(locationUnitedStatesVVISIBLEVGONE);
        }
        if ((dirtyFlags & 0xdL) != 0) {
            // api target 1

            this.incidentSupportBtn.setVisibility(locationUnitedStatesSubscriptionHasMembershipBooleanFalseVVISIBLEVGONE);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static FragmentSettingsMembershipBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsMembershipBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentSettingsMembershipBinding>inflate(inflater, is.yranac.canary.R.layout.fragment_settings_membership, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FragmentSettingsMembershipBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsMembershipBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(is.yranac.canary.R.layout.fragment_settings_membership, null, false), bindingComponent);
    }
    @NonNull
    public static FragmentSettingsMembershipBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FragmentSettingsMembershipBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_settings_membership_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentSettingsMembershipBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): subscription
        flag 1 (0x2L): v
        flag 2 (0x3L): location
        flag 3 (0x4L): null
        flag 4 (0x5L): subscription.hasMembership ? View.GONE : View.VISIBLE
        flag 5 (0x6L): subscription.hasMembership ? View.GONE : View.VISIBLE
        flag 6 (0x7L): location.unitedStates ? subscription.hasMembership : false
        flag 7 (0x8L): location.unitedStates ? subscription.hasMembership : false
        flag 8 (0x9L): location.unitedStates ? View.VISIBLE : View.GONE
        flag 9 (0xaL): location.unitedStates ? View.VISIBLE : View.GONE
        flag 10 (0xbL): location.unitedStates ? subscription.hasMembership : false ? View.VISIBLE : View.GONE
        flag 11 (0xcL): location.unitedStates ? subscription.hasMembership : false ? View.VISIBLE : View.GONE
        flag 12 (0xdL): subscription.hasMembership ? View.VISIBLE : View.GONE
        flag 13 (0xeL): subscription.hasMembership ? View.VISIBLE : View.GONE
    flag mapping end*/
    //end
}