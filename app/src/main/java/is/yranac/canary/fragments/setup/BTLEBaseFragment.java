package is.yranac.canary.fragments.setup;

import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.fragments.settings.EditDeviceFragment;
import is.yranac.canary.nativelibs.models.messages.BLEError;
import is.yranac.canary.services.bluetooth.BluetoothSingleton;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.ga.AnalyticsConstants;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;

/**
 * Created by sergeymorozov on 12/15/15.
 */
public abstract class BTLEBaseFragment extends SetUpBaseFragment {

    protected AlertDialog alertDialog;

    private static final String LOG_TAG = "BTLEBaseFragment";


    @Override
    public void onStart() {
        super.onStart();

        TinyMessageBus.register(this);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onStop() {
        super.onStop();

        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        TinyMessageBus.unregister(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    @Subscribe
    public void onError(BLEError error) {
        GoogleAnalyticsHelper.trackBluetoothEvent(changingWifi(), AnalyticsConstants.ACTION_BLUETOOTH_FAILURE, error.getError().toString());
        TinyMessageBus.cancel(BLEError.class);
        String header;
        String dsc;
        String leftBtn = getString(R.string.get_help);
        String rightBtn = getString(R.string.try_again);

        switch (error.getError()) {
            case BLEK_KEY:
                header = getString(R.string.device_activation_failed);
                dsc = getString(R.string.try_again_or_contact_support);
                break;
            case APP_CLOSE:
                header = getString(R.string.unable_to_connect);
                dsc = getString(R.string.please_dont_close_app);
                break;
            case DISCONNECT:
                header = getString(R.string.device_disconnected);
                dsc = getString(R.string.check_bluetooth_try_again);
                break;
            case PAIR_FAIL:
                header = getString(R.string.unable_to_pair);
                dsc = getString(R.string.check_bluetooth_try_again);
                break;
            default:
                return;
        }


        showLoading(false, null);
        BluetoothSingleton.reset();


        AlertUtils.showGenericAlert(getActivity(), header, dsc, 0,
                leftBtn, rightBtn, 0, 0, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GetHelpFragment fragment = GetHelpFragment.newInstance(GetHelpFragment.GetHelpType.GET_HELP_TYPE_BLUETOOTH);
                        addModalFragment(fragment);

                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isSetup()) {
                            fragmentStack.popBackStack(EditDeviceFragment.class);
                        } else {
                            fragmentStack.popBackStack(FindCanariesFragment.class);

                        }
                    }
                }

        );

    }

}
