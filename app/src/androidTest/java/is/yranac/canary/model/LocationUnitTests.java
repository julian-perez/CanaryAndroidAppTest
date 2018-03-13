package is.yranac.canary.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

import is.yranac.canary.model.location.Location;

/**
 * Created by michaelschroeder on 1/30/18.
 */

public class LocationUnitTests {

    @Test
    public void testCreatedAfterGrandfather() {

        Location location = new Location();

        location.created = new Date();
        Assert.assertTrue(location.createdAfterGrandfather());

        location.created = new Date(0);
        Assert.assertFalse(location.createdAfterGrandfather());

        // 08/13/2017 @ 12:00am (UTC)
        location.created = new Date(1502582400000L);
        Assert.assertFalse(location.createdAfterGrandfather());

        // 08/15/2017 @ 12:00am (UTC)
        location.created = new Date(1502755200000L);
        Assert.assertTrue(location.createdAfterGrandfather());

    }
}
