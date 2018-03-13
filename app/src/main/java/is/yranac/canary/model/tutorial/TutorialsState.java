package is.yranac.canary.model.tutorial;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sergeymorozov on 7/12/16.
 */
public class TutorialsState {

    @SerializedName("didCompleteHomeTutorial")
    public boolean didCompleteHomeTutorial;

    @SerializedName("didCompleteHomeForSecondDeviceTutorial")
    public boolean didCompleteHomeForSecondDeviceTutorial;

    @SerializedName("didCompleteTimelineTutorial")
    public boolean didCompleteTimelineTutorial;

    @SerializedName("didCompleteTimelineFilterTutorial")
    public boolean didCompleteTimelineFilterTutorial;

    @SerializedName("didCompleteSingleEntryMoreOptionsTutorial")
    public boolean didCompleteSingleEntryMoreOptionsTutorial;

    @SerializedName("didCompleteSecondDeviceTutorial")
    public boolean didCompleteSecondDeviceTutorial;

    @SerializedName("currentHomeStep")
    public int currentHomeStep;

    @SerializedName("startDeviceIndex")
    public Integer startDeviceIndex;
}
