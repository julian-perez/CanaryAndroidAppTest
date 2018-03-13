package is.yranac.canary.util;

import android.content.res.Resources;
import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;
import android.test.mock.MockContext;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import is.yranac.canary.R;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by michaelschroeder on 6/5/17.
 */
@RunWith(AndroidJUnit4.class)
public class BaseConstantsTest {
    private static final String LOG_TAG = "BaseConstantsTest";

    @Test
    public void autoLoginUrl() throws Exception {

        String expectedValue = "api/auth/m2w?webview=true&path=%2Fsubscription%2Fcheckout&locationId=1234&recurrence=monthly&promoCode=summer2017&email=schroeder%40canary.is";

        String test = "https://my.canary.is/subscription/checkout?locationId=1234&recurrence=monthly&promoCode=summer2017";
        MockContext mockContext = mock(MockContext.class);
        Resources resouces = mock(Resources.class);
        when(mockContext.getResources()).thenReturn(resouces);
        when(mockContext.getString(R.string.scheme)).thenReturn("https");
        when(mockContext.getString(R.string.webapp)).thenReturn("webapp-stage.canaryis.com");
        String url = BaseConstants.autoLoginUrl(Uri.parse(test), mockContext);

        Assert.assertTrue(url.contains(expectedValue));

    }

    @Test
    public void autoLoginUrl1() throws Exception {
        String expectedValue = "api/auth/m2w?webview=true&email=schroeder%40canary.is&locationId=123";
        MockContext mockContext = mock(MockContext.class);
        Resources resouces = mock(Resources.class);
        when(mockContext.getResources()).thenReturn(resouces);
        when(mockContext.getString(R.string.scheme)).thenReturn("https");
        when(mockContext.getString(R.string.webapp)).thenReturn("webapp-stage.canaryis.com");
        String url = BaseConstants.autoLoginUrl(123, mockContext);
        Log.i(LOG_TAG, url);

        Assert.assertTrue(url.contains(expectedValue));

    }

}