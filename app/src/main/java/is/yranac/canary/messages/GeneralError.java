package is.yranac.canary.messages;

/**
 * Created by sergeymorozov on 11/16/15.
 */
public class GeneralError {
    public enum ErrorLevel{
        Minor,
        Major,
        Chritical,
        OMG
    }
    String errorMessage;
    ErrorLevel errorLevel;

    public GeneralError(ErrorLevel errorLevel, String errorMessage){
        this.errorLevel = errorLevel;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ErrorLevel getErrorLevel() {
        return errorLevel;
    }
}
