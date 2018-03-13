package is.yranac.canary.model.tutorial;

import com.google.gson.annotations.SerializedName;

import is.yranac.canary.util.TutorialUtil.TutorialType;

/**
 * Created by sergeymorozov on 9/19/16.
 */
public class TutorialDetails {
    @SerializedName("tutorialType")
    public TutorialType tutorialType;
    @SerializedName("pageToStart")
    public int pageToStart;
    @SerializedName("isMultiDeviceTypeTutorial")
    public boolean isMultiDeviceTypeTutorial;
    @SerializedName("totalNumOfActivatedDvices")
    public int totalNumOfActivatedDvices;

    public TutorialDetails(int pageToStart, TutorialType tutorialType, boolean isMultiDeviceTutorial, int tototalNumOfActivatedDvices) {
        this.tutorialType = tutorialType;
        this.pageToStart = pageToStart;
        this.isMultiDeviceTypeTutorial = isMultiDeviceTutorial;
        this.totalNumOfActivatedDvices = tototalNumOfActivatedDvices;
    }

    public TutorialType getTutorialType() {
        return tutorialType;
    }

    public int getPageToStart() {
        return pageToStart;
    }

    public boolean isMultiDeviceTypeTutorial() {
        return isMultiDeviceTypeTutorial;
    }

    public void setPageToStart(int pageToStart) {
        this.pageToStart = pageToStart;
    }
}

