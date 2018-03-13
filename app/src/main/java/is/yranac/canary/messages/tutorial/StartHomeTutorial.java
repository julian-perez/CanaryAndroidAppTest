package is.yranac.canary.messages.tutorial;

import com.google.gson.annotations.SerializedName;

import is.yranac.canary.model.tutorial.TutorialDetails;

/**
 * Created by sergeymorozov on 8/18/16.
 */
public class StartHomeTutorial {
    @SerializedName("tutorialDetails")
    public TutorialDetails tutorialDetails;

    public StartHomeTutorial(TutorialDetails tutorialType) {
        this.tutorialDetails = tutorialType;
    }

    public TutorialDetails getTutorialDetails() {
        return tutorialDetails;
    }
}
