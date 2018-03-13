package is.yranac.canary.util;

import org.junit.Assert;
import org.junit.Test;

import is.yranac.canary.CanaryApplication;

/**
 * Created by narendramanoharan on 5/25/16.
 */
public class DensityUtilTest {

    @Test
    public void testDip2px() throws Exception {
        Float aFloat = 1080f;
        int px = DensityUtil.dip2px(CanaryApplication.getContext(), aFloat);
        Assert.assertNotNull(px);
    }

    @Test
    public void testPx2dip() throws Exception {
        Float aFloat = 1080f;
        int dip = DensityUtil.px2dip(CanaryApplication.getContext(), aFloat);
        Assert.assertNotNull(dip);
    }

    @Test
    public void testPx2sp() throws Exception {
        Float aFloat = 1080f;
        int sp = DensityUtil.px2sp(CanaryApplication.getContext(), aFloat);
        Assert.assertNotNull(sp);
    }
}
