package is.yranac.canary.model.customer;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 8/11/14.
 */
public class CustomerChangePassword {
    @SerializedName("action")
    public String action;

    @SerializedName("old_password")
    public String oldPassword;

    @SerializedName("new_password1")
    public String newPassword1;

    @SerializedName("new_password2")
    public String newPassword2;

    public CustomerChangePassword(String newPassword, String oldPassword) {
        this.action = "change_password";
        this.newPassword1 = newPassword;
        this.newPassword2 = newPassword;
        this.oldPassword = oldPassword;
    }

}
