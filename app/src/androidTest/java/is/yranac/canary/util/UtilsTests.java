package is.yranac.canary.util;

import android.webkit.URLUtil;

import org.junit.Test;

import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.model.entry.Entry;
import is.yranac.canary.model.thumbnail.Thumbnail;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class UtilsTests {


    @Test
    public void testBuilderEntryUrl() {

        Entry entry = new Entry();

        Thumbnail thumbnail = new Thumbnail();
        thumbnail.device = "3";

        String testEntryUrl = UrlUtils.buildEntryDeviceClipUrl(entry, thumbnail.device, thumbnail);

        assertTrue(URLUtil.isValidUrl(testEntryUrl));

    }

    @Test
    public void testBuildStreamingUrl() {
        Device device = new Device();

        String testEntryUri = UrlUtils.buildStreamingUrl(device);

        assertTrue(URLUtil.isValidUrl(testEntryUri));

    }

    @Test
    public void testPassword() {
        String email = "test@test.com";
        String validPassword = "test1234";
        String inValidPassword1 = "test1";
        String inValidPassword2 = "thisistesging";

        assertTrue(Utils.checkPasswordLength(validPassword));
        assertTrue(Utils.checkPasswordCharacters(validPassword));
        assertFalse(Utils.passwordUsernameMatch(email, validPassword));

        assertFalse(Utils.checkPasswordLength(inValidPassword1));
        assertTrue(Utils.checkPasswordCharacters(inValidPassword1));
        assertFalse(Utils.passwordUsernameMatch(email, inValidPassword1));

        assertTrue(Utils.checkPasswordLength(inValidPassword2));
        assertFalse(Utils.checkPasswordCharacters(inValidPassword2));
        assertFalse(Utils.passwordUsernameMatch(email, inValidPassword2));

        assertTrue(Utils.checkPasswordLength(email));
        assertTrue(Utils.checkPasswordCharacters(email));
        assertTrue(Utils.passwordUsernameMatch(email, email));
    }

    @Test
    public void testGetDeviceTypeFromDeviceSerialNumber() {

        String serialNumber = "C100K1412412";

        DeviceType deviceType = Utils.getDeviceTypeFromDeviceSerialNumber(serialNumber);

        assertEquals(deviceType.id, DeviceType.CANARY_AIO);

        serialNumber = "C999K1412412";
        deviceType = Utils.getDeviceTypeFromDeviceSerialNumber(serialNumber);
        assertEquals(deviceType.id, DeviceType.CANARY_AIO);

        serialNumber = "C400K1412412";
        deviceType = Utils.getDeviceTypeFromDeviceSerialNumber(serialNumber);
        assertEquals(deviceType.id, DeviceType.CANARY_VIEW);

        serialNumber = "C499K1412412";
        deviceType = Utils.getDeviceTypeFromDeviceSerialNumber(serialNumber);
        assertEquals(deviceType.id, DeviceType.CANARY_VIEW);

        serialNumber = "C600K1412412";
        deviceType = Utils.getDeviceTypeFromDeviceSerialNumber(serialNumber);
        assertEquals(deviceType.id, DeviceType.FLEX);


        serialNumber = "C200K1412412";
        deviceType = Utils.getDeviceTypeFromDeviceSerialNumber(serialNumber);
        assertEquals(deviceType.id, DeviceType.CANARY_PLUS);


    }


}
