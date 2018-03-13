package is.yranac.canary.util.cache.location;

import android.content.Context;

/**
 * Created by michaelschroeder on 1/17/18.
 */

public class UpdateMembershipPresenceCache {
    public final Context context;
    public final int locationId;
    public final int customerId;
    public final boolean presence;

    public UpdateMembershipPresenceCache(Context context, int locationId, int customerId, boolean presence) {
        this.context = context;
        this.locationId = locationId;
        this.customerId = customerId;
        this.presence = presence;
    }
}
