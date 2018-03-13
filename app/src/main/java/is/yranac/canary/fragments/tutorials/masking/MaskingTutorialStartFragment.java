package is.yranac.canary.fragments.tutorials.masking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import is.yranac.canary.databinding.FragmentTutorialMaskingStartBinding;
import is.yranac.canary.fragments.StackFragment;
import is.yranac.canary.fragments.settings.SettingsFragment;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;

import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_MASKING_ONBOARDING_CANCELLED;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_MASKING_ONBOARDING_START;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_MASKING_ONBOARDING;

/**
 * Created by michaelschroeder on 2/16/17.
 */

public class MaskingTutorialStartFragment extends SettingsFragment {


    public static MaskingTutorialStartFragment newInstance(int locationId) {
        MaskingTutorialStartFragment fragment = new MaskingTutorialStartFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("locationId", locationId);
        fragment.setArguments(bundle);
        return fragment;
    }

    private int locationId;

    private FragmentTutorialMaskingStartBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTutorialMaskingStartBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        locationId = getArguments().getInt("locationId");

        GoogleAnalyticsHelper.trackEvent(CATEGORY_MASKING_ONBOARDING, ACTION_MASKING_ONBOARDING_START,
                null, null, locationId, 0);

        final WeakReference<StackFragment> stackFragment = new WeakReference<StackFragment>(this);
        binding.startTutorialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MotionSensitivityInitializeFragment fragment = MotionSensitivityInitializeFragment.newInstance(locationId);
                stackFragment.get().addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
            }
        });

        binding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleAnalyticsHelper.trackEvent(CATEGORY_MASKING_ONBOARDING, ACTION_MASKING_ONBOARDING_CANCELLED,
                        null, null, locationId, 0);
                stackFragment.get().getActivity().finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.showHeader(false);
    }

    @Override
    public void onRightButtonClick() {

    }

    @Override
    protected String getAnalyticsTag() {
        return null;
    }
}
