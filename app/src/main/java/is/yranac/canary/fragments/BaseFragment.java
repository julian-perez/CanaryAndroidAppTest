package is.yranac.canary.fragments;

import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;

/**
 * Created by Schroeder on 9/17/14.
 */
public abstract class BaseFragment extends Fragment {
    /**
     * Return the Screen tag for google Analytics
     *
     * @return Null if no tagging otherwise the corresponding tag
     */
    protected abstract String getAnalyticsTag();


    protected AlertDialog mDialog;

    @Override
    public void onStart() {
        super.onStart();

        // Set screen name.
        // Where path is a String representing the screen name.
        String path = getAnalyticsTag();
        if (path == null)
            return;

        GoogleAnalyticsHelper.trackScreenEvent(path);

    }

    @Override
    public void onPause() {
        super.onPause();
        showLoading(false, null);
    }

    protected void showLoading(boolean showLoading, String message) {
        showLoading(showLoading, message, true);
    }

    protected void showLoading(boolean showLoading, String message, boolean cancelable) {

        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }

        if (!showLoading)
            return;

        mDialog = AlertUtils.initLoadingDialog(getActivity(), message, cancelable);

        if (mDialog == null)
            return;

        mDialog.show();
    }
}
