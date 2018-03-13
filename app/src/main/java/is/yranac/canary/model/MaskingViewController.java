package is.yranac.canary.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.databinding.library.baseAdapters.BR;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.CanaryApplication;
import is.yranac.canary.R;
import is.yranac.canary.messages.DeviceMaskCount;
import is.yranac.canary.messages.RefreshOptionButtons;
import is.yranac.canary.messages.ShowGetMaskingHelp;
import is.yranac.canary.messages.SwitchUserModeRequest;
import is.yranac.canary.messages.masking.AddNewMask;
import is.yranac.canary.messages.masking.DeleteMaskRequest;
import is.yranac.canary.messages.masking.SaveMasksRequest;
import is.yranac.canary.messages.masking.ShowMessageRequest;
import is.yranac.canary.ui.views.masking.MaskDrawingView;
import is.yranac.canary.util.TinyMessageBus;


/**
 * Created by sergeymorozov on 11/28/16.
 */

public class MaskingViewController extends BaseObservable {

    private static final String LOG_TAG = "MaskingViewController";
    private static Animation animSlideInBottom;
    private static Animation animSlideInTop;
    private static Animation animSlideOutBottom;
    private static Animation animSlideOutTop;

    private String topMessage;
    private boolean addEnabled;
    private boolean deleteEnabled;
    private boolean saveEnabled;
    private boolean showOptions;
    private UserMode userMode;
    private boolean multipleMasks;

    public MaskingViewController() {
        this.topMessage = null;
        this.addEnabled = true;
        this.deleteEnabled = true;
        this.saveEnabled = false;
        this.showOptions = true;
    }

    public enum UserMode {
        HIDE_OPTIONS,
        SHOW_OPTIONS,
        TOGGLE
    }

    @BindingAdapter("slideVisible")
    public static void setSlideVisible(final View view, final boolean show) {
        if (view.getTag() == null) {
            //first time, no need to animate
            view.setTag(true);
            view.setVisibility(show ? View.VISIBLE : View.GONE);
            return;
        }

        //if we are already showing/hiding the view, no need to do it again
        if (show == (view.getVisibility() == View.VISIBLE))
            return;

        Animation animation;

        switch (view.getId()) {
            case R.id.bottom_options:
                animation = show ? getAnimSlideInBottom() : getAnimSlideOutBottom();
                break;
            case R.id.masking_top_message_layout:
                animation = show ? getAnimSlideInTop() : getAnimSlideOutTop();
                break;
            default:
                return;
        }

        view.animate().cancel();

        if (show)
            view.setVisibility(View.VISIBLE);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!show) {
                    view.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(animation);
    }


    @Bindable
    public boolean isShowOptions() {
        return showOptions;
    }

    public void setShowOptions(boolean showOptions) {
        if (this.showOptions == showOptions)
            return;

        this.showOptions = showOptions;
        notifyPropertyChanged(BR.showOptions);
    }

    @Bindable
    public String getTopMessage() {
        return topMessage;
    }

    public void setTopMessage(String topMessage) {
        this.topMessage = topMessage;
        notifyPropertyChanged(BR.topMessage);
    }

    @Bindable
    public boolean isAddEnabled() {
        return addEnabled;
    }

    public void setAddEnabled(boolean addEnabled) {
        this.addEnabled = addEnabled;
        notifyPropertyChanged(BR.addEnabled);
    }

    @Bindable
    public boolean isDeleteEnabled() {
        return deleteEnabled;
    }

    public void setDeleteEnabled(boolean deleteEnabled) {
        this.deleteEnabled = deleteEnabled;
        notifyPropertyChanged(BR.deleteEnabled);
    }

    @Bindable
    public boolean isSaveEnabled() {
        return saveEnabled;
    }

    @Bindable
    public boolean isMultipleMasks() {
        return multipleMasks;
    }

    public void setSaveEnabled(boolean saveEnabled) {
        this.saveEnabled = saveEnabled;
        notifyPropertyChanged(BR.saveEnabled);
    }

    @Subscribe
    public void switchMode(SwitchUserModeRequest request) {
        UserMode modeNeeded = request.requestedMode;

        if (getUserMode() == modeNeeded)
            return;

        switch (modeNeeded) {
            case HIDE_OPTIONS:
                this.setShowOptions(false);
                break;
            case SHOW_OPTIONS:
                this.setShowOptions(true);
                break;
        }

        this.userMode = modeNeeded;
    }

    @Subscribe
    public void refreshButtons(RefreshOptionButtons request) {
        if (request == null)
            return;

        if (request.enableDelete != null)
            this.setDeleteEnabled(request.enableDelete);
        if (request.enableAdd != null)
            this.setAddEnabled(request.enableAdd);
        if (request.enableSave != null)
            this.setSaveEnabled(request.enableSave);
    }

    @Subscribe
    public void showMessage(ShowMessageRequest request) {

        this.setTopMessage(request.messageToShow);
    }

    @Subscribe
    public void deviceMaskCount(DeviceMaskCount deviceMaskCount) {
        multipleMasks = deviceMaskCount.maskCount > 1;
        notifyPropertyChanged(BR.multipleMasks);

    }

    public void register(MaskDrawingView maskDrawingView) {
        TinyMessageBus.register(this);
        if (maskDrawingView != null)
            TinyMessageBus.register(maskDrawingView);
    }

    public void unregister(MaskDrawingView maskDrawingView) {
        TinyMessageBus.unregister(this);
        if (maskDrawingView != null)
            TinyMessageBus.unregister(maskDrawingView);
    }

    public void getHelp(View viewClicked) {
        TinyMessageBus.post(new ShowGetMaskingHelp());
    }


    public void optionsClicked(View viewClicked) {
        switch (viewClicked.getId()) {
            case R.id.bottom_options_add:
                if (!this.isAddEnabled())
                    return;

                TinyMessageBus.post(new AddNewMask());
                break;
            case R.id.bottom_options_delete:
                if (!this.isDeleteEnabled())
                    return;

                TinyMessageBus.post(new DeleteMaskRequest());
                break;
            case R.id.bottom_options_save:
                if (!this.isSaveEnabled())
                    return;

                TinyMessageBus.post(new SaveMasksRequest());
                break;
        }
    }

    private UserMode getUserMode() {
        if (this.userMode == null)
            this.userMode = UserMode.SHOW_OPTIONS;
        return this.userMode;
    }

    private static Animation getAnimSlideInBottom() {
        if (animSlideInBottom == null)
            animSlideInBottom =
                    AnimationUtils.loadAnimation(
                            CanaryApplication.getContext(), R.anim.slide_in_bottom);

        return animSlideInBottom;
    }

    private static Animation getAnimSlideInTop() {
        if (animSlideInTop == null)
            animSlideInTop =
                    AnimationUtils.loadAnimation(
                            CanaryApplication.getContext(), R.anim.slide_in_top);

        return animSlideInTop;
    }

    private static Animation getAnimSlideOutBottom() {
        if (animSlideOutBottom == null)
            animSlideOutBottom =
                    AnimationUtils.loadAnimation(
                            CanaryApplication.getContext(), R.anim.slide_out_bottom);

        return animSlideOutBottom;
    }

    private static Animation getAnimSlideOutTop() {
        if (animSlideOutTop == null)
            animSlideOutTop =
                    AnimationUtils.loadAnimation(
                            CanaryApplication.getContext(), R.anim.slide_out_top);

        return animSlideOutTop;
    }
}