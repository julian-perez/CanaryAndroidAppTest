package is.yranac.canary.fragments.tutorials.masking;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import is.yranac.canary.databinding.FragmentTutorialMaskOverBinding;
import is.yranac.canary.fragments.settings.SettingsFragment;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;

import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_MASKING_ONBOARDING_COMPLETED;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_MASKING_ONBOARDING;

/**
 * Created by michaelschroeder on 2/17/17.
 */

public class CVTutorialOverFragment extends SettingsFragment {


    public static CVTutorialOverFragment newInstance(int locationId) {
        CVTutorialOverFragment fragment = new CVTutorialOverFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("locationId", locationId);
        fragment.setArguments(bundle);
        return fragment;
    }


    private FragmentTutorialMaskOverBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTutorialMaskOverBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final WeakReference<FragmentActivity> activityWeakReference = new WeakReference<>(getActivity());

        final int locationId = getArguments().getInt("locationId");

        binding.endTutorialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity fragmentActivity = activityWeakReference.get();
                if (fragmentActivity != null) {
                    fragmentActivity.finish();
                }

                GoogleAnalyticsHelper.trackEvent(CATEGORY_MASKING_ONBOARDING, ACTION_MASKING_ONBOARDING_COMPLETED,
                        null, null, locationId, 0);
            }
        });

    }

    @Override
    public void onRightButtonClick() {

    }

    @Override
    protected String getAnalyticsTag() {
        return null;
    }
}