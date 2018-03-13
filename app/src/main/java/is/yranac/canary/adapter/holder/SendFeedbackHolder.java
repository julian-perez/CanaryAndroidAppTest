package is.yranac.canary.adapter.holder;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import java.util.concurrent.TimeUnit;

import is.yranac.canary.R;
import is.yranac.canary.databinding.ListrowSendFeedbackBinding;
import is.yranac.canary.messages.EntryTableUpdated;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.ui.BaseActivity;
import is.yranac.canary.ui.MainActivity;
import is.yranac.canary.util.AnimationHelper;
import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.UserUtils;
import is.yranac.canary.util.ZendeskUtil;

/**
 * Created by michaelschroeder on 10/27/17.
 */

public class SendFeedbackHolder implements View.OnClickListener {


    private final ListrowSendFeedbackBinding binding;
    private final BaseActivity activity;

    public SendFeedbackHolder(BaseActivity activity, ListrowSendFeedbackBinding listrowSendFeedbackBinding) {
        this.binding = listrowSendFeedbackBinding;
        this.activity = activity;
        this.binding.excellentBtn.setOnClickListener(this);
        this.binding.notGreatBtn.setOnClickListener(this);
        this.binding.noThanksBtn.setOnClickListener(this);
        this.binding.writeAReview.setOnClickListener(this);
        this.binding.noThanksFeedbackBtn.setOnClickListener(this);
        this.binding.yesSureBtn.setOnClickListener(this);


    }

    public void checkLayout() {
        if (!PreferencesUtils.hasShownAmazonHeader(activity)) {
            return;
        }
        if (!PreferencesUtils.hasShownAmazonEnd(activity)) {
            PreferencesUtils.setHasShownAmazonEnd(activity);
            binding.howIsYourExperienceLayout.setVisibility(View.INVISIBLE);
            binding.giveUsFeedbackLayout.setVisibility(View.INVISIBLE);
            binding.spreadTheWordLayout.setVisibility(View.INVISIBLE);
            binding.thanksForLettingUsKnow.setVisibility(View.VISIBLE);
            TinyMessageBus.postDelayed(new EntryTableUpdated(true, UserUtils.getLastViewedLocationId()), TimeUnit.SECONDS.toMillis(10));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.excellent_btn:
                binding.howIsYourExperienceLayout.setVisibility(View.INVISIBLE);
                binding.spreadTheWordLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.not_great_btn:
                binding.howIsYourExperienceLayout.setVisibility(View.INVISIBLE);
                binding.giveUsFeedbackLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.no_thanks_btn:
                binding.spreadTheWordLayout.setVisibility(View.INVISIBLE);
                AnimationHelper.slideViewInFromTop(binding.thanksForLettingUsKnow,500, 0);
                PreferencesUtils.setHasShownAmazonHeader(activity);
                TinyMessageBus.postDelayed(new EntryTableUpdated(true, UserUtils.getLastViewedLocationId()), TimeUnit.SECONDS.toMillis(3));

                break;
            case R.id.write_a_review:
                if (activity instanceof MainActivity) {
                    Device device = (((MainActivity) activity).currentDevice());

                    int deviceType = DeviceType.CANARY_AIO;
                    if (device != null) {
                        deviceType = device.getDeviceType();
                    }
                    String url;
                    switch (deviceType) {
                        case DeviceType.CANARY_AIO:
                            url = activity.getString(R.string.aio_amazon_url);
                            break;
                        case DeviceType.FLEX:
                            url = activity.getString(R.string.flex_amazon_url);
                            break;
                        default:
                            url = activity.getString(R.string.aio_amazon_url);
                            break;
                    }


                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    activity.startActivity(browserIntent);
                }

                PreferencesUtils.setHasShownAmazonHeader(activity);
                break;
            case R.id.no_thanks_feedback_btn:
                binding.giveUsFeedbackLayout.setVisibility(View.INVISIBLE);
                AnimationHelper.slideViewInFromTop(binding.thanksForLettingUsKnow,500, 0);
                PreferencesUtils.setHasShownAmazonHeader(activity);
                TinyMessageBus.postDelayed(new EntryTableUpdated(true, UserUtils.getLastViewedLocationId()), TimeUnit.SECONDS.toMillis(3));
                break;
            case R.id.yes_sure_btn:
                PreferencesUtils.setHasShownAmazonHeader(activity);
                ZendeskUtil.showZendesk(activity, 0,activity.getString(R.string.send_feedback), "amazon_app_feedback");
                break;
        }
    }
}
