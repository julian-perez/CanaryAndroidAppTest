package is.yranac.canary.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import is.yranac.canary.R;
import is.yranac.canary.model.clip.Clip;
import is.yranac.canary.util.DensityUtil;

/**
 * Created by Schroeder on 5/12/16.
 */
public class SeekbarWithIntervals extends LinearLayout implements SeekBar.OnSeekBarChangeListener {
    private static final String LOG_TAG = "SeekbarWithIntervals";
    private RelativeLayout relativeLayout = null;
    private SeekBar seekbar = null;
    private EntrySeekBarCallBack entrySeekBarCallBack;
    private List<Clip> clips = new ArrayList<>();
    private View progressView;
    private View bufferView;
    private int totalDuration = 0;

    public void setDuration(long duration) {
        getSeekbar().setMax((int) duration);
    }


    public interface EntrySeekBarCallBack {

        void setTimeStampLabels(long timeStamp);

        void changeVideoPosition(long position);

        void stopPlayBack();

    }


    public SeekbarWithIntervals(Context context) {
        super(context);
        init();
    }

    public SeekbarWithIntervals(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public SeekbarWithIntervals(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        setUpClipLines();
    }

    private RelativeLayout getRelativeLayout() {
        if (relativeLayout == null) {
            relativeLayout = (RelativeLayout) findViewById(R.id.intervals);
        }

        return relativeLayout;
    }

    private SeekBar getSeekbar() {
        if (seekbar == null) {
            seekbar = (SeekBar) findViewById(R.id.seekbar);
        }

        return seekbar;
    }

    private View getProgressView() {
        if (progressView == null) {
            progressView = findViewById(R.id.seek_bar_progress);
        }

        return progressView;
    }


    private View getBufferView() {
        if (bufferView == null) {
            bufferView = findViewById(R.id.seek_bar_buffer);
        }

        return bufferView;
    }

    private void init() {
        inflate(getContext(), R.layout.seekbar_with_intervals, this);
        getSeekbar().setOnSeekBarChangeListener(this);


    }

    public void setClipsAndActivityNotification(List<Clip> clips) {
        totalDuration = 0;

        this.clips = clips;

        for (Clip clip : clips) {
            totalDuration += clip.duration * 1000;
        }
        setUpClipLines();

    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        entrySeekBarCallBack.stopPlayBack();
        entrySeekBarCallBack.setTimeStampLabels(getSeekTime(getSeekbar().getProgress()));

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (entrySeekBarCallBack == null)
            return;

        float percentage = getSeekbar().getPaddingLeft() + ((getWidth() -
                getSeekbar().getPaddingLeft() * 2) * (float) progress / (float) getSeekbar().getMax());
        getProgressView().getLayoutParams().width = (int) (percentage);
        getProgressView().requestLayout();
        entrySeekBarCallBack.setTimeStampLabels(getSeekTime(progress));
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        entrySeekBarCallBack.setTimeStampLabels(getSeekTime(getSeekbar().getProgress()));
        entrySeekBarCallBack.changeVideoPosition(getSeekbar().getProgress());
    }

    public void setEntrySeekBarCallBack(EntrySeekBarCallBack entrySeekBarCallBack) {
        this.entrySeekBarCallBack = entrySeekBarCallBack;
    }


    public void setBuffer(int buffer) {
        float percentage = getSeekbar().getPaddingLeft() + ((getWidth() -
                getSeekbar().getPaddingLeft() * 2) * (float) buffer / 100.0f);

        getBufferView().getLayoutParams().width = (int) (percentage);

        getBufferView().requestLayout();
    }

    public long setPosition(long position) {
        getSeekbar().setProgress((int) position);
        return position;
    }


    private void setUpClipLines() {

        getRelativeLayout().removeAllViews();
        int localDuration = 0;

        for (Clip clip : clips) {

            if (clip.equals(clips.get(clips.size() - 1))) {
                break;
            }

            localDuration += clip.duration * 1000;
            float percentage = (float) localDuration / (float) totalDuration;

            float position = calculateOffsetFromPercentage(percentage);

            View view = new View(getContext());
            int width = DensityUtil.dip2px(getContext(), 2);
            int height = DensityUtil.dip2px(getContext(), 8);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);

            view.setLayoutParams(layoutParams);
            view.setBackgroundColor(getResources().getColor(R.color.black));
            view.setX(position);

            getRelativeLayout().addView(view);

        }


        getRelativeLayout().invalidate();
    }

    private float calculateOffsetFromPercentage(float percentage) {
        return (getWidth() - getSeekbar().getPaddingLeft() * 2) * (percentage);
    }

    private long getSeekTime(long currentVideoTime) {
        if (clips.isEmpty())
            return 0;

        long localClipDurationCounter = 0;

        Clip firstClip = clips.get(0);


        Date firstStartDate = firstClip.start;

        long firstStartTime = firstStartDate.getTime();

        for (Clip clip : clips) {

            if (currentVideoTime <= localClipDurationCounter + clip.duration * 1000) {
                Date startDate = clip.start;
                long startTime = startDate.getTime();
                long seekTime = currentVideoTime - localClipDurationCounter;
                return startTime + seekTime;
            }
            localClipDurationCounter += clip.duration * 1000;
        }

        return firstStartTime + currentVideoTime;

    }


}

