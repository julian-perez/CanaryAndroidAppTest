package is.yranac.canary.messages;

import retrofit.RetrofitError;
import retrofit.mime.TypedByteArray;

/**
 * Created by sergeymorozov on 11/13/15.
 */
public class DeviceActivationError {
    public RetrofitError error;

    public DeviceActivationError(RetrofitError error){
        this.error = error;
    }

    public String getErrorMessage(){
        if(error == null || error.getResponse() == null)
            return null;
        return new String(((TypedByteArray)error.getResponse().getBody()).getBytes());
    }
}
