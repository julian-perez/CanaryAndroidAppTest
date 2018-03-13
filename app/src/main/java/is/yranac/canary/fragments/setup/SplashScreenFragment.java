package is.yranac.canary.fragments.setup;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.SimpleExoPlayer;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSetupSplashScreenBinding;
import is.yranac.canary.media.CanaryVideoPlayer;
import is.yranac.canary.messages.OnBackBlocked;
import is.yranac.canary.messages.PushToken;
import is.yranac.canary.model.authentication.OauthResponse;
import is.yranac.canary.services.api.CustomerAPIService;
import is.yranac.canary.services.api.OathAuthenticationAPIService;
import is.yranac.canary.services.intent.GlobalAPIntentServiceManager;
import is.yranac.canary.ui.BaseActivity;
import is.yranac.canary.ui.LaunchActivity;
import is.yranac.canary.ui.MyPreferencesActivity;
import is.yranac.canary.ui.views.CanaryTextWatcher;
import is.yranac.canary.ui.views.EditTextWithLabel;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.AnimationHelper;
import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.StringUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.keystore.KeyStoreHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_GET_STARTED;


public class SplashScreenFragment extends SetUpBaseFragment implements TextureView.SurfaceTextureListener {


    private final static String LOG_TAG = "SplashScreenFragment";
    private FragmentSetupSplashScreenBinding binding;
    private CanaryVideoPlayer player;
    private PasswordOnClickListener passwordOnClickListener;
    private List<EditTextWithLabel> editTextList = new ArrayList<>();
    private List<EditTextWithLabel> editTextListForgotPassword = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setup_splash_screen, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.preferecnesBtn.setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), MyPreferencesActivity.class);
                        startActivity(i);
                        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }

                });


        binding.signUpBtn.setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        addFragmentToStack(new CreateAccountFragment(), Utils.FADE_IN_DISAPPEAR);
                    }

                });

        binding.goToSignInBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.signInLayout.getVisibility() != View.VISIBLE) {
                    AnimationHelper.fadeViewIn(binding.signInLayout, 400);
                    AnimationHelper.fadeViewOut(binding.textLayout, 400);
                    player.setPlayWhenReady(false);
                    fragmentStack.disableBackButton();
                }
            }
        });

        binding.forgotPasswordBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });

        binding.signInBtn.setEnabled(false);
        binding.goToSignInBtn.setText(StringUtils.spannableStringBuilder(getContext(), R.string.already_member, R.string.sign_in_period), TextView.BufferType.SPANNABLE);
        binding.introVideoView.setSurfaceTextureListener(this);
        player = new CanaryVideoPlayer(getContext(), CanaryVideoPlayer.VideoType.VideoTypeMP4);

        String uri = "asset:///intro_video.mp4";
        player.setVideoListener(new SimpleExoPlayer.VideoListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                float deviceWidth = displayMetrics.widthPixels;
                float deviceHeight = displayMetrics.heightPixels;

                float ratio = deviceWidth / deviceHeight;
                float mediaRatio = (float) width / (float) height;

                RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) binding.introVideoView.getLayoutParams();

                if (ratio > mediaRatio) {
                    layout.height = (int) (binding.introVideoView.getWidth() * (1 / mediaRatio));
                } else {
                    layout.width = (int) (binding.introVideoView.getHeight() * mediaRatio);

                }
                binding.introVideoView.setLayoutParams(layout);
            }

            @Override
            public void onRenderedFirstFrame() {

            }
        });

        player.addListener(new CanaryVideoPlayer.Listener() {
            @Override
            public void onStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    case CanaryVideoPlayer.STATE_ENDED:
                        player.seekTo(0);
                        player.setPlayWhenReady(true);
                        break;

                }
            }
        });
        player.setDataSource(uri);
        binding.forgotPasswordTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgotPassword();

            }
        });


        binding.signInBtn.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        signIn();
                    }
                }
        );


        onClickFocusListener(binding.email, binding.email.getEditText());
        onClickFocusListener(binding.password, binding.password.getEditText());
        onClickFocusListener(binding.emailForgotPassword, binding.emailForgotPassword.getEditText());
        passwordOnClickListener = new PasswordOnClickListener(binding.passwordCheckbox, binding.password.getEditText());
        binding.passwordShowCheckbox.setOnClickListener(passwordOnClickListener);

        setupViewWatcher(binding.email, CanaryTextWatcher.ANY_TEXT, editTextList, stackFragmentCallback);

        setupViewWatcher(binding.password, CanaryTextWatcher.VALID_PASSWORD, editTextList, stackFragmentCallback);


        setupViewWatcher(binding.emailForgotPassword, CanaryTextWatcher.ANY_TEXT, editTextListForgotPassword, forgotStackFragmentCallback);

    }

    @Override
    public void onStart() {
        super.onStart();

        TinyMessageBus.register(this);
    }


    @Override
    public void onStop() {
        TinyMessageBus.unregister(this);
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        passwordOnClickListener.syncTextView();
        fragmentStack.showHeader(false);


        editTextList.clear();
        editTextList.add(binding.email);
        editTextList.add(binding.password);
        editTextListForgotPassword.clear();
        editTextListForgotPassword.add(binding.emailForgotPassword);
        if (showSignIn()) {
            setShowIn(false);
            binding.signInLayout.setVisibility(View.VISIBLE);
            binding.forgotPasswordLayout.setVisibility(View.INVISIBLE);
            binding.textLayout.setVisibility(View.INVISIBLE);
            fragmentStack.disableBackButton();
        }

    }


    @Override
    public void onRightButtonClick() {

    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_GET_STARTED;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        player.setSurface(new Surface(surface));
        player.prepare();
        player.seekTo(0);

        if (binding.signInLayout.getVisibility() == View.VISIBLE) {
            player.setPlayWhenReady(false);
        } else {
            player.setPlayWhenReady(true);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Subscribe
    public void onBackBlocked(OnBackBlocked onBackBlocked) {


        if (binding.forgotPasswordLayout.getVisibility() == View.VISIBLE) {
            AnimationHelper.fadeViewIn(binding.signInLayout, 400);
            AnimationHelper.fadeViewOut(binding.forgotPasswordLayout, 400);
            binding.emailForgotPassword.setText(binding.email.getText());
            return;
        }


        if (binding.signInLayout.getVisibility() == View.VISIBLE) {
            AnimationHelper.fadeViewIn(binding.textLayout, 400);
            AnimationHelper.fadeViewOut(binding.signInLayout, 400);
            player.setPlayWhenReady(true);
            fragmentStack.enableBackButton();
        }
    }

    private void showForgotPassword() {
        if (binding.forgotPasswordLayout.getVisibility() != View.VISIBLE) {
            AnimationHelper.fadeViewIn(binding.forgotPasswordLayout, 400);
            AnimationHelper.fadeViewOut(binding.signInLayout, 400);
        }
    }

    private StackFragmentCallback stackFragmentCallback = new StackFragmentCallback() {
        @Override
        public void onAllEditTextHasChanged(boolean allValid) {
            binding.signInBtn.setEnabled(allValid);
        }
    };

    private StackFragmentCallback forgotStackFragmentCallback = new StackFragmentCallback() {
        @Override
        public void onAllEditTextHasChanged(boolean allValid) {
            binding.forgotPasswordBtn.setEnabled(allValid);
        }
    };

    private void signIn() {
        hideSoftKeyboard();
        BaseActivity baseActivity = (BaseActivity) getActivity();
        if (!baseActivity.hasInternetConnection())
            return;

        showLoading(true, null);


        PreferencesUtils.setUserName(
                binding.email.getText()
        );


        OathAuthenticationAPIService.oauthAuthentication(
                PreferencesUtils.getUserName(), binding.password.getText(), new Callback<OauthResponse>() {
                    @Override
                    public void success(OauthResponse oauthResponse, Response response) {
                        showLoading(false, null);
                        KeyStoreHelper.saveToken(oauthResponse.accessToken);
                        KeyStoreHelper.saveRefreshToken(oauthResponse.refreshToken);

                        Activity activity = getActivity();
                        if (activity == null)
                            return;

                        GlobalAPIntentServiceManager.resetAlarms(getContext());

                        Intent i = new Intent(getActivity(), LaunchActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                        TinyMessageBus.post(new PushToken());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        showLoading(false, null);

                        try {
                            if (getActivity() != null) {
                                String retrofitError = Utils.getErrorMessageFromRetrofit(getActivity(), error);
                                if (retrofitError != null && retrofitError.contains("invalid_grant")) {
                                    AlertUtils.showGenericAlert(getActivity(), getString(R.string.invalid_login_credentials));
                                } else {
                                    AlertUtils.showGenericAlert(getActivity(), retrofitError);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
        );
    }


    private void resetPassword() {
        String email = binding.emailForgotPassword.getText();
        showLoading(true, getString(R.string.loading_dialog));
        CustomerAPIService.resetCustomerPassword(email, new Callback<Void>() {

            @Override
            public void success(Void aVoid, Response response) {
                showLoading(false, null);
                AlertUtils.showResetPasswordSuccessDialog(getActivity(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackBlocked(null);
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                showLoading(false, null);
                try {
                    AlertUtils.showGenericAlert(getActivity(), Utils.getErrorMessageFromRetrofit(getActivity(), error));
                } catch (JSONException ignore) {

                }
            }
        });
    }

    @Override
    public void onDestroy() {
        if (player != null)
            player.release();
        super.onDestroy();
    }
}
