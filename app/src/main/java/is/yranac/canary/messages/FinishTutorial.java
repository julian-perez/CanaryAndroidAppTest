package is.yranac.canary.messages;

import is.yranac.canary.util.TutorialUtil.TutorialType;

/**
 * Created by Schroeder on 7/27/16.
 */
public class FinishTutorial {
    private TutorialType typeFinished;

    public FinishTutorial(TutorialType tutorialType) {
        this.typeFinished = tutorialType;
    }

    public TutorialType getTypeFinished() {
        if (typeFinished == null)
            return TutorialType.NONE;
        return typeFinished;
    }
}
