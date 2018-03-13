package is.yranac.canary.fragments.settings;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.adapter.MembershipSlideShowAdapter;
import is.yranac.canary.databinding.FragmentSettingsMembershipBinding;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.subscription.Subscription;
import is.yranac.canary.text.style.CustomTypefaceSpan;
import is.yranac.canary.ui.WebActivity;
import is.yranac.canary.util.AnimationHelper;
import is.yranac.canary.util.DensityUtil;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;

import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_ADD_MEMBERSHIP;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_CONTACT_INCIDENT_SUPPORT;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_MEMBERSHIP;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_MEMBERSHIP_SETTINGS;
import static is.yranac.canary.util.ga.AnalyticsConstants.READ_MEMBERSHIP_DETAILS;
import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_MEMBERSHIP_SETTINGS;


public class MembershipFragment extends SettingsFragment implements ViewPager.OnPageChangeListener,
        View.OnClickListener {

    private static final String LOG_TAG = "MembershipFragment";
    private static final int pulseDuration = 400;

    private float initButtonX = 0;
    private float finaButtonX;


    private boolean trackedReading = false;

    private Location location;

    private FragmentSettingsMembershipBinding binding;
    private FirstPageChangeListener firstPageChangeListener;
    private Subscription subscription;
    private int currentItem;

    private int topPadding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsMembershipBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.addMemberButton.setOnClickListener(this);
        binding.incidentSupportBtn.setOnClickListener(this);
        binding.backToTopLayout.setOnClickListener(this);

        binding.viewPager.setOnPageChangeListener(this);

        float dp5 = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        AnimationHelper.startPulsing(binding.bottomArrow, false, dp5, pulseDuration);
        AnimationHelper.startPulsing(binding.bottomArrowUp, false, -dp5, pulseDuration);
        generateClickableSpannableView();

        topPadding = DensityUtil.dip2px(getContext(), 115);

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                finaButtonX = -binding.addMemberButton.getHeight() / 2 + topPadding;

                if (initButtonX == 0.0) {
                    initButtonX = binding.addMemberButton.getY();
                }

                fixAlignment(binding.page1Text, binding.page1Image);
                fixAlignment(binding.page2Text, binding.page2Image);
                fixAlignment(binding.page3Text, binding.page3Image);
                fixAlignment(binding.page4Text, binding.page4Image);
                fixAlignment(binding.page6Text, binding.page6Image);
                fixAlignment(binding.page7Text, binding.page7Image);

                if (binding.viewPager.getCurrentItem() == 0) {
                    binding.addMemberButton.setY(initButtonX);
                } else {
                    binding.addMemberButton.setY(finaButtonX);
                }

            }
        });

        setupLocationAndSubscription(location, subscription);
    }

    private void fixAlignment(View textLayout, ImageView imageView) {
        final int thirty = (int) getResources().getDimension(R.dimen.large_margin);

        if (imageView.getTop() - textLayout.getBottom() < thirty) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
            params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.addRule(RelativeLayout.BELOW, textLayout.getId());
            imageView.setLayoutParams(params);


            int viewWidth = imageView.getMeasuredWidth();
            int viewHeight = imageView.getMeasuredHeight();
            Drawable src = imageView.getDrawable();
            if (src != null) {
                int srcWidth = src.getIntrinsicWidth();
                int srcHeight = src.getIntrinsicHeight();
                float wRatio = (float) viewWidth / srcWidth;
                float hRatio = (float) viewHeight / srcHeight;
                float scaleFactor = Math.max(wRatio, hRatio);
                float hTranslate = (viewWidth - scaleFactor * srcWidth) / 2;
                Matrix matrix = new Matrix();
                matrix.setScale(scaleFactor, scaleFactor);
                matrix.postTranslate(hTranslate, 0);
                imageView.setScaleType(ImageView.ScaleType.MATRIX);
                imageView.setImageMatrix(matrix);
            }
        }

    }

    private void generateClickableSpannableView() {
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Uri path = Uri.parse(Constants.CANARY_RETURNS_AND_WARRANTY());
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, path);

                startActivity(browserIntent);

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ds.linkColor);    // you can use custom color
                ds.setUnderlineText(false);    // this remove the underline
            }
        };

        SpannableStringBuilder builder3 = getClickableString(getString(R.string.extended_warranty_dsc), getString(R.string.deductible_learn_more), clickableSpan);

        binding.extendedWarrantyDscTextView.setMovementMethod(LinkMovementMethod.getInstance());
        binding.extendedWarrantyDscTextView.setHighlightColor(Color.TRANSPARENT);
        binding.extendedWarrantyDscTextView.setText(builder3);


        SpannableStringBuilder builder4 = getClickableString(getString(R.string.two_year_warrenty), getString(R.string.deductible_learn_more), clickableSpan);

        binding.extendedWarrantyTextView.setMovementMethod(LinkMovementMethod.getInstance());
        binding.extendedWarrantyTextView.setHighlightColor(Color.TRANSPARENT);
        binding.extendedWarrantyTextView.setText(builder4);

    }


    private SpannableStringBuilder getClickableString(String firstString, String secondString, ClickableSpan clickableSpan) {
        SpannableString ss = new SpannableString(firstString);


        SpannableString ss2 = new SpannableString(secondString);


        Typeface font = Typeface.createFromAsset(getResources().getAssets(), getString(R.string.gibson_light));
        Typeface font2 = Typeface.createFromAsset(getResources().getAssets(), getString(R.string.gibson_regular));


        ss2.setSpan(clickableSpan, 0, secondString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        float size = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics());

        ss.setSpan(new CustomTypefaceSpan("", font, size), 0, firstString.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        ss2.setSpan(new CustomTypefaceSpan("", font2, size), 0, secondString.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);


        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(ss);
        builder.append(ss2);

        return builder;
    }

    private SpannableStringBuilder getClickableString(String text) {
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Uri path = Uri.parse(Constants.CANARY_MEMBERSHIP_HELP());
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, path);

                startActivity(browserIntent);

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ds.linkColor);    // you can use custom color
                ds.setUnderlineText(false);    // this remove the underline
            }
        };
        return getClickableString(text, getString(R.string.deductible_learn_more), clickableSpan);
    }


    public void setupLocationAndSubscription(final Location location, final Subscription subscription) {

        this.location = location;
        this.subscription = subscription;
        if (binding == null || location == null || subscription == null) {
            return;
        }


        binding.setSubscription(subscription);
        binding.setLocation(location);

        if (subscription.hasMembership) {
            if (!Utils.canDial(getContext())) {
                binding.incidentSupportBtn.setVisibility(View.GONE);
            }
        }

        if (!subscription.hasMembership) {
            binding.viewPager.setAdapter(new MembershipSlideShowAdapter(getContext(), location.isUnitedStates()));

            binding.viewPager.setCurrentItem(currentItem);
            for (int i = 0; i <= currentItem; i++) {
                onPageScrolled(i, 0, 0);
            }

        } else {
            binding.bottomArrow.clearAnimation();
        }

    }

    @Override
    public void onRightButtonClick() {

    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_MEMBERSHIP_SETTINGS;

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


        switch (position) {
            case 0:
                if (positionOffset == 0) {
                    setUpTransitions(binding.page7, binding.page6, 0.0f);
                    setUpTransitions(binding.page6, binding.page4, 0.0f);
                    setUpTransitions(binding.page4, binding.page3, 0.0f);
                    setUpTransitions(binding.page3, binding.page2, 0.0f);
                }
                setUpFirstTransition(positionOffset);
                break;
            case 1:
                if (!trackedReading) {
                    trackedReading = true;
                    GoogleAnalyticsHelper.trackEvent(CATEGORY_MEMBERSHIP, READ_MEMBERSHIP_DETAILS, PROPERTY_MEMBERSHIP_SETTINGS);
                }

                setUpFirstTransition(1.0f);

                setUpTransitions(binding.page3, binding.page2, positionOffset);

                if (firstPageChangeListener != null) {
                    firstPageChangeListener.onFirstPageChange(1.0f, 1);
                }
                break;
            case 2:
                setUpTransitions(binding.page3, binding.page2, 1.0f);
                setUpTransitions(binding.page4, binding.page3, positionOffset);
                break;
            case 3:
                setUpTransitions(binding.page4, binding.page3, 1.0f);
                setUpTransitions(binding.page6, binding.page4, positionOffset);
                break;
            case 4:
                setUpTransitions(binding.page6, binding.page4, 1.0f);
                setUpTransitions(binding.page7, binding.page6, positionOffset);
                break;
            case 5:
                setUpTransitions(binding.page7, binding.page6, 1.0f);
                break;

        }

        setUpArrowAnimation(position, positionOffset);

    }

    private void setUpArrowAnimation(int position, float positionOffset) {
        float alpha;
        float upAlpha;
        PagerAdapter adapter = binding.viewPager.getAdapter();

        if (adapter == null)
            return;

        int count = adapter.getCount();
        boolean goingToLastPage = count - 2 >= position;
        boolean onLastPage = count - 1 == position;

        if (onLastPage) {
            upAlpha = 1.0f;
        } else if (!goingToLastPage) {
            upAlpha = 20 * positionOffset - 19.0f;
        } else {
            upAlpha = 0.0f;
        }

        if (positionOffset < 0.05 && !onLastPage) {
            alpha = -20 * positionOffset + 1.0f;
        } else if (positionOffset > 0.95 && !goingToLastPage) {
            alpha = 20 * positionOffset - 19.0f;
        } else {
            alpha = 0.0f;
        }
        binding.bottomArrow.setAlpha(alpha);
        binding.backToTopLayout.setAlpha(upAlpha);
    }

    private void setUpFirstTransition(float positionOffset) {
        crossFade(binding.page2, binding.page1, positionOffset);
        buttonLayout(positionOffset);
        binding.page1.setVisibility(View.VISIBLE);
        binding.page2.setVisibility(View.VISIBLE);
        int height = ((ViewGroup) binding.page1.getParent()).getHeight();
        float offset2 = height * (1.0f - positionOffset);
        binding.page2.setY(offset2);

    }

    @Override
    public void onPause() {
        super.onPause();
        currentItem = binding.viewPager.getCurrentItem();

    }

    private void buttonLayout(float positionOffset) {
        if (initButtonX == 0.0f || finaButtonX == 0.0f || binding.getRoot().getHeight() == 0.0)
            return;


        float adjustedOffset;
        if (positionOffset < 0.5f) {
            adjustedOffset = (1.0f - positionOffset * 2.0f);
            if (firstPageChangeListener != null) {
                firstPageChangeListener.onFirstPageChange(positionOffset * 2.0f, 0);
            }
        } else {
            adjustedOffset = 0.0f;
            firstPageChangeListener.onFirstPageChange(1.0f, 0);
        }

        float offset = (initButtonX - finaButtonX) * adjustedOffset;

        binding.addMemberButton.setY((offset + finaButtonX));


    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private void setUpTransitions(@NonNull View viewIn, @NonNull View viewOut,
                                  @FloatRange(from = 0.0, to = 1.0) float positionOffset) {

        slideInAndOut(viewIn, viewOut, positionOffset);
        crossFade(viewIn, viewOut, positionOffset);

    }

    private void crossFade(@NonNull View viewIn, @NonNull View viewOut,
                           @FloatRange(from = 0.0, to = 1.0) float positionOffset) {
        float adjustedOffset;
        float adjustedOffset2;
        if (positionOffset < 0.5f) {
            adjustedOffset = 0.0f;
            adjustedOffset2 = (1.0f - positionOffset * 2.0f);

        } else {
            adjustedOffset = (positionOffset - 0.5f) * 2.0f;
            adjustedOffset2 = 0.0f;
        }

        viewIn.setVisibility(View.VISIBLE);
        viewOut.setVisibility(View.VISIBLE);
        viewIn.setAlpha(adjustedOffset);
        viewOut.setAlpha(adjustedOffset2);
    }

    private void slideInAndOut(@NonNull View viewIn, @NonNull View viewOut, @FloatRange(from = 0.0, to = 1.0) float positionOffset) {
        viewOut.setVisibility(View.VISIBLE);
        viewIn.setVisibility(View.VISIBLE);
        int height = ((ViewGroup) viewIn.getParent()).getHeight();
        float offset = positionOffset * height;
        float offset2 = height * (1.0f - positionOffset);
        viewOut.setY(-offset);
        viewIn.setY(offset2);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.incident_support_btn: {
                GoogleAnalyticsHelper.trackEvent(CATEGORY_MEMBERSHIP, ACTION_CONTACT_INCIDENT_SUPPORT, PROPERTY_MEMBERSHIP_SETTINGS);
                Intent intent = new Intent(
                        Intent.ACTION_DIAL, Uri.fromParts(
                        "tel", "+18557295583", null));
                startActivity(intent);
            }
            break;

            case R.id.back_to_top_layout:
                binding.viewPager.setCurrentItem(0, true);
                break;
            case R.id.add_member_button: {
                GoogleAnalyticsHelper.trackEvent(CATEGORY_MEMBERSHIP, ACTION_ADD_MEMBERSHIP, PROPERTY_MEMBERSHIP_SETTINGS, null, location.id, 0);
                String url = Constants.autoLoginUrlWithPromoCodes(location, getContext(), false);
                String title = getString(R.string.activate_membership);
                Intent intent = WebActivity.newInstance(getContext(), url, title, true);
                startActivity(intent);
            }
            break;

        }
    }

    public interface FirstPageChangeListener {
        void onFirstPageChange(float change, int page);
    }

    public void setFirstPageChangeListener(FirstPageChangeListener firstPageChangeListener) {
        this.firstPageChangeListener = firstPageChangeListener;
    }
}
