package is.yranac.canary.exceptions;

import is.yranac.canary.util.StringUtils;

/**
 * Created by sergeymorozov on 10/20/15.
 */
public class ChirpException extends Exception{
    int errorCode;
    String customMessage;
    public ChirpException(int errorCode, String customMessage){
        this.errorCode = errorCode;
        this.customMessage = customMessage;
    }

    @Override
    public String getMessage(){
        return getErrorMessage();
    }

    private String getErrorMessage(){
        String msg;
        switch (errorCode){
            case -1: msg = "NULL/invalid context"; break;
            case -2: msg = "Chirp message length greater than CHIRP_MAX_MESSAGE"; break;
            case -3: msg = "invalid message type"; break;
            case -4: msg = "message queue full (e.g. prior chirpTx() not yet done)"; break;
            case -5: msg = "NULL tx buffer with non-zero length"; break;
            case -6: msg = "unable to convert json String to UTF-* byte array"; break;
            case -7: msg = "Error with ecrypting or decrypting"; break;
            default: msg = "Unknown Chirp error"; break;
        }
        return StringUtils.isNullOrEmpty(customMessage) ? msg : msg + "\n" + customMessage;
    }
}
