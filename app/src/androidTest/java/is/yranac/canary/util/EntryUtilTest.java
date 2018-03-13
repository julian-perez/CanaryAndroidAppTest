package is.yranac.canary.util;


import android.content.ContentProvider;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import is.yranac.canary.Constants;
import is.yranac.canary.contentproviders.CanaryEntryContentProvider;
import is.yranac.canary.model.entry.Entry;
import is.yranac.canary.model.videoexport.VideoExport;
import is.yranac.canary.services.database.BaseDatabaseService;
import is.yranac.canary.services.database.EntryDatabaseService;
import is.yranac.canary.services.database.VideoExportDatabaseService;
import is.yranac.canary.util.EntryUtil.DOWNLOADSTATUS;

@RunWith(MockitoJUnitRunner.class)
public class EntryUtilTest extends ProviderTestCase2<CanaryEntryContentProvider> {


    @Mock
    Context context;

    public EntryUtilTest() {
        super(CanaryEntryContentProvider.class, Constants.AUTHORITY_ENTRY);
    }

    @Test
    public void testGetDownloadProgress() throws Exception {

        MockContentResolver mockContentResolver = new MockContentResolver();


        ContentProvider contentProvider = new CanaryEntryContentProvider();

        ProviderInfo providerInfo = new ProviderInfo();
        providerInfo.authority = Constants.AUTHORITY_ENTRY;
        contentProvider.attachInfo(context, providerInfo);
        contentProvider.onCreate();
        mockContentResolver.addProvider(Constants.AUTHORITY_VIDEO_EXPORT, contentProvider);


        BaseDatabaseService.contentResolver = mockContentResolver;

        Entry entry = new Entry();
        entry.exported = true;
        EntryDatabaseService.insertEntryAndLinkedObjects(entry, 0);
        DOWNLOADSTATUS downloadStatus = EntryUtil.getDownloadProgress(entry);
        Assert.assertEquals(downloadStatus, DOWNLOADSTATUS.DOWNLOAD_READY);

        entry.exported = false;

        VideoExport videoExport = new VideoExport();
        videoExport.requestedAt = new Date();
        videoExport.processing = true;
        List<VideoExport> videoExportList = new ArrayList<>();
        videoExportList.add(videoExport);
        VideoExportDatabaseService.insertVideoExport(videoExport);

        DOWNLOADSTATUS downloadStatus2 = EntryUtil.getDownloadProgress(entry);
        Assert.assertEquals(downloadStatus2, DOWNLOADSTATUS.IN_PROGRESS);

        videoExport.processing = false;

        DOWNLOADSTATUS downloadStatus3 = EntryUtil.getDownloadProgress(entry);

        Assert.assertEquals(downloadStatus3, DOWNLOADSTATUS.NOT_STARTED);


    }
}
