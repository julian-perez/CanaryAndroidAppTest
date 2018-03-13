package is.yranac.canary.ui.views;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import is.yranac.canary.R;
import is.yranac.canary.model.reading.Reading;
import is.yranac.canary.model.reading.Reading.WifiConnectionLevel;

import static is.yranac.canary.model.reading.Reading.WifiConnectionLevel.MIN;

/**
 * Created by sergeymorozov on 6/2/16.
 */

public class WifiLevelView extends AppCompatImageView {

    /**
     * Constructor that is called when inflating a view from XML. This is called
     * when a view is being constructed from an XML file, supplying attributes
     * that were specified in the XML file. This version uses a default style of
     * 0, so the only attribute values applied are those in the Context's Theme
     * and the given AttributeSet.
     * <p/>
     * <p/>
     * The method onFinishInflate() will be called after all children have been
     * added.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     */
    public WifiLevelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setWifiConnectionLevel(Reading wifiReading) {

        WifiConnectionLevel level;
        int resourceId;
        if (wifiReading == null) {
            level = MIN;

        } else {
            level = wifiReading.getWifiConnectionLevel();
        }
        switch (level) {
            case MIN:
                resourceId = R.drawable.wifi_offline;
                break;
            case ONE_BAR:
                resourceId = R.drawable.wifi_low;
                break;
            case TWO_BARS:
                resourceId = R.drawable.wifi_med;
                break;
            case FULL:
                resourceId = R.drawable.wifi_high;
                break;
            default:
                setVisibility(GONE);
                return;
        }


        setImageResource(resourceId);
    }


}
