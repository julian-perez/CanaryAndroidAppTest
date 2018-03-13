package is.yranac.canary.model;

import org.junit.Assert;
import org.junit.Test;

import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceType;

/**
 * Created by michaelschroeder on 11/14/16.
 */

public class DeviceUnitTests {


    @Test
    public void testHasBLTE() {


        Device device = new Device();
        device.deviceType = new DeviceType(DeviceType.FLEX);

        Assert.assertTrue(device.isBTLECompatible());
        device.deviceType = new DeviceType(DeviceType.CANARY_AIO);

        device.applicationVersion = "1.3.0";
        Assert.assertTrue(device.isBTLECompatible());

        device.applicationVersion = "1.2.0";
        Assert.assertTrue(!device.isBTLECompatible());

        device.deviceType = new DeviceType(DeviceType.CANARY_PLUS);
        Assert.assertTrue(device.isBTLECompatible());

    }


    @Test
    public void testMaskCompatible() {


        Device device = new Device();
        device.deviceType = new DeviceType(DeviceType.FLEX);

        device.applicationVersion = "1.1.0";
        Assert.assertFalse(device.isMaskCompatible());

        device.applicationVersion = "1.4.5";
        Assert.assertFalse(device.isMaskCompatible());

        device.applicationVersion = "1.4.0";
        Assert.assertFalse(device.isMaskCompatible());

        device.applicationVersion = "1.5.0";
        Assert.assertFalse(device.isMaskCompatible());

        device.deviceType = new DeviceType(DeviceType.CANARY_AIO);
        device.applicationVersion = "1.1.0";
        Assert.assertFalse(device.isMaskCompatible());

        device.applicationVersion = "1.4.0";
        Assert.assertFalse(device.isMaskCompatible());

        device.applicationVersion = "1.4.5";
        Assert.assertTrue(device.isMaskCompatible());

        device.applicationVersion = "1.5.0";
        Assert.assertTrue(device.isMaskCompatible());

        device.applicationVersion = "1.4.5-rc5";
        Assert.assertTrue(device.isMaskCompatible());


    }

    @Test
    public void testHasSiren() {
        Device device = new Device();

        device.deviceType = new DeviceType(DeviceType.CANARY_AIO);
        Assert.assertTrue(device.hasSiren());

        device.deviceType = new DeviceType(DeviceType.CANARY_PLUS);
        Assert.assertTrue(device.hasSiren());

        device.deviceType = new DeviceType(DeviceType.FLEX);
        Assert.assertTrue(!device.hasSiren());

    }

    @Test
    public void testOTAStatus() {
        Device device = new Device();

        device.ota_status = "inactive";
        Assert.assertTrue(device.isOtaing());

        device.ota_status = "downloading";
        Assert.assertTrue(device.isOtaing());

        device.ota_status = "downloaded";
        Assert.assertTrue(device.isOtaing());

        device.ota_status = "rebooting";
        Assert.assertTrue(device.isOtaing());

        device.ota_status = "verified";
        Assert.assertTrue(device.isOtaing());

        device.ota_status = "failed";
        Assert.assertTrue(device.failedOTA());

        device.ota_status = "minimal";
        Assert.assertTrue(device.failedOTA());

    }


    @Test
    public void testIsHSNDevice() {
        Device device = new Device();
        device.deviceType = new DeviceType(DeviceType.CANARY_VIEW);

        device.serialNumber = "C400J1723423";
        Assert.assertTrue(device.isHSNDevice());

        device.serialNumber = "C400K1723423";
        Assert.assertFalse(device.isHSNDevice());

        device.serialNumber = "C400K1706000";
        Assert.assertTrue(device.isHSNDevice());

        device.serialNumber = "C403J1723423";
        Assert.assertTrue(device.isHSNDevice());

        device.serialNumber = "C403K1706000";
        Assert.assertTrue(device.isHSNDevice());

        device.serialNumber = "C403K1718000";
        Assert.assertFalse(device.isHSNDevice());

        device.serialNumber = "C400J1723423";
        Assert.assertTrue(device.isHSNDevice());

        device.serialNumber = "C403J1700019";
        Assert.assertTrue(device.isHSNDevice());

        device.serialNumber = "C400A1823423";
        Assert.assertFalse(device.isHSNDevice());

        device.deviceType = new DeviceType(DeviceType.CANARY_AIO);
        Assert.assertFalse(device.isHSNDevice());


    }


}

