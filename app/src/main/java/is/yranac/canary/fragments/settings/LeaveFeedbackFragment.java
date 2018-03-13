package is.yranac.canary.fragments.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentWebViewBinding;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;

import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_CONTRACT_SUPPORT;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_HELP;

public class LeaveFeedbackFragment extends SettingsFragment {


    private FragmentWebViewBinding fragmentWebViewBinding;
    private boolean showHeader = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentWebViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_web_view, container, false);
        return fragmentWebViewBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();

        GoogleAnalyticsHelper.trackEvent(CATEGORY_HELP, ACTION_CONTRACT_SUPPORT);
        if (args != null) {
            showHeader = args.getBoolean("show_header");
        }
        if (showHeader) {
            fragmentWebViewBinding.xButton.setVisibility(View.GONE);
        } else {
            fragmentWebViewBinding.header.setVisibility(View.GONE);
        }
        WebSettings wbset = fragmentWebViewBinding.webview.getSettings();
        wbset.setJavaScriptEnabled(true);
        wbset.setAppCacheEnabled(false);
        wbset.setDomStorageEnabled(true);
        fragmentWebViewBinding.webview.setWebViewClient(new WebViewClient());
        String url = Constants.CANARY_HELP();
        fragmentWebViewBinding.webview.loadUrl(url);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!showHeader) {
            fragmentStack.showHeader(true);
            fragmentStack.enableRightButton(this, false);
            fragmentStack.showRightButton(0);
            fragmentStack.setHeaderTitle(R.string.get_help);
        }
    }

    @Override
    public void onRightButtonClick() {
        hideSoftKeyboard();
    }

    @Override
    protected String getAnalyticsTag() {
        return null;
    }

    public static Fragment newInstance(boolean b) {
        LeaveFeedbackFragment feedbackFragment = new LeaveFeedbackFragment();
        Bundle args = new Bundle();
        args.putBoolean("show_header", b);
        feedbackFragment.setArguments(args);
        return feedbackFragment;
    }
}
