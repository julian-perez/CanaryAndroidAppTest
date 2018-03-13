package is.yranac.canary.model;

import org.junit.Assert;
import org.junit.Test;

import is.yranac.canary.Constants;
import is.yranac.canary.model.entry.Entry;
import is.yranac.canary.util.Utils;

/**
 * Created by michaelschroeder on 1/3/17.
 */

public class EntryUnitTests {

    @Test
    public void testHasDetailView() {
        Entry entry = new Entry();

        entry.entryType = Entry.ENTRY_TYPE_MOTION;
        Assert.assertTrue(entry.hasDetailView());

        entry.entryType = Entry.ENTRY_TYPE_LIVE;
        Assert.assertTrue(entry.hasDetailView());

        entry.entryType = Entry.ENTRY_TYPE_CONNECT;
        Assert.assertFalse(entry.hasDetailView());

        entry.entryType = Entry.ENTRY_TYPE_DISCONNECT;
        Assert.assertFalse(entry.hasDetailView());

        entry.entryType = Entry.ENTRY_TYPE_HUMIDITY;
        Assert.assertFalse(entry.hasDetailView());

        entry.entryType = Entry.ENTRY_TYPE_AIR_QUALITY;
        Assert.assertFalse(entry.hasDetailView());

        entry.entryType = Entry.ENTRY_TYPE_TEMPERATURE;
        Assert.assertFalse(entry.hasDetailView());

        entry.entryType = Entry.ENTRY_TYPE_SIREN;
        Assert.assertFalse(entry.hasDetailView());

        entry.entryType = Entry.ENTRY_TYPE_OTA;
        Assert.assertFalse(entry.hasDetailView());

        entry.entryType = Entry.ENTRY_TYPE_LOCATION;
        Assert.assertFalse(entry.hasDetailView());

        entry.entryType = Entry.ENTRY_TYPE_MODE;
        Assert.assertFalse(entry.hasDetailView());
    }

    @Test
    public void testIsHomeHealthNotificationEntry() {
        Entry entry = new Entry();

        entry.entryType = Entry.ENTRY_TYPE_HUMIDITY;
        Assert.assertTrue(entry.isHomeHealthNotificationEntry());

        entry.entryType = Entry.ENTRY_TYPE_AIR_QUALITY;
        Assert.assertTrue(entry.isHomeHealthNotificationEntry());

        entry.entryType = Entry.ENTRY_TYPE_TEMPERATURE;
        Assert.assertTrue(entry.isHomeHealthNotificationEntry());

        entry.entryType = Entry.ENTRY_TYPE_MOTION;
        Assert.assertFalse(entry.isHomeHealthNotificationEntry());

        entry.entryType = Entry.ENTRY_TYPE_LIVE;
        Assert.assertFalse(entry.isHomeHealthNotificationEntry());

        entry.entryType = Entry.ENTRY_TYPE_CONNECT;
        Assert.assertFalse(entry.isHomeHealthNotificationEntry());

        entry.entryType = Entry.ENTRY_TYPE_DISCONNECT;
        Assert.assertFalse(entry.isHomeHealthNotificationEntry());

        entry.entryType = Entry.ENTRY_TYPE_SIREN;
        Assert.assertFalse(entry.isHomeHealthNotificationEntry());

        entry.entryType = Entry.ENTRY_TYPE_OTA;
        Assert.assertFalse(entry.isHomeHealthNotificationEntry());

        entry.entryType = Entry.ENTRY_TYPE_LOCATION;
        Assert.assertFalse(entry.isHomeHealthNotificationEntry());

        entry.entryType = Entry.ENTRY_TYPE_MODE;
        Assert.assertFalse(entry.isHomeHealthNotificationEntry());
    }

    @Test
    public void testLocationId() {
        Entry entry = new Entry();

        entry.id = 1;
        entry.locationUri = Utils.buildResourceUri(Constants.ENTRIES_URI, entry.id);

        int entryId = entry.getLocationId();

        Assert.assertTrue(entry.id == entryId);

    }

}
