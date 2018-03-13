package is.yranac.canary.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by michaelschroeder on 3/16/17.
 */
public class BackpackValidationTest {
    @Test
    public void isValidIdentifier() throws Exception {

        String identifier1 = "CanaryLTE-1234";
        String identifier2 = "CanaryLTE-848323";
        String identifier3 = "CanaTE-1234";
        String identifier4 = "CanaryLTE-1234";
        String identifier5 = "CanaryLTE-bn38";

        String identifier6 = "CanaryLTE-;.`/";

        Assert.assertTrue(BackpackValidation.isValidIdentifier(identifier1));
        Assert.assertFalse(BackpackValidation.isValidIdentifier(identifier2));
        Assert.assertFalse(BackpackValidation.isValidIdentifier(identifier3));
        Assert.assertTrue(BackpackValidation.isValidIdentifier(identifier4));
        Assert.assertTrue(BackpackValidation.isValidIdentifier(identifier5));
        Assert.assertFalse(BackpackValidation.isValidIdentifier(identifier6));
    }

    @Test
    public void isValidPairingKey() throws Exception {
        String pairingKey1 = "D51JHGneHaRhg4F0I";
        String pairingKey2 = "lKzq4M53UO3";
        String pairingKey3 = "zo45QbFGZUTOfQFtB";
        String pairingKey4 = "5pEmIk6@G^7l&DAO%";

        Assert.assertTrue(BackpackValidation.isValidPairingKey(pairingKey1));
        Assert.assertFalse(BackpackValidation.isValidPairingKey(pairingKey2));
        Assert.assertTrue(BackpackValidation.isValidPairingKey(pairingKey3));
        Assert.assertFalse(BackpackValidation.isValidPairingKey(pairingKey4));
    }

    @Test
    public void isValidIMEI() throws Exception {
        String IMEI1 = "990000862471854";
        String IMEI2 = "35175605asdlhb23999";
        String IMEI3 = "351756051523999";
        String IMEI4 = "8593874788345";
        Log.i("Test", String.valueOf(IMEI1.length()));

        Assert.assertTrue(BackpackValidation.isValidIMEI(IMEI1));
        Assert.assertFalse(BackpackValidation.isValidIMEI(IMEI2));
        Assert.assertTrue(BackpackValidation.isValidIMEI(IMEI3));
        Assert.assertFalse(BackpackValidation.isValidIMEI(IMEI4));
    }

    @Test
    public void isValidCCID() throws Exception {
        String ICCID1 = "99000086247185483956";
        String ICCID2 = "35175605asdlhb2399983956";
        String ICCID3 = "35175605152399983956";
        String ICCID4 = "8593874788345";

        Assert.assertTrue(BackpackValidation.isValidICCID(ICCID1));
        Assert.assertFalse(BackpackValidation.isValidICCID(ICCID2));
        Assert.assertTrue(BackpackValidation.isValidICCID(ICCID3));
        Assert.assertFalse(BackpackValidation.isValidICCID(ICCID4));
    }

}