package is.yranac.canary.util.cache.location;

import java.util.List;

import is.yranac.canary.model.location.Location;

/**
 * Created by michaelschroeder on 6/1/17.
 */

public class GotLocations {

    public List<Location> locations;
    public Location currentLocation;

    public GotLocations(List<Location> locations, Location currentLocation){
        this.currentLocation = currentLocation;
        this.locations = locations;
    }
}
