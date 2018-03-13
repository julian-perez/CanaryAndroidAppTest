package is.yranac.canary.model.comment;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 8/11/14.
 */
public class CommentCreate {

    public CommentCreate(String body, String entryUri){
        this.body = body;
        this.entryUri = entryUri;
    }

    @SerializedName("body")
    public String body;

    @SerializedName("entry")
    public String entryUri;
}
