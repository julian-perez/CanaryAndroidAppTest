package is.yranac.canary.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentVideoPlaybackBinding;
import is.yranac.canary.util.AnimationHelper;
import is.yranac.canary.util.Log;

public class VideoPlaybackFragment extends Fragment {
    private static final String LOG_TAG = "WatchLiveFragment";


    private boolean letterBox;
    private boolean flexLoading;

    private static final String THUMBNAIL = "thumbnailUrl";
    private static final String THUMBNAIL_ID = "thumbnail_id";
    private static final String THUMBNAIL_SHOW = "thumbnaiShow";
    private static final String OFFLINE = "offline";
    private static final String PRIVATE = "private";
    private static final String SHOW_PLAY_BTN = "showPlayBtn";
    private static final String LETTER_BOX = "letterBoxed";
    private static final String FLEX_LOADING = "flexLoading";


    private FragmentVideoPlaybackBinding binding;

    public static VideoPlaybackFragment newInstance(String thumbnailUrl, long thumbnailId, boolean showThumbnailsInViewpager,
                                                    boolean deviceOffline, boolean isPrivate, boolean showPlayBtn, boolean letterBoxed,
                                                    boolean flexLoading) {
        VideoPlaybackFragment fragment = new VideoPlaybackFragment();

        Bundle args = new Bundle();
        args.putString(THUMBNAIL, thumbnailUrl);
        args.putLong(THUMBNAIL_ID, thumbnailId);
        args.putBoolean(THUMBNAIL_SHOW, showThumbnailsInViewpager);
        args.putBoolean(OFFLINE, deviceOffline);
        args.putBoolean(PRIVATE, isPrivate);
        args.putBoolean(SHOW_PLAY_BTN, showPlayBtn);
        args.putBoolean(LETTER_BOX, letterBoxed);
        args.putBoolean(FLEX_LOADING, flexLoading);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentVideoPlaybackBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        boolean deviceOffline = getArguments().getBoolean(OFFLINE, false);
        boolean isPrivate = getArguments().getBoolean(PRIVATE, false);


        if (deviceOffline) {
            showOfflineView(R.string.device_offline);
            return;
        }

        if (isPrivate) {
            showOfflineView(R.string.set_to_private_period);
            return;
        }

        ImageLoader imageLoader = ImageLoader.getInstance();

        boolean showThumbnail = getArguments().getBoolean(THUMBNAIL_SHOW);
        boolean showPlay = getArguments().getBoolean(SHOW_PLAY_BTN);
        letterBox = getArguments().getBoolean(LETTER_BOX, false);

        flexLoading = getArguments().getBoolean(FLEX_LOADING, false);

        String thumbnailUrl = getArguments().getString(THUMBNAIL);
        if (thumbnailUrl != null && !flexLoading) {

            long thumbnailId = getArguments().getLong(THUMBNAIL_ID, 0);

            binding.eventDeviceThumbnail.loadThumbnail(thumbnailId, thumbnailUrl);

            imageLoader.displayImage(
                    thumbnailUrl, binding.eventDeviceThumbnailCropped);

        }


        showPlayBtn(showPlay);
        showVideoLoadingOverlay(!showPlay);
        showEventDeviceThumbnail(showThumbnail);

        if (flexLoading) {
            startFlexLoading();
        } else if (letterBox) {
            setUpLetterBox();
        }
    }

    private void startFlexLoading() {

        /**
         * Fuck the code that is below this comment
         */

        Log.i(LOG_TAG, "Start flex");
        binding.flexLoadingText.setVisibility(View.VISIBLE);
        binding.flexLoadingText.setText(R.string.waking_from_sleep);

        setupFadeTextWithCompetion(binding.flexLoadingText, getString(R.string.establishing_your_connection), new AnimationHelper.AnimationCompletion() {
            @Override
            public void onComplete() {
                setupFadeTextWithCompetion(binding.flexLoadingText, getString(R.string.loading_live_stream), null);
            }
        });

    }

    private void setupFadeTextWithCompetion(final TextView view, final String text, final AnimationHelper.AnimationCompletion completion) {

        final AlphaAnimation alphaAnimation2 = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation2.setDuration(500);
        alphaAnimation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (completion != null) {
                    completion.onComplete();
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(500);
        alphaAnimation.setStartOffset(2000);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setText(text);
                view.startAnimation(alphaAnimation2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(alphaAnimation);
    }

    private void setUpLetterBox() {
        if (flexLoading) {
            binding.eventDeviceThumbnail.setVisibility(View.GONE);
            return;
        }

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int deviceWidth = displayMetrics.widthPixels;

        int newHeight = (int) ((float) deviceWidth * (9.0f / 16.0f));
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) binding.eventDeviceThumbnailCropped.getLayoutParams();
        layoutParams.height = newHeight;

        binding.eventDeviceThumbnailCropped.setLayoutParams(layoutParams);
        binding.eventDeviceThumbnailCropped.setVisibility(View.VISIBLE);
        binding.eventDeviceThumbnail.setVisibility(View.GONE);

    }

    public void showVideoLoadingOverlay(boolean showVideoLoadingOverlay) {
        binding.offlineContainer.setVisibility(View.GONE);
        if (showVideoLoadingOverlay) {
            binding.videoLoadingOverlay.setVisibility(View.VISIBLE);
            if (flexLoading) {
                startFlexLoading();
            } else {
                binding.offlineContainer.setVisibility(View.GONE);
            }
        } else {
            binding.videoLoadingOverlay.setVisibility(View.GONE);
        }
    }

    public void showPlayBtn(boolean showPlay) {
        binding.offlineContainer.setVisibility(View.GONE);
        if (showPlay) {
            binding.playBtn.setVisibility(View.VISIBLE);
        } else {
            binding.playBtn.setVisibility(View.GONE);
        }
    }

    public void showEventDeviceThumbnail(boolean showEventDeviceThumbnail) {
        if (letterBox)
            return;

        binding.offlineContainer.setVisibility(View.GONE);
        if (showEventDeviceThumbnail) {
            binding.eventDeviceThumbnailContainer.setVisibility(View.VISIBLE);
        } else {
            binding.eventDeviceThumbnailContainer.setVisibility(View.GONE);
        }
    }


    public void showOfflineView(int text) {
        binding.offlineContainer.setVisibility(View.VISIBLE);
        binding.eventDeviceThumbnailContainer.setVisibility(View.GONE);
        binding.videoLoadingOverlay.setVisibility(View.GONE);
        binding.playBtn.setVisibility(View.GONE);
        binding.textCanaryStatus.setText(text);
    }


}
