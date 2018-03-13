package is.yranac.canary.nativelibs.models.messages;

/**
 * Created by sergeymorozov on 10/28/15.
 */
public class BLEMessage {
    String jsonString;

    public BLEMessage(String jsonString){
        this.jsonString = jsonString;
    }

    public String getJsonString(){
        return this.jsonString;
    }
}
