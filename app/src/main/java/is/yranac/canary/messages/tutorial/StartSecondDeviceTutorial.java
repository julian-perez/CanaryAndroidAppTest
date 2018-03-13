package is.yranac.canary.messages.tutorial;

import is.yranac.canary.model.tutorial.TutorialDetails;

/**
 * Created by sergeymorozov on 8/18/16.
 */
public class StartSecondDeviceTutorial {
    private TutorialDetails tutorialDetails;

    public StartSecondDeviceTutorial(TutorialDetails positionToStart) {
        this.tutorialDetails = positionToStart;
    }

    public TutorialDetails getTutorialDetails() {
        return tutorialDetails;
    }

    @Override
    public String toString() {
        return String.format("StartSecondDeviceTutorial: tutorialType: %s; pageToStart: %d; isMultiDeviceTypeTutorial: %b", this.tutorialDetails.tutorialType, this.tutorialDetails.pageToStart, tutorialDetails.isMultiDeviceTypeTutorial);

    }
}
