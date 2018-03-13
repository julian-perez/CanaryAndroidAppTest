package is.yranac.canary.fragments.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSettingsGetHelpBinding;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.TutorialUtil;
import is.yranac.canary.util.TutorialUtil.TutorialType;
import is.yranac.canary.util.ZendeskUtil;

import static is.yranac.canary.util.TutorialUtil.TutorialType.ENTRY_MORE_OPTIONS;
import static is.yranac.canary.util.TutorialUtil.TutorialType.TIMELINE;
import static is.yranac.canary.util.TutorialUtil.TutorialType.TIMELINE_FILTER;

/**
 * Created by Schroeder on 4/1/15.
 */
public class GetHelpFragment extends SettingsFragment implements View.OnClickListener {


    private FragmentSettingsGetHelpBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsGetHelpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.helpCenter.setOnClickListener(this);
        binding.contactSupport.setOnClickListener(this);
        binding.restartHomeTutorial.setOnClickListener(this);
        binding.restartTimelineTutorial.setOnClickListener(this);

    }


    @Override
    public void onResume() {
        super.onResume();
        TinyMessageBus
                .register(this);
        fragmentStack.showLogoutButton(false);
        fragmentStack.showHeader(true);
        fragmentStack.setHeaderTitle(R.string.get_help);
        fragmentStack.showRightButton(0);
    }


    @Override
    protected String getAnalyticsTag() {
        return null;
    }

    @Override
    public void onRightButtonClick() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.help_center:
                String url = Constants.CANARY_HELP();
                ZendeskUtil.loadHelpCenter(getContext(), url);
                break;
            case R.id.contact_support:
                ZendeskUtil.showZendesk(getActivity(), 0);
                break;
            case R.id.restart_home_tutorial:
                TutorialUtil.queueTutorial(TutorialType.HOME, true);
                getActivity().finish();
                break;
            case R.id.restart_timeline_tutorial:
                TutorialUtil.queueTutorials(new TutorialType[]{
                        TIMELINE,
                        TIMELINE_FILTER,
                        ENTRY_MORE_OPTIONS
                }, true);
                getActivity().finish();
                break;
        }
    }
}
