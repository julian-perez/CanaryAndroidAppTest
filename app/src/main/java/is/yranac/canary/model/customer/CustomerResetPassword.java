package is.yranac.canary.model.customer;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 8/11/14.
 */
public class CustomerResetPassword {
    @SerializedName("action")
    public String action;

    @SerializedName("email")
    public String email;

    public CustomerResetPassword(String email) {
        this.action = "request_reset_password";
        this.email = email;
    }

}
