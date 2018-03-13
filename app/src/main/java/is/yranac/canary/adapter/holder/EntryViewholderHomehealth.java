package is.yranac.canary.adapter.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import is.yranac.canary.R;
import is.yranac.canary.model.entry.Entry;
import is.yranac.canary.ui.BaseActivity;
import is.yranac.canary.util.DateUtil;
import is.yranac.canary.util.StringUtils;

/**
 * Created by sergeymorozov on 3/25/16.
 */
public class EntryViewholderHomehealth extends EntryViewHolder {

    TextView dateTextView;
    TextView summaryTestView;
    ImageView iconImageView;
    public EntryViewholderHomehealth(BaseActivity activity, View view, ViewGroup viewGroup) {
        super(view, activity);
        this.dateTextView = (TextView) view.findViewById(R.id.entry_homehealth_date);
        this.summaryTestView = (TextView) view.findViewById(R.id.entry_homehealth_summary);
        this.iconImageView = (ImageView) view.findViewById(R.id.entry_homehealth_image);
    }

    private void setDate(String date) {
        if (this.dateTextView == null || StringUtils.isNullOrEmpty(date))
            return;

        this.dateTextView.setText(date);
    }

    private void setSummary(String summary) {

        if (this.summaryTestView == null || StringUtils.isNullOrEmpty(summary))
            return;

        this.summaryTestView.setText(summary);
    }

    private void setIcon(String homehealthEntryType) {
        if (this.iconImageView == null || StringUtils.isNullOrEmpty(homehealthEntryType))
            return;

        switch (homehealthEntryType) {
            case Entry.ENTRY_TYPE_HUMIDITY:
                this.iconImageView.setImageResource(R.drawable.timeline_icn_alert_humidity);
                break;
            case Entry.ENTRY_TYPE_AIR_QUALITY:
                this.iconImageView.setImageResource(R.drawable.timeline_icn_alert_airquality);
                break;
            case Entry.ENTRY_TYPE_TEMPERATURE:
                this.iconImageView.setImageResource(R.drawable.timeline_icn_alert_temperature);
                break;
        }
    }

    @Override
    public void setUpEntry(Entry entry) {
        setDate(DateUtil.utcDateToDisplayString(entry.startTime));
        setSummary(entry.description);
        setIcon(entry.entryType);
    }

}
