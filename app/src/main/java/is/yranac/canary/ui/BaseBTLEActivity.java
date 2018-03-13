package is.yranac.canary.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Button;
import android.widget.RelativeLayout;

import is.yranac.canary.R;
import is.yranac.canary.fragments.StackFragment;
import is.yranac.canary.fragments.setup.BTLEBaseFragment;
import is.yranac.canary.nativelibs.BTLEMessageQueue;
import is.yranac.canary.nativelibs.models.messages.BLEError;
import is.yranac.canary.services.bluetooth.BluetoothSingleton;
import is.yranac.canary.util.DensityUtil;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.Utils;

/**
 * Created by sergeymorozov on 1/6/16.
 */
public abstract class BaseBTLEActivity extends BaseActivity implements StackFragment.FragmentStack {

    private AlertDialog alertDialog = null;

    protected Button rightButton;
    protected Button leftButton;


    @Override
    public void addFragmentAndResetStack(StackFragment fragment, int animation) {

        String fragmentTag = fragment == null ? null : fragment.getClass().getSimpleName();

        Utils.addFragmentToStack(getSupportFragmentManager(), fragment, fragmentTag, animation, true);
    }

    @Override
    public void popBackStack(Class<? extends Fragment> fragmentClass) {
        enableBackButton();

        String tag = fragmentClass == null ? null : fragmentClass.getSimpleName();

        Log.i(LOG_TAG, tag);

        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            getSupportFragmentManager().popBackStack(tag, 0);
        }
    }

    @Override
    public void setButtonsHeight(int height) {

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rightButton.getLayoutParams();

        layoutParams.height = height;
        rightButton.setLayoutParams(layoutParams);

        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) leftButton.getLayoutParams();

        layoutParams2.height = height;
        leftButton.setLayoutParams(layoutParams2);
    }

    @Override
    public void setButtonsWidth(int width) {
        rightButton.setMinWidth(0);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rightButton.getLayoutParams();

        layoutParams.width = width;
        rightButton.setLayoutParams(layoutParams);


        leftButton.setMinWidth(0);
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) leftButton.getLayoutParams();

        layoutParams2.width = width;
        leftButton.setLayoutParams(layoutParams2);
    }

    @Override
    public void resetButtonStyle() {

        rightButton.setBackground(ContextCompat.getDrawable(this, R.drawable.button_header_selector));
        rightButton.setTextColor(ContextCompat.getColorStateList(this, R.color.green_action_btn_text));
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rightButton.getLayoutParams();
        layoutParams.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.height = DensityUtil.dip2px(this, 32);
        rightButton.setMinWidth(DensityUtil.dip2px(this, 55));
        rightButton.setLayoutParams(layoutParams);


        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) leftButton.getLayoutParams();
        layoutParams2.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        layoutParams2.height = DensityUtil.dip2px(this, 32);
        leftButton.setMinWidth(DensityUtil.dip2px(this, 55));

        showRightButton(0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        BTLEMessageQueue.register();
    }

    @Override
    protected void onPause() {
        super.onPause();

        BTLEMessageQueue.unregister();

        if (BluetoothSingleton.checkBleHardwareAvailable()) {
            if (BluetoothSingleton.getBluetoothHelperService().getCurrentDevice() != null) {
                resetProcess();
            }

            BluetoothSingleton.reset();
        }
    }

    private void resetProcess() {

        int index = getSupportFragmentManager().getBackStackEntryCount() - 1;

        if (index == -1)
            return;

        FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(index);
        String tag = backEntry.getName();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);

        if (fragment != null && fragment instanceof BTLEBaseFragment) {
            ((BTLEBaseFragment) fragment).onError(new BLEError(BLEError.ERROR.APP_CLOSE));
        }
    }
}
