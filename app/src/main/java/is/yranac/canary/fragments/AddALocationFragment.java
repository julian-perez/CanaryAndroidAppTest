package is.yranac.canary.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentAddALocationBinding;
import is.yranac.canary.ui.SettingsFragmentStackActivity;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;

import static is.yranac.canary.ui.SettingsFragmentStackActivity.add_a_location;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_ADD_DEVICE;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_SETTINGS;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_NEW_LOCATION;

/**
 * Created by michaelschroeder on 5/22/17.
 */

public class AddALocationFragment extends Fragment implements View.OnClickListener {

    private FragmentAddALocationBinding binding;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddALocationBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        GoogleAnalyticsHelper.trackEvent(CATEGORY_SETTINGS, ACTION_ADD_DEVICE, PROPERTY_NEW_LOCATION, null, 0, 0);
        Intent i = new Intent(getActivity(), SettingsFragmentStackActivity.class);
        i.setAction(add_a_location);
        startActivity(i);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.scale_alpha_push);
    }
}
