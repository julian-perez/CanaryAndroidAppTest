package is.yranac.canary.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import is.yranac.canary.R;
import is.yranac.canary.databinding.ViewBatteryIndicatorBinding;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceSettings;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.model.reading.Reading;
import is.yranac.canary.model.reading.Reading.BatteryState;
import is.yranac.canary.util.UserUtils;

import static is.yranac.canary.model.reading.Reading.READING_BATTERY;


/**
 * Created by sergeymorozov on 5/27/16.
 */
public class BatteryIndicator extends FrameLayout {

    public static final int batteryMax = 100;
    public static final int batteryMin = 0;
    public static final int batteryChriticalThreshold = 10;
    public static final int batteryWhiteThreshold = 100;


    private ViewBatteryIndicatorBinding binding;

    private boolean useWhiteIcons;
    private int initialViewWidth;

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
     * @see #View(Context, AttributeSet)
     */
    public BatteryIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray xmlAttributes = context.obtainStyledAttributes(attrs, R.styleable.BatteryIndicator, 0, 0);
        useWhiteIcons = xmlAttributes.getInt(R.styleable.BatteryIndicator_ColorScheme, 0) == 0;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = ViewBatteryIndicatorBinding.inflate(inflater, this, true);

        binding.batteryOverlay.setVisibility(GONE);
        initialViewWidth = (int) getContext()
                .getResources()
                .getDimension(R.dimen.battery_indicator_initial_width);

        xmlAttributes.recycle();
    }


    public void updateBatteryState(@Nullable Reading batteryReading, @NonNull Device device) {
        redrawView(batteryReading, device);
    }

    private float normalizeChargeValue(float chargeRemaining) {
        return chargeRemaining > batteryMax ? batteryMax
                : chargeRemaining < batteryMin ? batteryMin : chargeRemaining;
    }

    private void redrawView(Reading batteryReading, Device device) {

        if (binding == null)
            return;


        binding.batteryOverlay.setVisibility(VISIBLE);
        if (batteryReading != null) {
            binding.batteryOverlay.getLayoutParams().width =
                    getOverlayWidth(batteryReading.getBatteryAdjustedValue());
        } else {

            binding.batteryOverlay.getLayoutParams().width = getOverlayWidth(0);
        }

        BatteryState batteryState;
        if (batteryReading == null) {
            batteryState = BatteryState.OFFLINE;
            batteryReading = Reading.getReadingForOfflineDevice(READING_BATTERY);
        } else if (device != null && !device.isOnline) {
            batteryState = BatteryState.OFFLINE;
        } else if (batteryReading.getBatteryAdjustedValue() >= batteryMax) {
            batteryState = BatteryState.FULL;
        } else {
            batteryState = batteryReading.getBatteryState();
        }

        int resourceId;
        switch (batteryState) {
            case CHARGHING:
            case FULL:
                if (useWhiteIcons) {
                    resourceId = R.drawable.ic_batteryicon_charging_white;
                } else {
                    resourceId = R.drawable.ic_battery_charging_new;

                }
                break;
            case OFFLINE:
                if (useWhiteIcons) {
                    resourceId = R.drawable.ic_batteryicon_offline;
                } else {
                    resourceId = R.drawable.ic_batteryicon_offline;
                }
                break;
            case DISCHARGING:
                resourceId =
                        R.drawable.ic_batteryicon_empty;
                break;
            case ISSUE:
                if (useWhiteIcons) {
                    resourceId =
                            R.drawable.ic_batteryicon_error_white;
                } else {
                    resourceId =
                            R.drawable.ic_battery_error_red;
                }

                break;
            default:
                resourceId = Integer.MIN_VALUE;
        }


        binding.batteryImage.setImageResource(resourceId);

        if (batteryState == BatteryState.DISCHARGING) {
            if (useWhiteIcons) {
                setOverlayColor(R.color.white);
            } else if (batteryReading.getBatteryAdjustedValue() <= batteryChriticalThreshold) {
                setOverlayColor(R.color.light_red);
            } else {
                setOverlayColor(R.color.white);
            }
        } else {
            binding.batteryOverlay.setVisibility(GONE);
        }
    }

    private void setOverlayColor(int color) {
        if (binding == null)
            return;

        Drawable background = binding.batteryOverlay.getBackground();
        if (background == null || !(background instanceof GradientDrawable))
            return;

        ((GradientDrawable) background).setColor(ContextCompat.getColor(getContext(), color));
    }

    private int getOverlayWidth(float chargeRemaining) {
        if (initialViewWidth == Integer.MIN_VALUE)
            initialViewWidth = binding.batteryOverlay.getWidth();
        return (int) UserUtils.formatFloatDecimalPlaces(initialViewWidth * (normalizeChargeValue(chargeRemaining) / 100), 0);
    }
}
