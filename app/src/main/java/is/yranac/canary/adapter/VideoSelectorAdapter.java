package is.yranac.canary.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.model.videoexport.VideoExport;

/**
 * Created by Schroeder on 3/23/16.
 */
public class VideoSelectorAdapter extends BaseAdapter {


    private final Context context;
    private final List<VideoExport> videoExports;

    public VideoSelectorAdapter(@NonNull Context context, @NonNull List<VideoExport> videoExports) {
        this.context = context;
        this.videoExports = videoExports;
    }


    private class VideoSelectorHolder {
        TextView deviceName;
        TextView videoLength;
        TextView videoSize;
        ImageView imageView;

    }


    @Override
    public int getCount() {
        return videoExports.size();
    }

    @Override
    public VideoExport getItem(int position) {
        return videoExports.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        VideoSelectorHolder videoSelectorHolder;
        if (convertView == null || !(convertView.getTag() instanceof VideoSelectorHolder)) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.listrow_video_selector, null);
            videoSelectorHolder = new VideoSelectorHolder();
            videoSelectorHolder.deviceName = (TextView) convertView.findViewById(R.id.device_name);
            videoSelectorHolder.videoLength = (TextView) convertView.findViewById(R.id.video_length_text_view);
            videoSelectorHolder.videoSize = (TextView) convertView.findViewById(R.id.video_size_text_view);
            videoSelectorHolder.imageView = (ImageView) convertView.findViewById(R.id.thumbnail_image_view);
        } else {
            videoSelectorHolder = (VideoSelectorHolder) convertView.getTag();
        }


        VideoExport videoExport = videoExports.get(position);
        int duration = videoExport.duration;
        float size = videoExport.size;

        float sizeMB = (int) (size / Constants.Kibi);

        float durationSec = duration % 60;
        float durationMin = duration / 60;

        DecimalFormat format = new DecimalFormat("##");
        format.setMinimumIntegerDigits(2);

        format.setRoundingMode(RoundingMode.HALF_UP);
        String time = context.getString(R.string.time_format, format.format(durationMin), format.format(durationSec));
        videoSelectorHolder.videoLength.setText(time);
        format.setMinimumIntegerDigits(1);

        if (size < Constants.Kibi) {
            videoSelectorHolder.videoSize.setText(context.getString(R.string.kb_format, format.format(size)));
        } else {
            videoSelectorHolder.videoSize.setText(context.getString(R.string.mb_format, format.format(sizeMB)));
        }

        ImageLoader.getInstance().displayImage(videoExport.thumbnail, videoSelectorHolder.imageView);

        videoSelectorHolder.deviceName.setText(videoExport.name);
        return convertView;
    }
}
