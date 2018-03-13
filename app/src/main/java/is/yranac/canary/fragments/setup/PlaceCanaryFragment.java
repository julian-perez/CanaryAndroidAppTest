package is.yranac.canary.fragments.setup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSetupPlaceCanaryBinding;
import is.yranac.canary.media.CanaryVideoPlayer;
import is.yranac.canary.media.PlayerVideoInitializer;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.util.Utils;

import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_DEVICE_PLACEMENT;

public class PlaceCanaryFragment extends SetUpBaseFragment implements View.OnClickListener {

    private static final String LOG_TAG = "PlaceCanaryFragment";
    private CanaryVideoPlayer player;

    private FragmentSetupPlaceCanaryBinding binding;
    private int deviceType;
    private List<String> videos = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetupPlaceCanaryBinding.inflate(inflater);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        deviceType = getArguments().getInt(device_type, 0);

        binding.nextBtn.setOnClickListener(this);

        String uri;
        switch (deviceType) {
            case DeviceType.CANARY_AIO:
                uri = "asset:///plug_aio.mp4";
                binding.plugInTextView.setText(R.string.plug_in_canary_one);
                break;
            case DeviceType.FLEX:
                uri = "asset:///plug_flex.mp4";
                binding.plugInTextView.setText(R.string.plug_in_flex);
                break;
            case DeviceType.CANARY_VIEW:
                uri = "asset:///plug_view.mp4";
                binding.plugInTextView.setText(R.string.plug_in_canary_view);
                break;
            default:
                return;

        }

        videos.add(uri);
        initVideoPLayer();


    }

    private void initVideoPLayer() {

        binding.textureView.setVisibility(View.VISIBLE);
        player = new CanaryVideoPlayer(getContext(), CanaryVideoPlayer.VideoType.VideoTypeMP4);
        binding.textureView.setSurfaceTextureListener(new PlayerVideoInitializer(player, videos.get(0), binding.textureView));
    }


    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.showLogoutButton(false);
        fragmentStack.showRightButton(0);
        fragmentStack.enableRightButton(this, false);

        switch (deviceType) {
            case DeviceType.CANARY_AIO:
                fragmentStack.setHeaderTitle(R.string.canary);
                break;
            case DeviceType.FLEX:
                fragmentStack.setHeaderTitle(R.string.canary_flex);
                break;
            case DeviceType.CANARY_VIEW:
                fragmentStack.setHeaderTitle(R.string.canary_view);
                break;
        }

    }

    @Override
    public void onRightButtonClick() {
    }


    @Override
    protected String getAnalyticsTag() {
        return SCREEN_DEVICE_PLACEMENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null)
            player.release();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_btn:
                addFragmentToStack(getInstance(FindCanariesFragment.class), Utils.SLIDE_FROM_RIGHT);

                break;
        }
    }
}
