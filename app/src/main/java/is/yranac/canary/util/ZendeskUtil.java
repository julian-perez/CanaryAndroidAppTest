package is.yranac.canary.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.zendesk.sdk.feedback.ZendeskFeedbackConfiguration;
import com.zendesk.sdk.feedback.ui.ContactZendeskActivity;
import com.zendesk.sdk.model.network.AnonymousIdentity;
import com.zendesk.sdk.model.network.Identity;
import com.zendesk.sdk.network.impl.ZendeskConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import is.yranac.canary.BuildConfig;
import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;

import static is.yranac.canary.Constants.ZENDESK_CLIENT;
import static is.yranac.canary.Constants.ZENDESK_TOKEN;
import static is.yranac.canary.Constants.ZENDESK_URL;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_CONTRACT_SUPPORT;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_HELP;

/**
 * Created by Schroeder on 11/20/15.
 */
public class ZendeskUtil {

    public static void loadHelpCenter(final Context context, String url) {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }


    public static void showZendesk(final Activity activity, final long entryId) {
        showZendesk(activity, entryId, activity.getString(R.string.contact_support));
    }

    public static void showZendesk(final Activity activity, final long entryId, final String title, final String... extraTags) {
        ZendeskConfig.INSTANCE.init(activity.getApplicationContext(), ZENDESK_URL, ZENDESK_TOKEN, ZENDESK_CLIENT);
        GoogleAnalyticsHelper.trackEvent(CATEGORY_HELP, ACTION_CONTRACT_SUPPORT);

        Customer currentCustomer = CurrentCustomer.getCurrentCustomer();
        String name = "";
        if (currentCustomer != null)
            name = currentCustomer.getFullName();
        Identity anonymousIdentity = new AnonymousIdentity.Builder()
                .withEmailIdentifier(PreferencesUtils.getUserName())
                .withNameIdentifier(name).build();
        ZendeskConfig.INSTANCE.setIdentity(anonymousIdentity);
        ZendeskConfig.INSTANCE.setContactConfiguration(new ZendeskFeedbackConfiguration() {
            @Override
            public String getRequestSubject() {
                return title;
            }

            @Override
            public String getAdditionalInfo() {
                return Utils.getDeviceName();
            }

            @Override
            public List<String> getTags() {
                List<String> list = new ArrayList<>();
                list.add("android");
                if (Constants.ZENDESK_TESTING) {
                    list.add("beta-program");
                    list.add(BuildConfig.FLAVOR);
                }

                list.addAll(Arrays.asList(extraTags));

                Location location = UserUtils.getLastViewedLocation();

                if (!location.isUnitedStates()) {
                    list.add("international");
                }

                if (entryId != 0) {
                    list.add(String.valueOf(entryId));
                }

                return list;
            }
        });


        Intent supportIntent = new Intent(activity, ContactZendeskActivity.class);
        activity.startActivity(supportIntent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
