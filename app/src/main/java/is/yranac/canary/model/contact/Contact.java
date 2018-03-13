package is.yranac.canary.model.contact;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 8/8/14.
 */
public class Contact {

    @SerializedName("created")
    public String created;

    @SerializedName("created_by")
    public String createdBy;

    @SerializedName("email")
    public String email;

    @SerializedName("id")
    public int id;

    @SerializedName("location")
    public String location;

    @SerializedName("name")
    public String name;

    @SerializedName("notification_preference")
    public String notificationPreference;

    @SerializedName("notifications_enabled")
    public boolean notificationsEnabled;

    @SerializedName("phone")
    public String phone;

    @SerializedName("priority")
    public int priority;

    @SerializedName("resource_uri")
    public String resourceUri;

}
