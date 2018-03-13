package is.yranac.canary.messages;

import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.thumbnail.Thumbnail;

/**
 * Created by sergeymorozov on 11/16/15.
 */
public class GotCachedDeviceAndLocationOwner {
    private Device device;
    private Customer locationOwner;
    private boolean isCurrentUserLocationOwner;
    private Thumbnail thumbnail;
    private int locationID;
    private int totalNumberDevices;


    public GotCachedDeviceAndLocationOwner(Device device, Customer locationOwner, boolean isOwner,
                                           Thumbnail thumbnail, int locationID,
                                           int totalNumberOfDevices) {
        this.device = device;
        this.locationOwner = locationOwner;
        this.isCurrentUserLocationOwner = isOwner;
        this.thumbnail = thumbnail;
        this.locationID = locationID;
        this.totalNumberDevices = totalNumberOfDevices;
    }

    public boolean isCurrentUserLocationOwner() {
        return isCurrentUserLocationOwner;
    }

    public Customer getLocationOwner() {
        return locationOwner;
    }

    public Device getDevice() {
        return device;
    }

    public Thumbnail getThumbnail() {
        return this.thumbnail;
    }

    public int getLocationID() {
        return this.locationID;
    }


    public int getTotalNumberDevices() {
        return totalNumberDevices;
    }
}
