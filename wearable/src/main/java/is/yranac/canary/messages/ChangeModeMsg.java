package is.yranac.canary.messages;

/**
 * Created by narendramanoharan on 7/6/16.
 */
public class ChangeModeMsg {

    String modeURI;
    public ChangeModeMsg(String modeURI) {
        this.modeURI = modeURI;
    }
    public String getModeURI() {
        return modeURI;
    }
}
