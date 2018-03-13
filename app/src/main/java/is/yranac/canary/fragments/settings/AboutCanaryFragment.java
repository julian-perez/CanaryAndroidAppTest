package is.yranac.canary.fragments.settings;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import is.yranac.canary.BuildConfig;
import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.fragments.GenericWebviewFragment;
import is.yranac.canary.ui.BaseActivity;
import is.yranac.canary.util.Utils;

import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_ABOUT_CANARY;

public class AboutCanaryFragment extends SettingsFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings_about_canary, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupSocialButtons(view);
        setupButton(view);
    }


    private void setupSocialButtons(View view) {


        ImageButton twitterBtn = (ImageButton) view.findViewById(R.id.twitter_button);

        twitterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getOpenTwitterIntent();

            }
        });
        ImageButton facebookBtn = (ImageButton) view.findViewById(R.id.facebook_button);
        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(getOpenFacebookIntent(getActivity()));
            }
        });
        ImageButton instagramBtn = (ImageButton) view.findViewById(R.id.instagram_button);
        instagramBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(getOpenInstagramIntent(getActivity()));
            }
        });
    }

    private void setupButton(View view) {

        TextView appVersionTextView = (TextView) view.findViewById(R.id.app_version_label);

        StringBuilder appVersion = new StringBuilder("v");

        appVersion.append(BuildConfig.VERSION_NAME);
        if (Utils.isNotProd()) {
            appVersion.append(" ");
            appVersion.append(BuildConfig.VERSION_CODE);
        }

        appVersionTextView.setText(appVersion);

        final BaseActivity baseActivity = (BaseActivity) getActivity();

        Button reviewBtn = (Button) view.findViewById(R.id.review_btn);
        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri marketUri = Uri.parse("market://details?id=is.yranac.canary");
                Intent i = new Intent(Intent.ACTION_VIEW, marketUri);
                startActivity(i);
            }
        });

        Button websiteBtn = (Button) view.findViewById(R.id.website_btn);
        websiteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Constants.CANARY_WEBSITE();

                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url)));
            }
        });

        Button privacyPolicyBtn = (Button) view.findViewById(R.id.privacy_policy_btn);
        privacyPolicyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!baseActivity.hasInternetConnection())
                    return;

                String url = Constants.CANARY_PP();
                addModalFragment(GenericWebviewFragment.newInstance(url, R.string.privacy_policy));

            }
        });


        Button termsBtn = (Button) view.findViewById(R.id.terms_and_conditions_btn);
        termsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!baseActivity.hasInternetConnection())
                    return;

                String url = Constants.CANARY_TOS();
                addModalFragment(GenericWebviewFragment.newInstance(url, R.string.terms_of_service_label));
            }
        });


        Button endUserBtn = (Button) view.findViewById(R.id.end_user_btn);
        endUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!baseActivity.hasInternetConnection())
                    return;

                String url = Constants.CANARY_EULA();
                addModalFragment(GenericWebviewFragment.newInstance(url, R.string.eula));
            }
        });


        Button complianceBtn = (Button) view.findViewById(R.id.compliance_btn);
        complianceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!baseActivity.hasInternetConnection())
                    return;

                String url = Constants.CANARY_COMPLIANCE();
                addModalFragment(GenericWebviewFragment.newInstance(url, R.string.compliance_notices));
            }
        });
    }

    public static Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("fb://page/494625077260133")); //Trys to make intent with FB's URI
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/canary.is")); //catches and opens a url to the desired page
        }
    }

    private Intent getOpenInstagramIntent(Context context) {
        try {
            Intent insta_intent = context.getPackageManager().getLaunchIntentForPackage("com.instagram.android");
            insta_intent.setComponent(new ComponentName("com.instagram.android", "com.instagram.android.activity.UrlHandlerActivity"));
            insta_intent.setData(Uri.parse("http://instagram.com/_u/caughtbycanary"));
            return insta_intent;
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/caughtbycanary")); //catches and opens a url to the desired page
        }
    }


    private void getOpenTwitterIntent() {

        String twitterApp = "twitter://user?screen_name=" + getString(R.string.canary_twitter);
        String twitterSite = "https://twitter.com/" + getString(R.string.canary_twitter);

        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(twitterApp)));
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(twitterSite)));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.setHeaderTitle(R.string.about_canary);
    }

    @Override
    public void onRightButtonClick() {

    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_ABOUT_CANARY;
    }
}
