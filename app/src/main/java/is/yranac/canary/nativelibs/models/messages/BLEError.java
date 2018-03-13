package is.yranac.canary.nativelibs.models.messages;

/**
 * Created by sergeymorozov on 10/26/15.
 */
public class BLEError {

    private String customMessage;
    private  Exception exception;
    private ERROR error;

    public BLEError(String customMessage, Exception exception){
        this.customMessage = customMessage;
        this.exception = exception;
    }

    public BLEError(ERROR error, String customMessage, Exception exception){
        this.customMessage = customMessage;
        this.exception = exception;
        this.error = error;
    }

    public BLEError(ERROR error) {
        this.error = error;
    }

    public String getErrorMessage(){
        String msg = customMessage;
        if(exception != null)
            msg += " Exception was thrown, with this message: \n" + exception.getMessage();
        return msg;
    }

    public ERROR getError(){
        if ( error == null){
            return ERROR.DEFAULT;
        }
        return error;
    }

    public enum ERROR{
        BLEK_KEY,
        APP_CLOSE,
        DISCONNECT,
        PAIR_FAIL,
        TIMEOUT,
        DEFAULT
    }

}
