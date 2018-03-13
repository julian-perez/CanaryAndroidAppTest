package is.yranac.canary.messages;

import java.util.List;

/**
 * Created by Schroeder on 1/13/16.
 */
public class Tags {

    public List<String> defaultTags;
    public List<String> userTags;

    public Tags(List<String> defaultTags, List<String> userTags) {
        this.defaultTags = defaultTags;
        this.userTags = userTags;
    }
}
