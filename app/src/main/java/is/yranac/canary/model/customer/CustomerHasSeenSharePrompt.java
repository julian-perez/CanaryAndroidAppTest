package is.yranac.canary.model.customer;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 6/2/16.
 */
public class CustomerHasSeenSharePrompt {

    @SerializedName("has_seen_data_share_prompt")
    public boolean hasSeenSharePrompt;

    public CustomerHasSeenSharePrompt(boolean hasSeenSharePrompt) {
        this.hasSeenSharePrompt = hasSeenSharePrompt;
    }
}
