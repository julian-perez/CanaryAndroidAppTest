package is.yranac.canary.ui;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.databinding.ActivityMaskingBinding;
import is.yranac.canary.messages.ShowGetMaskingHelp;
import is.yranac.canary.messages.masking.SaveMasksRequest;
import is.yranac.canary.model.MaskingViewController;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.masking.DeviceMasks;
import is.yranac.canary.services.api.DeviceAPIService;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.JSONUtil;
import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.StringUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static is.yranac.canary.ui.SettingsFragmentStackActivity.get_masking_help;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_MASKING_HELP;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_MASKING_SAVE;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_MASKING;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_MASKING_MANAGE_MASK;

/**
 * Created by sergeymorozov on 9/28/16.
 */

public class MaskingActivity extends BaseActivity {

    private static final String OKAY = "OKAY";
    private ActivityMaskingBinding binding;
    private Device device;
    private DeviceMasks deviceMasks;
    private MaskingViewController listener;
    private AlertDialog mDialog;
    private String thumbnail;

    /**
     * Return the Screen tag for google Analytics
     *
     * @return Null if no tagging otherwise the corresponding tag
     */
    @Override
    protected String getAnalyticsTag() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMaskingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle args = getIntent().getExtras();
        if (args == null || StringUtils.isNullOrEmpty(args.getString(key_device, null)))
            return;

        device = JSONUtil.getObject(args.getString(key_device), Device.class);
        thumbnail = args.getString(key_thumbnail);
        long thumbnailId = args.getLong(key_thumbnail_id);

        deviceMasks = JSONUtil.getObject(args.getString(key_masks), DeviceMasks.class);

        if (!TextUtils.isEmpty(thumbnail)) {
            binding.backgroundImage.loadThumbnail(thumbnailId, thumbnail);
        } else {
            binding.noPreviewImage.setVisibility(View.VISIBLE);
        }

        listener = new MaskingViewController();

        binding.setControlListener(listener);
    }

    @Subscribe
    public void saveMasks(SaveMasksRequest request) {
        if (binding == null || binding.maskDrawView == null)
            return;
        GoogleAnalyticsHelper.trackEvent(CATEGORY_MASKING, ACTION_MASKING_SAVE, PROPERTY_MASKING_MANAGE_MASK, device.uuid, device.getLocationId(), 0);


        final DeviceMasks masks = binding.maskDrawView.getMasksToSave(device.resourceUri);

        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }

        mDialog = AlertUtils.initLoadingDialog(MaskingActivity.this, getString(R.string.saving), false);


        DeviceAPIService.saveDeviceMasks(device.id, masks, new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                Toast.makeText(MaskingActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        TinyMessageBus.register(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (listener != null)
            listener.register(binding == null ? null : binding.maskDrawView);

        if (binding != null && binding.maskDrawView != null) {
            if (!binding.maskDrawView.checkEnableSave())
                binding.maskDrawView.setDeviceMasks(deviceMasks, device);
        }

        binding.deleteMaskButtonText.setText(getString(R.string.masking_delete_mask));
        binding.addMasksButtonText.setText(getString(R.string.masking_create_mask));
        binding.imagePreviewText.setText(R.string.image_preview_unavailable);
        listener.setTopMessage(getString(R.string.masking_default_message));

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        if (!PreferencesUtils.hasSeenCVMaskingTutorial()) {
            binding.tutorialText.setText(getString(R.string.masks_reduce_notifications));
            binding.nextBtn.setText(getString(R.string.next));
            binding.tutorialLayout.setVisibility(View.VISIBLE);
            binding.nextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (v.getTag() == OKAY) {
                        binding.tutorialLayout.animate().alpha(0.0f).setDuration(1000).setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                binding.tutorialLayout.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        }).start();
                        return;
                    }


                    Integer colorFrom = ContextCompat.getColor(getBaseContext(), R.color.white);
                    Integer colorTo = ContextCompat.getColor(getBaseContext(), R.color.transparent);

                    final ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo, colorFrom);
                    colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            binding.tutorialText.setTextColor((Integer) animator.getAnimatedValue());
                            binding.nextBtn.setTextColor((Integer) animator.getAnimatedValue());
                            if (animator.getAnimatedFraction() > 0.5) {
                                binding.nextBtn.setText(R.string.okay);
                                binding.tutorialText.setText(R.string.keep_in_mind_if_device_moved);
                                binding.nextBtn.setTag(OKAY);
                            }
                        }

                    });
                    colorAnimation.setDuration(1000);
                    colorAnimation.start();
                    PreferencesUtils.setHasSeenCVMaskingTutorial();
                }
            });
        }

        if (TextUtils.isEmpty(thumbnail)) {
            binding.maskDrawView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            return;
        }

        //we need to position view so that if screen ratio is different than image, it's aligned properly
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;

        float idealRatio = (16f / 9f);
        float currentWindowRatio = (float) screenWidth / (float) screenHeight;

        RelativeLayout.LayoutParams newParams;

        if (currentWindowRatio < idealRatio) {
            //screen is taller. we need to alight to left and right
            newParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        } else if (currentWindowRatio > idealRatio) {
            //screen is longer. we need to align to top and bottom.
            newParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        } else {
            newParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        }

        newParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        binding.backgroundImage.setLayoutParams(newParams);
        binding.maskDrawView.setLayoutParams(newParams);

        binding.getRoot().invalidate();
    }

    @Override
    public void onStop() {
        super.onStop();
        TinyMessageBus.unregister(this);
        if (listener != null)
            listener.unregister(binding == null ? null : binding.maskDrawView);
    }

    @Subscribe
    public void showHelp(ShowGetMaskingHelp event) {
        GoogleAnalyticsHelper.trackEvent(CATEGORY_MASKING, ACTION_MASKING_HELP, null, null, 0, 0);
        Intent i = new Intent(this, SettingsFragmentStackActivity.class);
        i.putExtra(get_masking_help, true);
        startActivity(i);
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onBackPressed() {
        if (listener.isSaveEnabled()) {

            if (mDialog != null && mDialog.isShowing()) {
                return;
            }

            if (binding.maskDrawView != null && binding.maskDrawView.checkEnableSave()) {

                mDialog = AlertUtils.showGenericAlert(this, getString(R.string.do_you_want_to_save_changes), null, 0, getString(R.string.dont_save), getString(R.string.save),
                        0, 0, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mDialog != null) {
                                    mDialog.dismiss();
                                    mDialog = null;
                                    saveMasks(null);
                                }
                            }
                        });

                return;
            }
        }

        super.onBackPressed();
    }
}
