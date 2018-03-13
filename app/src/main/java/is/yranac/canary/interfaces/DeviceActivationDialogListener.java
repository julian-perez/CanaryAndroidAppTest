package is.yranac.canary.interfaces;

/**
 * Created by sergeymorozov on 11/11/15.
 */
public interface DeviceActivationDialogListener {

    void onSubmit(String password);
    void resetPassword();
}
