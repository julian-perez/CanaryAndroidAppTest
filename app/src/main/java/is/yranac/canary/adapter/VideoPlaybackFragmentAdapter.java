package is.yranac.canary.adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

import java.util.List;

import is.yranac.canary.fragments.VideoPlaybackFragment;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.model.reading.Reading;
import is.yranac.canary.model.thumbnail.Thumbnail;
import is.yranac.canary.services.database.ReadingDatabaseService;

public class VideoPlaybackFragmentAdapter extends FragmentStatePagerAdapter {

    private static final String LOG_TAG = "VideoPlaybackFragmentAdapter";
    private int devicesCount;
    private final SparseArray<VideoPlaybackFragment> mPageReferences = new SparseArray<>();
    private final SparseArray<Reading> readingSparseArray = new SparseArray<>();

    private List<Thumbnail> thumbnails;
    private List<Device> devices;
    private boolean showThumbnailsInViewpager;
    private boolean letterBoxed = false;

    public VideoPlaybackFragmentAdapter(FragmentManager supportFragmentManager, List<Device> devices) {
        super(supportFragmentManager);
        this.devicesCount = devices.size();
        this.devices = devices;
        this.thumbnails = null;
        this.showThumbnailsInViewpager = true;
    }

    public VideoPlaybackFragmentAdapter(FragmentManager supportFragmentManager,
                                        List<Thumbnail> thumbnails, boolean showThumbnailsInViewpager) {
        super(supportFragmentManager);
        this.devicesCount = thumbnails.size();
        this.thumbnails = thumbnails;
        this.showThumbnailsInViewpager = showThumbnailsInViewpager;
    }

    @Override
    public Fragment getItem(int position) {

        boolean showPlayBtn = true;
        boolean flexLoading = false;
        String imageUrl;

        boolean showThumbnail = showThumbnailsInViewpager;
        long thumbnailId = 0;
        if (thumbnails != null) {
            Thumbnail thumbnail = thumbnails.get(position);
            imageUrl = thumbnail.imageUrl();
            thumbnailId = thumbnail.id;
        } else {
            Device device = devices.get(position);
            showPlayBtn = false;
            showThumbnailsInViewpager = true;
            imageUrl = devices.get(position).imageUrl();

            // #Blame Dave
            if (device.getDeviceType() == DeviceType.FLEX) {
                Reading batteryReading = readingSparseArray.get(device.id);
                if (batteryReading == null) {
                    batteryReading = ReadingDatabaseService.getLatestReadingForDevice(
                            device.resourceUri, Reading.READING_BATTERY);
                    readingSparseArray.put(device.id, batteryReading);
                }
                if (batteryReading != null) {
                    flexLoading = batteryReading.getBatteryState() == Reading.BatteryState.DISCHARGING;
                }
            }

        }

        boolean showOffline = devices != null && !devices.get(position).isOnline;
        boolean showPrivate = devices != null && devices.get(position).isPrivate();


        VideoPlaybackFragment fragment =
                VideoPlaybackFragment.newInstance(imageUrl, thumbnailId,
                        showThumbnail, showOffline, showPrivate, showPlayBtn,
                        letterBoxed, flexLoading);
        mPageReferences.put(position, fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        return devicesCount;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public VideoPlaybackFragment getFragment(int key) {
        return mPageReferences.get(key);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    public void setShowLetterBox() {
        if (this.letterBoxed)
            return;
        this.letterBoxed = true;
        notifyDataSetChanged();
    }

    public void hideLetterBox() {
        if (!this.letterBoxed)
            return;
        this.letterBoxed = false;
        notifyDataSetChanged();
    }
}

