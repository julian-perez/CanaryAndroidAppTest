package is.yranac.canary.fragments;

import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.contentproviders.CanaryReadingContentProvider;
import is.yranac.canary.databinding.FragmentHealthGraphBinding;
import is.yranac.canary.messages.PastReadingsUpdated;
import is.yranac.canary.messages.ShowModalFragment;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceSettings;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.reading.Reading;
import is.yranac.canary.services.api.DeviceAPIService;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.services.database.ReadingDatabaseService;
import is.yranac.canary.ui.SettingsFragmentStackActivity;
import is.yranac.canary.ui.views.CircleView;
import is.yranac.canary.ui.views.CustomScrollView;
import is.yranac.canary.util.AnimationHelper;
import is.yranac.canary.util.DateUtil;
import is.yranac.canary.util.DensityUtil;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.StringUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.TutorialUtil;
import is.yranac.canary.util.TutorialUtil.TutorialType;
import is.yranac.canary.util.UserUtils;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import retrofit.Callback;

public class HomeHealthDataFragment extends Fragment {

    private static final String LOG_TAG = "ModeHealthDataFragment";
    static final String key_deviceName = "key_deviceName";
    static final String key_device = "key_device";

    private final int GREEN = 1;
    private final int darkGreen = Color.rgb(74, 175, 134);
    private final int lightGreen = Color.argb(64, 74, 175, 134);

    private final int RED = 2;
    private final int darkRed = Color.rgb(255, 83, 63);
    private final int lightRed = Color.argb(64, 255, 83, 63);

    private final int TEMP = 1;
    private final int HUMIDITY = 2;
    private final int AIR_QUALITY = 3;

    private final char degreeSymbol = '\u00B0';
    private final char percentSymbol = '\u0025';

    private Typeface tf;

    private String deviceUri;

    private List<Reading> tempReadingList;
    private List<Reading> humidityReadingList;
    private List<Reading> airQualityReadingList;

    private GetTemp getTemp;
    private GetHumidity getHumidity;
    private GetAirQuality getAirQuality;

    private DeviceSettings deviceSettings;
    private Location location;

    private int previousAirQualityColor = -1;
    private int previousTempColor = -1;
    private int previousHumidityColor = -1;

    private FragmentHealthGraphBinding binding;

    public static HomeHealthDataFragment newInstance(String deviceURI, String deviceName) {
        HomeHealthDataFragment modalHealthDataFragment = new HomeHealthDataFragment();
        Bundle bundle = new Bundle();
        bundle.putString(key_device, deviceURI);
        bundle.putString(key_deviceName, deviceName);
        modalHealthDataFragment.setArguments(bundle);
        return modalHealthDataFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        binding = FragmentHealthGraphBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        deviceUri = getArguments().getString(key_device);

        GoogleAnalyticsHelper.trackScreenEvent("HomeHealth");

        if (TutorialUtil.getTutorialInProgress() == TutorialType.HOME) {
            binding.tutorialFrameGraph.setVisibility(View.VISIBLE);
            binding.tutorialFrameGraphTwo.setVisibility(View.VISIBLE);
            binding.tutorialFrameGraphThree.setVisibility(View.VISIBLE);
        } else {
            binding.tutorialFrameGraph.setVisibility(View.GONE);
            binding.tutorialFrameGraphTwo.setVisibility(View.GONE);
            binding.tutorialFrameGraphThree.setVisibility(View.GONE);
        }

        binding.temperatureChart.setVisibility(View.GONE);
        binding.humidityChart.setVisibility(View.GONE);
        binding.airQualityChart.setVisibility(View.GONE);


        tf = Typeface.createFromAsset(getActivity().getAssets(), "Gibson.otf");
        binding.header.headerTitleTextView.setText(getHeaderTitle());


        binding.header.headerViewRightButton.setVisibility(View.VISIBLE);
        binding.header.headerViewRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettings();
            }
        });

        binding.temperatureChart.setBackgroundColor(lightGreen);
        binding.humidityChart.setBackgroundColor(lightGreen);
        binding.airQualityChart.setBackgroundColor(lightGreen);

        binding.tempChartBackground.setBackgroundColor(lightGreen);
        binding.humidityChartBackground.setBackgroundColor(lightGreen);
        binding.airQualityChartBackground.setBackgroundColor(lightGreen);

        binding.temperatureInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeHealthInfoFragment homeHealthInfoFragment = HomeHealthInfoFragment
                        .newInstance(HomeHealthInfoFragment.TEMPERATURE);

                TinyMessageBus.post(new ShowModalFragment(homeHealthInfoFragment));
            }
        });

        binding.humidityInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeHealthInfoFragment homeHealthInfoFragment = HomeHealthInfoFragment
                        .newInstance(HomeHealthInfoFragment.HUMIDITY);

                TinyMessageBus.post(new ShowModalFragment(homeHealthInfoFragment));
            }
        });

        binding.airQualityInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeHealthInfoFragment homeHealthInfoFragment = HomeHealthInfoFragment
                        .newInstance(HomeHealthInfoFragment.AIR_QUALITY);

                TinyMessageBus.post(new ShowModalFragment(homeHealthInfoFragment));
            }
        });
        resetValues();
    }


    @Override
    public void onResume() {
        super.onResume();
        TinyMessageBus.register(this);
        refreshReadingsForDevice(CanaryReadingContentProvider.READING_TEMPERATURE);
        refreshReadingsForDevice(CanaryReadingContentProvider.READING_HUMIDITY);
        refreshReadingsForDevice(CanaryReadingContentProvider.READING_AIR_QUALITY);
    }

    @Override
    public void onPause() {
        super.onPause();
        getTemp.cancel(true);
        getHumidity.cancel(true);
        getAirQuality.cancel(true);
        TinyMessageBus.unregister(this);
        resetValues();
    }

    @Subscribe
    public void onPastReadingsUpdated(PastReadingsUpdated pastReadingsUpdated) {
        if (pastReadingsUpdated == null)
            return;

        for (Integer readingType : pastReadingsUpdated.getUpdatedReadingTypesForDevice(deviceUri)) {
            refreshReadingsForDevice(readingType);
        }
    }

    private void refreshReadingsForDevice(int readingType) {
        Log.i(LOG_TAG, "refreshReadingsForDevice " + readingType);
        switch (readingType) {
            case CanaryReadingContentProvider.READING_HUMIDITY:
                getHumidity = new GetHumidity();
                getHumidity.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                        String.valueOf(CanaryReadingContentProvider.READING_HUMIDITY));
                break;
            case CanaryReadingContentProvider.READING_TEMPERATURE:
                getTemp = new GetTemp();
                getTemp.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                        String.valueOf(CanaryReadingContentProvider.READING_TEMPERATURE), deviceUri);
                break;
            case CanaryReadingContentProvider.READING_AIR_QUALITY:
                getAirQuality = new GetAirQuality();
                getAirQuality.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                        String.valueOf(CanaryReadingContentProvider.READING_AIR_QUALITY));
                break;
            default:
                return;
        }
    }

    private void resetValues() {
        tempReadingList = new ArrayList<>();
        humidityReadingList = new ArrayList<>();
        airQualityReadingList = new ArrayList<>();
    }

    private String getHeaderTitle() {
        String name = getArguments().getString(key_deviceName);
        return getString(R.string.home_health_title, name);
    }

    private void setupTemperatureGraph() {

        List<PointValue> values = new ArrayList<>();
        List<AxisValue> xAxisValues = new ArrayList<>();
        List<AxisValue> yAxisValues = new ArrayList<>();

        int total = 144;

        Date lastDate = tempReadingList.get(tempReadingList.size() - 1).created;

        Date firstDate = tempReadingList.get(0).created;

        long firstTime = firstDate.getTime();
        long lastTime = lastDate.getTime();


        long endDiff = firstTime - (DateUtil.getCurrentTime().getTime() - (60 * 60 * 24 * 1000));

        int offset = 0;
        if (endDiff < 1000 * 19 * 60.0) {
            total = tempReadingList.size();
        } else {
            long nowTime = DateUtil.getCurrentTime().getTime();

            float offsetPercent = (nowTime - lastTime) / (60.0f * 60.0f * 24.0f * 1000.0f);
            offset = total - tempReadingList.size();

            if (offsetPercent > (60 * 60) / (60.0f * 60.0f * 24.0f)) {
                offset += total * offsetPercent;
            }
        }


        float max = Float.MIN_VALUE;
        float min = Float.MAX_VALUE;


        int i = 0;
        for (Reading reading : tempReadingList) {
            float value = reading.value;

            if (value != Float.MIN_VALUE) {

                float convertedValue = UserUtils.getTemperatureInPreferredUnits(value,
                        needCelsius(), false);

                values.add(new PointValue(i + offset, convertedValue));

                if (convertedValue > max) {
                    max = convertedValue;
                }

                if (convertedValue < min) {
                    min = convertedValue;
                }
            }
            i++;
        }


        int padding;
        int minDiff;
        int yInterval;
        if (!needCelsius()) {
            padding = 5;
            minDiff = 70;
            yInterval = 10;
        } else {
            padding = 2;
            minDiff = 35;
            yInterval = 5;
        }

        max += padding;
        min -= padding;

        int roundMax = Utils.roundDownNearest((int) max, yInterval) + yInterval;
        int roundMin = Utils.roundUpNearest((int) min, yInterval) - yInterval;

        if (max - min < minDiff) {
            float diff = (roundMax - roundMin) / 2;
            roundMin -= ((minDiff / 2) - diff);
            roundMin = Utils.roundUpNearest(roundMin, yInterval);
            roundMax = roundMin + minDiff;
        }

        for (int j = roundMin; j < roundMax; j += yInterval) {
            yAxisValues.add(new AxisValue(j).setLabel(String.valueOf(j) + degreeSymbol));
        }

        long interval = 24 * 60 * 60 * 1000 / 6;

        for (i = 6; i >= 0; i--) {
            Date intervalDate = new Date(lastTime - (interval * (6 - i)));
            int x = (int) (total * ((float) i / 6.0f));
            String dateFormatted = DateUtil.convertDateToHour(intervalDate).toUpperCase();
            xAxisValues.add(new AxisValue(x).setLabel(dateFormatted));
        }

        Viewport v = new Viewport(0, roundMax, total - 1, roundMin);
        v.inset(0, 0);

        //In most cased you can call data model methods in builder-pattern-like manner.
        final Line line = new Line(values).setCubic(true).setFilled(true).setHasPoints(false)
                .setColor(darkGreen);

        List<Line> lines = new ArrayList<>();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        final Axis axisX = new Axis().setValues(xAxisValues).setTypeface(tf).setTextSize(10)
                .setTextColor(darkGreen).setMaxLabelChars(4).setInside(true).setHasLines(false)
                .setHasSeparationLine(false);

        final Axis axisY = new Axis().setValues(yAxisValues).setInside(true).setTypeface(tf)
                .setTextSize(10).setTextColor(darkGreen).setHasSeparationLine(false)
                .setMaxLabelChars(4);

        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        data.setBaseValue(Float.NEGATIVE_INFINITY);

        binding.temperatureChart.setPadding(0, 0, 0, 0);
        binding.temperatureChart.setLineChartData(data);


        binding.temperatureChart.setZoomEnabled(false);

        binding.temperatureChart.setMaximumViewport(v);
        binding.temperatureChart.setCurrentViewport(v);

        line.setHasLabelsOnlyForSelected(false);

        binding.temperatureChart.setValueSelectionEnabled(false);

        setUpLineChart(binding.temperatureChart);


        binding.tempLine.setBackgroundColor(darkGreen);
        binding.tempCircle.setBackgroundColor(darkGreen);

        if (tempReadingList.size() > total - 10) {
            binding.tempLoadingTextView.setVisibility(View.GONE);
        } else {
            binding.tempLoadingTextView.setVisibility(View.VISIBLE);
            binding.tempLoadingTextView.setText(R.string.collecting_homehealth_data);
        }

        setUpUserThresholds(TEMP, lines);
    }

    private void setupHumidityGraph() {


        List<PointValue> values = new ArrayList<>();
        List<AxisValue> xAxisValues = new ArrayList<>();
        List<AxisValue> yAxisValues = new ArrayList<>();

        int total = 144;

        Date lastDate = humidityReadingList.get(humidityReadingList.size() - 1).created;

        Date firstDate = humidityReadingList.get(0).created;

        long firstTime = firstDate.getTime();
        long lastTime = lastDate.getTime();


        long endDiff = firstTime - (DateUtil.getCurrentTime().getTime() - (60 * 60 * 24 * 1000));

        int offset = 0;
        if (endDiff < 1000 * 19 * 60.0) {
            total = humidityReadingList.size();
        } else {
            long nowTime = DateUtil.getCurrentTime().getTime();

            float offsetPercent = (nowTime - lastTime) / (60.0f * 60.0f * 24.0f * 1000.0f);
            offset = total - humidityReadingList.size();

            if (offsetPercent > (60 * 60) / (60.0f * 60.0f * 24.0f)) {
                offset += total * offsetPercent;
            }
        }

        int i = 0;

        float max = Integer.MIN_VALUE;
        float min = Integer.MAX_VALUE;

        for (Reading reading : humidityReadingList) {
            float value = reading.value;
            if (value != Float.MIN_VALUE) {
                values.add(new PointValue(i + offset, value));
                if (value > max) {
                    max = value;
                }

                if (value < min) {
                    min = value;
                }
            }
            i++;
        }

        max += 5;
        min -= 5;

        int roundMax = Utils.roundDownNearest((int) max, 10) + 10;
        int roundMin = Utils.roundUpNearest((int) min, 10) - 10;

        if (roundMax - roundMin < 70) {
            float diff = (roundMax - roundMin) / 2;
            roundMin -= (25 - diff);
            roundMax = roundMin + 70;
        }


        long interval = 24 * 60 * 60 * 1000 / 6;

        for (i = 6; i >= 0; i--) {
            Date intervalDate = new Date(lastTime - (interval * (6 - i)));
            int x = (int) (total * ((float) i / 6.0f));
            String dateFormatted = DateUtil.convertDateToHour(intervalDate).toUpperCase();
            xAxisValues.add(new AxisValue(x).setLabel(dateFormatted));
        }

        for (int j = roundMin; j <= roundMax; j += 10) {
            yAxisValues.add(new AxisValue(j).setLabel(String.valueOf(j) + percentSymbol));
        }

        //In most cased you can call data model methods in builder-pattern-like manner.
        final Line line = new Line(values).setCubic(true).setFilled(true).setHasPoints(false)
                .setColor(darkGreen);

        List<Line> lines = new ArrayList<>();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);
        data.setBaseValue(Float.NEGATIVE_INFINITY);

        final Axis axisX = new Axis().setValues(xAxisValues).setTypeface(tf).setTextSize(10)
                .setMaxLabelChars(4).setInside(true).setTextColor(darkGreen).setHasLines(false)
                .setHasSeparationLine(false);

        final Axis axisY = new Axis().setValues(yAxisValues).setInside(true).setTextColor
                (darkGreen).setTypeface(tf).setTextSize(10).setHasSeparationLine(false)
                .setMaxLabelChars(3);

        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        binding.humidityChart.setLineChartData(data);
        Viewport v = new Viewport(0, roundMax, total - 1, roundMin);

        binding.humidityChart.setMaximumViewport(v);
        binding.humidityChart.setCurrentViewport(v);

        binding.humidityChart.setZoomEnabled(false);

        line.setHasLabelsOnlyForSelected(false);

        binding.humidityChart.setValueSelectionEnabled(false);


        binding.humidityLine.setBackgroundColor(darkGreen);
        binding.humidityCircle.setBackgroundColor(darkGreen);

        setUpLineChart(binding.humidityChart);

        if (humidityReadingList.size() > total - 10) {
            binding.humidityLoadingTextView.setVisibility(View.GONE);
        } else {
            binding.humidityLoadingTextView.setVisibility(View.VISIBLE);
            binding.humidityLoadingTextView.setText(R.string.collecting_homehealth_data);
        }

        setUpUserThresholds(HUMIDITY, lines);
    }

    private void setUpUserThresholds(int graphType, List<Line> lines) {
        if (deviceSettings == null)
            return;

        if (!deviceSettings.sendHomehealthNotifications) {
            resetGraph(graphType);
            return;
        }

        List<PointValue> maxPoints = new ArrayList<>();
        List<PointValue> minPoints = new ArrayList<>();

        float thresholdMin = Float.MIN_VALUE, thresholdMax = Float.MAX_VALUE;

        boolean isGraphAbnormalForUser;

        // magical values for x-axes. While 0 makes sense, 500 is just a random val for y
        // to do a straright line
        int defaultMin = 0, defaultMax = 500;
        boolean enableMax = false, enableMin = false;
        switch (graphType) {
            case TEMP:
                if (tempReadingList == null || tempReadingList.isEmpty()) {
                    break;
                }
                enableMax = deviceSettings.sendTempMaxNotifications;
                enableMin = deviceSettings.sendTempMinNotifications;
                thresholdMin = UserUtils.getTemperatureInPreferredUnits(deviceSettings
                        .tempThresholdMin, needCelsius());
                thresholdMax = UserUtils.getTemperatureInPreferredUnits(deviceSettings
                        .tempThresholdMax, needCelsius());
                break;
            case HUMIDITY:
                if (humidityReadingList == null || humidityReadingList.isEmpty()) {
                    break;
                }

                enableMax = deviceSettings.sendHumidityMaxNotifications;
                enableMin = deviceSettings.sendHumidityMinNotifications;
                thresholdMin = deviceSettings.humidityThresholdMin * 100;
                thresholdMax = deviceSettings.humidityThresholdMax * 100;
                break;
            case AIR_QUALITY:
                if (airQualityReadingList == null || airQualityReadingList.isEmpty()) {
                    break;
                }

                enableMax = deviceSettings.sendAirQualityNotifications;
                thresholdMin = Float.MIN_VALUE;
                thresholdMax = deviceSettings.airQualityThreshold == 0 ? Float.MAX_VALUE :
                        airQualityConversion(deviceSettings.airQualityThreshold);
                break;
            default:
                break;
        }

        maxPoints.add(new PointValue(defaultMin, thresholdMax));
        maxPoints.add(new PointValue(defaultMax, thresholdMax));

        Line maxLine = new Line().setCubic(true).setHasPoints(false).setStrokeWidth(1).setColor(lightGreen);

        maxLine.setValues(maxPoints);

        GraphUserValueType valueType = getValueTypeForUser(graphType, null);
        isGraphAbnormalForUser = valueType == GraphUserValueType.ABNORMAL;

        if (enableMax && valueType != GraphUserValueType.INVALID)
            lines.add(maxLine);

        if (graphType != AIR_QUALITY) {
            minPoints.add(new PointValue(defaultMin, thresholdMin));
            minPoints.add(new PointValue(defaultMax, thresholdMin));

            Line minLine = new Line().setCubic(true).setStrokeWidth(1).setHasPoints(false)
                    .setColor(lightGreen);

            minLine.setValues(minPoints);

            if (enableMin && valueType != GraphUserValueType.INVALID)
                lines.add(minLine);
        }

        if (valueType != GraphUserValueType.INVALID)
            setUpGraphLabels(graphType);
        else
            resetGraph(graphType);

        if (isGraphAbnormalForUser) {
            setGraphColor(graphType, RED);
        } else {
            setGraphColor(graphType, GREEN);
        }
    }


    private boolean isValidReading(Reading lastReading) {
        if (lastReading == null || lastReading.value == Float.MIN_VALUE)
            return false;
        return true;
    }

    private void animateLinesAndCirclesAway(boolean upOrDown) {

        ValueAnimator alphaAnimation1 = ValueAnimator.ofObject(
                new FloatEvaluator(), upOrDown ? 0.0f : 1.0f, upOrDown ? 1.0f : 0.0f);

        alphaAnimation1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                float value = (float) animator.getAnimatedValue();
                binding.tempCircle.setAlpha(value);
                binding.tempLine.setAlpha(value);
                binding.humidityCircle.setAlpha(value);
                binding.humidityLine.setAlpha(value);
                binding.airQualityCircle.setAlpha(value);
                binding.airQualityLine.setAlpha(value);
            }
        });

        alphaAnimation1.start();
    }

    private void setupAirQualityGraph() {

        List<PointValue> values = new ArrayList<>();
        List<AxisValue> xAxisValues = new ArrayList<>();
        List<AxisValue> yAxisValues = new ArrayList<>();

        int total = 144;
        int i = 0;
        Date lastDate = airQualityReadingList.get(airQualityReadingList.size() - 1).created;

        Date firstDate = airQualityReadingList.get(0).created;

        long firstTime = firstDate.getTime();
        long lastTime = lastDate.getTime();


        long endDiff = firstTime - (DateUtil.getCurrentTime().getTime() - (60 * 60 * 24 * 1000));

        int offset = 0;
        if (endDiff < 1000 * 19 * 60.0) {
            total = airQualityReadingList.size();
        } else {
            long nowTime = DateUtil.getCurrentTime().getTime();

            float offsetPercent = (nowTime - lastTime) / (60.0f * 60.0f * 24.0f * 1000.0f);
            offset = total - airQualityReadingList.size();

            if (offsetPercent > (60 * 60) / (60.0f * 60.0f * 24.0f)) {
                offset += total * offsetPercent;
            }
        }

        for (Reading reading : airQualityReadingList) {
            float value = reading.value;
            if (value != Float.MIN_VALUE) {
                values.add(new PointValue(i + offset, airQualityConversion(value)));
            }
            i++;
        }

        long interval = 24 * 60 * 60 * 1000 / 6;

        for (i = 6; i >= 0; i--) {
            Date intervalDate = new Date(lastTime - (interval * (6 - i)));
            int x = (int) (total * ((float) i / 6.0f));
            String dateFormatted = DateUtil.convertDateToHour(intervalDate).toUpperCase();
            xAxisValues.add(new AxisValue(x).setLabel(dateFormatted));
        }
        //In most cased you can call data model methods in builder-pattern-like manner.
        final Line line = new Line(values).setCubic(true);
        line.setFilled(true);
        line.setHasPoints(false);
        List<Line> lines = new ArrayList<>();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);
        data.setBaseValue(Float.NEGATIVE_INFINITY);

        yAxisValues.add(
                new AxisValue(1.2f).setLabel(getString(R.string.very_abnormal).toUpperCase()));

        yAxisValues.add(new AxisValue(0.8f).setLabel(getString(R.string.abnormal).toUpperCase()));

        yAxisValues.add(new AxisValue(0.4f).setLabel(getString(R.string.normal).toUpperCase()));


        final Axis axisX = new Axis().setValues(xAxisValues).setTypeface(tf).setTextSize(10)
                .setMaxLabelChars(4).setInside(true).setHasLines(false).setHasSeparationLine(false);

        final Axis axisY = new Axis().setValues(yAxisValues).setInside(true).setTypeface(tf)
                .setTextSize(10).setHasSeparationLine(false).setMaxLabelChars(32);

        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        data.setAxisXBottom(axisX);


        binding.airQualityChart.setLineChartData(data);
        Viewport v = new Viewport(0, 1.35f, total - 1, -0.15f);

        binding.airQualityChart.setZoomEnabled(false);

        binding.airQualityChart.setMaximumViewport(v);
        binding.airQualityChart.setCurrentViewport(v);

        binding.airQualityChart.setValueSelectionEnabled(false);

        setUpLineChart(binding.airQualityChart);

        if (airQualityReadingList.size() > total - 10) {
            binding.airQualityLoadingTextView.setVisibility(View.GONE);
        } else {
            binding.airQualityLoadingTextView.setVisibility(View.VISIBLE);
            binding.airQualityLoadingTextView.setText(R.string.collecting_homehealth_data);
        }

        setUpUserThresholds(AIR_QUALITY, lines);
    }

    private void setUpLineChart(final LineChartView lineChartView) {


        final GestureDetector gestureDetector = new GestureDetector(getActivity(), new
                GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        setGraphLine(AIR_QUALITY, e.getX(), true);
                        setGraphLine(TEMP, e.getX(), true);
                        setGraphLine(HUMIDITY, e.getX(), true);
                        return true;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
                    }

                    @Override
                    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float
                            distanceY) {
                        binding.graphScrollView.setScrollingEnabled(false);

                        setGraphLine(AIR_QUALITY, e2.getX(), true);
                        setGraphLine(TEMP, e2.getX(), true);
                        setGraphLine(HUMIDITY, e2.getX(), true);
                        animateLinesAndCirclesAway(false);
                        return true;
                    }
                });

        lineChartView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                boolean handled = gestureDetector.onTouchEvent(event);
                if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
                    binding.graphScrollView.setScrollingEnabled(true);
                    return false;
                } else {

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        animateLinesAndCirclesAway(true);
                    }

                    return handled;
                }
            }
        });
    }

    private void setGraphLine(int graph, float x, boolean showSelection) {

        List<Reading> readings;
        TextView selectedReadingView;
        String symbol;
        CircleView circleView;
        View verticalLineView;
        LineChartView lineChartView;

        Customer customer = CurrentCustomer.getCurrentCustomer();

        switch (graph) {
            case TEMP:
                lineChartView = binding.temperatureChart;
                verticalLineView = binding.tempLine;
                circleView = binding.tempCircle;
                readings = tempReadingList;
                selectedReadingView = binding.temperatureReadingTextView;
                symbol = degreeSymbol + ((customer != null && !customer.celsius) ? "F" : "C");
                break;
            case HUMIDITY:
                lineChartView = binding.humidityChart;
                verticalLineView = binding.humidityLine;
                circleView = binding.humidityCircle;
                readings = humidityReadingList;
                selectedReadingView = binding.humidityReadingTextView;
                symbol = percentSymbol + "";
                break;
            case AIR_QUALITY:
                lineChartView = binding.airQualityChart;
                verticalLineView = binding.airQualityLine;
                circleView = binding.airQualityCircle;
                readings = airQualityReadingList;
                selectedReadingView = binding.airQualityReadingTextView;
                symbol = "";
                break;
            default:
                return;
        }

        if (readings == null || readings.size() == 0) {
            return;
        }

        float minPercentage = 0;
        float maxPercentage = 100;

        Date firstDate = readings.get(0).created;

        long firstTime = firstDate.getTime();

        long endDiff = (DateUtil.getCurrentTime().getTime() - (60 * 60 * 24 * 1000)) - firstTime;

        if (endDiff > 1000 * 19 * 60.0) {
            minPercentage = 1.0f - ((float) readings.size() / 144.0f);

            Date lastDate = readings.get(readings.size() - 1).created;

            long lastTime = lastDate.getTime();

            long nowTime = DateUtil.getCurrentTime().getTime();

            float offsetPercent = (nowTime - lastTime) / (60.0f * 60.0f * 24.0f * 1000.0f);

            if (offsetPercent > (60 * 60) / (60.0f * 60.0f * 24.0f)) {
                maxPercentage = 100 - offsetPercent;
                minPercentage -= maxPercentage;
            }

        }

        float percentage = x / binding.getRoot().getWidth();

        if (percentage < minPercentage) {
            percentage = minPercentage;
            x = minPercentage * lineChartView.getWidth();
        } else if (percentage > maxPercentage) {
            percentage = maxPercentage;
            x = maxPercentage * lineChartView.getWidth();
        }

        verticalLineView.setX(x - verticalLineView.getWidth() / 2);

        circleView.setX(x - circleView.getWidth() / 2);


        float normalizedPercentage = (percentage - minPercentage) / (1.0f - minPercentage);
        int index = Math.round(normalizedPercentage * (readings.size() - 1));


        Reading selectedReading = readings.get(index);


        float convertedTemp = selectedReading.value;

        if (convertedTemp == Float.MIN_VALUE) {
            circleView.setVisibility(View.GONE);
            return;
        }
        circleView.setVisibility(View.VISIBLE);

        setGraphColor(graph, getGraphColorForValue(graph, convertedTemp));
        if (graph == AIR_QUALITY) {

            convertedTemp = airQualityConversion(convertedTemp);
            //moving air quality point down a little to be right on the graph line
            convertedTemp -= 0.02;
        }

        if (graph == TEMP) {
            convertedTemp = UserUtils.getTemperatureInPreferredUnits(convertedTemp, needCelsius(), false);
        }

        positionViewOnGraph(circleView, convertedTemp, lineChartView);

        String formattedTemp;

        switch (graph) {
            case HUMIDITY:
            case TEMP:
                DecimalFormat decimalFormat = new DecimalFormat("#,##0");
                decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
                formattedTemp = decimalFormat.format(convertedTemp) + symbol;
                break;

            case AIR_QUALITY:
                if (convertedTemp > airQualityConversion(0.4f)) {
                    formattedTemp = getString(R.string.very_abnormal);
                } else if (convertedTemp > airQualityConversion(0.6f)) {
                    formattedTemp = getString(R.string.abnormal);
                } else {
                    formattedTemp = getString(R.string.normal);
                }
                break;
            default:
                return;

        }

        Date date = selectedReading.created;

        updateSelectedValue(selectedReadingView, formattedTemp, date);

        selectedReadingView.measure(0, 0);
        if (showSelection) {
            int margin = DensityUtil.dip2px(getActivity(), 23);
            float textViewX = x - selectedReadingView.getMeasuredWidth() / 2;
            if (textViewX < margin) {
                textViewX = margin;
            } else if (textViewX + selectedReadingView.getWidth() > binding.getRoot().getMeasuredWidth() -
                    margin) {
                textViewX = binding.getRoot().getWidth() - (selectedReadingView.getMeasuredWidth() + margin);
            }

            selectedReadingView.setX(textViewX);
        }
        if (showSelection) {
            circleView.setVisibility(View.VISIBLE);
            verticalLineView.setVisibility(View.VISIBLE);
        } else {
            circleView.setVisibility(View.GONE);
            verticalLineView.setVisibility(View.GONE);
        }

    }

    private void setGraphColor(final int graph, int colorType) {

        final CircleView tempCircle;
        final View tempLine;
        final LineChartView lineChartView;
        int lastColor;

        switch (graph) {
            case AIR_QUALITY:
                lineChartView = binding.airQualityChart;
                tempLine = binding.airQualityLine;
                tempCircle = binding.airQualityCircle;
                lastColor = previousAirQualityColor < 0 ? GREEN : previousAirQualityColor;
                break;
            case TEMP:
                lineChartView = binding.temperatureChart;
                tempLine = binding.tempLine;
                tempCircle = binding.tempCircle;
                lastColor = previousTempColor < 0 ? GREEN : previousTempColor;
                break;
            case HUMIDITY:
                lineChartView = binding.humidityChart;
                tempLine = binding.humidityLine;
                tempCircle = binding.humidityCircle;
                lastColor = previousHumidityColor < 0 ? GREEN : previousHumidityColor;
                break;
            default:
                return;
        }


        final Axis axisX = lineChartView.getLineChartData().getAxisYLeft();
        final Axis axisY = lineChartView.getLineChartData().getAxisXBottom();


        int lightColor;
        int darkColor;


        switch (colorType) {
            case GREEN:
                lightColor = lightGreen;
                darkColor = darkGreen;
                break;
            case RED:
                lightColor = lightRed;
                darkColor = darkRed;
                break;
            default:
                return;
        }


        int toDark = darkColor;
        int fromDark;
        int toLight = lightColor;
        int fromLight;


        if (lastColor == GREEN) {
            fromDark = darkGreen;
            fromLight = lightGreen;
        } else {
            fromDark = darkRed;
            fromLight = lightRed;
        }

        List<Line> lines = lineChartView.getLineChartData().getLines();
        List<Line> userValueLines = new ArrayList<>();
        Line mainLine = null;
        for (int i = 0; i < lines.size(); i++) {
            if (i == 0)
                mainLine = lines.get(i);
            else
                userValueLines.add(lines.get(i));
        }

        final List<Line> immutableUserLines = userValueLines;
        final Line immutableMainLine = mainLine;

        ValueAnimator colorAnimationDark = ValueAnimator.ofObject(new ArgbEvaluator(), fromDark,
                toDark);
        colorAnimationDark.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                int value = (int) animator.getAnimatedValue();

                axisX.setTextColor(value);
                axisY.setTextColor(value);

                immutableMainLine.setColor(value);

                lineChartView.getAxesRenderer().onChartDataChanged();

                tempCircle.setBackgroundColor(value);
                tempLine.setBackgroundColor(value);

                if (graph == TEMP) {
                    binding.tempUserMaxLabel.setTextColor(value);
                    binding.tempUserMinLabel.setTextColor(value);
                } else if (graph == HUMIDITY) {
                    binding.humidityUserMaxLabel.setTextColor(value);
                    binding.humidityUserMinLabel.setTextColor(value);
                } else
                    binding.airQualityUserMaxLabel.setTextColor(value);
            }

        });
        colorAnimationDark.setDuration(250);
        colorAnimationDark.start();

        ValueAnimator colorAnimationLight = ValueAnimator.ofObject(new ArgbEvaluator(),
                fromLight, toLight);
        colorAnimationLight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                int value = (int) animator.getAnimatedValue();
                lineChartView.setBackgroundColor(value);

                for (Line line : immutableUserLines)
                    line.setColor(value);

            }

        });
        colorAnimationLight.setDuration(250);
        colorAnimationLight.start();

        switch (graph) {
            case AIR_QUALITY:
                previousAirQualityColor = colorType;
                break;
            case TEMP:
                previousTempColor = colorType;
                break;
            case HUMIDITY:
                previousHumidityColor = colorType;
                break;
        }
    }

    private int getGraphColorForValue(int graphType, Float value) {
        if (deviceSettings == null || !deviceSettings.sendHomehealthNotifications)
            return GREEN;
        return getValueTypeForUser(graphType, value) == GraphUserValueType.ABNORMAL ? RED : GREEN;
    }

    private GraphUserValueType getValueTypeForUser(int graphType, Float value) {
        float normalizedLastReadingValue;
        GraphUserValueType graphUserValueType;

        float thresholdMin, thresholdMax, valueToCheck;

        switch (graphType) {
            case TEMP:
                thresholdMin = UserUtils.getTemperatureInPreferredUnits(deviceSettings
                        .tempThresholdMin, needCelsius());
                thresholdMax = UserUtils.getTemperatureInPreferredUnits(deviceSettings
                        .tempThresholdMax, needCelsius());
                if (value == null) {
                    Reading lastTemp = tempReadingList.get(tempReadingList.size() - 1);
                    if (!isValidReading(lastTemp)) {
                        return GraphUserValueType.INVALID;
                    }
                    valueToCheck = lastTemp.value;
                } else
                    valueToCheck = value;
                normalizedLastReadingValue =
                        UserUtils.getTemperatureInPreferredUnits(valueToCheck, needCelsius());
                break;
            case HUMIDITY:
                thresholdMin = deviceSettings.humidityThresholdMin * 100;
                thresholdMax = deviceSettings.humidityThresholdMax * 100;
                if (value == null && humidityReadingList != null) {
                    Reading lastHumidity = humidityReadingList.get(humidityReadingList.size() - 1);
                    if (!isValidReading(lastHumidity)) {
                        return GraphUserValueType.INVALID;
                    }
                    valueToCheck = lastHumidity.value;
                } else
                    valueToCheck = value;

                normalizedLastReadingValue = valueToCheck;
                break;
            case AIR_QUALITY:
                thresholdMin = Float.MIN_VALUE;
                thresholdMax = deviceSettings.airQualityThreshold == 0 ? Float.MAX_VALUE :
                        airQualityConversion(deviceSettings.airQualityThreshold);
                if (value == null) {
                    Reading lastAirQuality = airQualityReadingList.get(airQualityReadingList.size() - 1);
                    if (!isValidReading(lastAirQuality)) {
                        return GraphUserValueType.INVALID;
                    }
                    valueToCheck = lastAirQuality.value;
                } else
                    valueToCheck = value;
                normalizedLastReadingValue = airQualityConversion(valueToCheck);
                break;
            default:
                return GraphUserValueType.NORMAL;
        }

        graphUserValueType = normalizedLastReadingValue > thresholdMax ?
                GraphUserValueType.ABNORMAL : GraphUserValueType.NORMAL;

        if (graphType != AIR_QUALITY && graphUserValueType == GraphUserValueType.NORMAL)
            graphUserValueType = normalizedLastReadingValue < thresholdMin ?
                    GraphUserValueType.ABNORMAL : GraphUserValueType.NORMAL;

        return graphUserValueType;
    }


    private void updateSelectedValue(TextView textView, String value, Date date) {
        String time = DateUtil.utcDateToDisplayString(date).toLowerCase();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        String reading;
        if (hour == 1 || (hour == 13 && !DateUtil.is24HourFormat())) {
            reading = getString(R.string.at_one_home_health, value, time);
        } else {
            reading = getString(R.string.at_home_health, value, time);
        }
        setText(textView, reading);
    }

    private void setText(TextView textView, String value) {
        textView.setText(value);
    }

    private void setUpGraphLabels(int graphType) {
        if (deviceSettings == null)
            return;

        if (!deviceSettings.sendHomehealthNotifications) {
            resetGraph(graphType);
            return;
        }

        switch (graphType) {
            case TEMP:
                if (deviceSettings.sendTempMaxNotifications) {
                    binding.tempUserMaxLabel.setVisibility(View.VISIBLE);
                    float normalizedMaxTemp = UserUtils.getTemperatureInPreferredUnits
                            (deviceSettings.tempThresholdMax, needCelsius());
                    positionViewOnGraph(binding.tempUserMaxLabel, normalizedMaxTemp, binding.temperatureChart);
                } else
                    binding.tempUserMaxLabel.setVisibility(View.GONE);

                if (deviceSettings.sendTempMinNotifications) {
                    binding.tempUserMinLabel.setVisibility(View.VISIBLE);
                    float normalizedMinTemp = UserUtils.getTemperatureInPreferredUnits
                            (deviceSettings.tempThresholdMin, needCelsius());
                    positionViewOnGraph(binding.tempUserMinLabel, normalizedMinTemp, binding.temperatureChart);
                } else
                    binding.tempUserMinLabel.setVisibility(View.GONE);

                break;

            case HUMIDITY:
                if (deviceSettings.sendHumidityMaxNotifications) {
                    binding.humidityUserMaxLabel.setVisibility(View.VISIBLE);
                    float normalizedMaxHumidity = deviceSettings.humidityThresholdMax * 100;
                    positionViewOnGraph(binding.humidityUserMaxLabel, normalizedMaxHumidity, binding.humidityChart);
                } else
                    binding.humidityUserMaxLabel.setVisibility(View.GONE);

                if (deviceSettings.sendHumidityMinNotifications) {
                    binding.humidityUserMinLabel.setVisibility(View.VISIBLE);
                    float normalizedMinHumidity = deviceSettings.humidityThresholdMin * 100;
                    positionViewOnGraph(binding.humidityUserMinLabel, normalizedMinHumidity, binding.humidityChart);
                } else
                    binding.humidityUserMinLabel.setVisibility(View.GONE);

                break;

            case AIR_QUALITY:
                if (deviceSettings.sendAirQualityNotifications) {
                    binding.airQualityUserMaxLabel.setVisibility(View.VISIBLE);
                    positionViewOnGraph(binding.airQualityUserMaxLabel, airQualityConversion(deviceSettings
                            .airQualityThreshold), binding.airQualityChart);
                } else
                    binding.airQualityUserMaxLabel.setVisibility(View.GONE);

                break;
        }
    }

    private void positionViewOnGraph(final View viewToPosition, final float yOffset,
                                     final LineChartView graph) {

        viewToPosition.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        viewToPosition.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        Viewport v = graph.getCurrentViewport();
                        float yPercentage = (yOffset - v.bottom) / (v.top - v.bottom);
                        float y = graph.getHeight() - ((graph.getHeight() * yPercentage) +
                                (viewToPosition.getHeight() / 2));
                        viewToPosition.setY(y);
                    }
                });
    }

    private void resetGraph(int graphType) {
        setGraphColor(graphType, GREEN);
        switch (graphType) {
            case TEMP:
                binding.tempUserMaxLabel.setVisibility(View.GONE);
                binding.tempUserMinLabel.setVisibility(View.GONE);
                break;
            case HUMIDITY:
                binding.humidityUserMaxLabel.setVisibility(View.GONE);
                binding.humidityUserMinLabel.setVisibility(View.GONE);
                break;
            case AIR_QUALITY:
                binding.airQualityUserMaxLabel.setVisibility(View.GONE);
                break;
        }
    }


    private class GraphData {
        List<Reading> readingList;
        DeviceSettings deviceSettings;
        Location location;
    }

    private class GetReading extends AsyncTask<String, Object, GraphData> {

        @Override
        protected GraphData doInBackground(String... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }

            GraphData data = new GraphData();
            data.readingList = new ArrayList<>();

            if (getActivity() == null)
                return data;

            String where = CanaryReadingContentProvider.COLUMN_SENSOR_ID + " == ?";
            where += " AND ";
            where += CanaryReadingContentProvider.COLUMN_DEVICE_UUID + " == ?";

            String sensorId = params[0];

            String deviceToGetSettings = null;
            if (params.length == 2)
                deviceToGetSettings = params[1];

            Log.i(LOG_TAG, "Device uri " + deviceUri);

            String[] whereArgs = {sensorId, Utils.getStringFromResourceUri(deviceUri)};

            ContentResolver contentResolver = getActivity().getContentResolver();
            Cursor cursor = contentResolver.query(CanaryReadingContentProvider.CONTENT_URI, null,
                    where, whereArgs, CanaryReadingContentProvider.COLUMN_CREATED + " ASC");


            if (cursor != null) {
                Log.i(LOG_TAG, "sensor readings " + cursor.getCount());
                if (cursor.moveToLast()) {

                    Reading lastReading = new Reading();
                    lastReading.created = new Date();
                    do {
                        Reading reading = ReadingDatabaseService.cursorToReading(cursor);

                        while (!Utils.isDemo() && lastReading.created.getTime() - reading.created.getTime() > 1900 * 60 * 10) {
                            Reading placeHolderReading = new Reading();
                            placeHolderReading.created = new Date(lastReading.created.getTime() - 1000 * 60 * 10);
                            placeHolderReading.value = Float.MIN_VALUE;
                            data.readingList.add(0, placeHolderReading);
                            lastReading = placeHolderReading;
                        }
                        lastReading = reading;
                        data.readingList.add(0, reading);
                    } while (cursor.moveToPrevious());
                }
                cursor.close();
            }


            if (!StringUtils.isNullOrEmpty(deviceToGetSettings)) {

                Device device = DeviceDatabaseService.getDeviceFromResourceUri(deviceUri);
                try {
                    data.deviceSettings = DeviceAPIService.getDeviceSettings(device.id);
                } catch (Exception e) {
                    Log.e(LOG_TAG, e.getMessage());
                    data.deviceSettings = DeviceDatabaseService.getDeviceSettingsForDevice(device.id);
                }
                data.location = LocationDatabaseService.getLocationFromResourceUri(device.location);
            }
            return data;
        }
    }

    private class GetTemp extends GetReading {

        @Override
        protected void onPostExecute(GraphData data) {

            location = data.location;
            deviceSettings = data.deviceSettings;
            tempReadingList = data.readingList;

            if (tempReadingList.size() == 0) {
                return;
            }

            setupTemperatureGraph();
            binding.temperatureChart.setVisibility(View.VISIBLE);


            binding.tempChartBackground.setBackgroundColor
                    (ContextCompat.getColor(getContext(), R.color.white));
            setGraphLine(TEMP, binding.getRoot().getWidth(), false);

        }
    }

    private class GetHumidity extends GetReading {

        @Override
        protected void onPostExecute(GraphData data) {

            humidityReadingList = data.readingList;

            if (humidityReadingList.size() == 0) {
                return;
            }
            setupHumidityGraph();
            binding.humidityChart.setVisibility(View.VISIBLE);

            binding.humidityChartBackground.setBackgroundColor
                    (ContextCompat.getColor(getContext(), R.color.white));
            setGraphLine(HUMIDITY, binding.getRoot().getWidth(), false);
        }
    }

    private class GetAirQuality extends GetReading {

        @Override
        protected void onPostExecute(GraphData data) {

            airQualityReadingList = data.readingList;

            if (airQualityReadingList.size() == 0) {
                return;
            }

            setupAirQualityGraph();
            binding.airQualityChart.setVisibility(View.VISIBLE);
            binding.airQualityChart.setBackgroundColor(ContextCompat.getColor(getContext(), R.color
                    .transparent));

            binding.airQualityChartBackground.setBackgroundColor
                    (ContextCompat.getColor(getContext(), R.color.white));
            setGraphLine(AIR_QUALITY, binding.getRoot().getWidth(), false);
        }
    }

    private boolean needCelsius() {
        return CurrentCustomer.getCurrentCustomer() != null && CurrentCustomer.getCurrentCustomer
                ().celsius;
    }

    private float airQualityConversion(float value) {


        float adjustedValue;

        if (value <= 0.4) {
            adjustedValue = value;
        } else if (value <= 0.6) {
            adjustedValue = ((value - 0.4f) * 2) + 0.4f;
        } else {
            adjustedValue = value + 0.2f;
        }

        return 1.2f - adjustedValue;

    }


    /**
     * Function that is called when the right button is click in the header
     */
    public void showSettings() {
        if (location == null)
            return;

        Intent settingsIntent = new Intent(getActivity(), SettingsFragmentStackActivity.class);
        Bundle extras = new Bundle();
        extras.putInt(SettingsFragmentStackActivity.extra_homehealth_settings, location.id);
        settingsIntent.putExtras(extras);

        getActivity().startActivity(settingsIntent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.scale_alpha_push);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {

        if (nextAnim == 0x0)
            return super.onCreateAnimation(transit, enter, nextAnim);

        Animation anim = AnimationUtils.loadAnimation(getActivity(), nextAnim);

        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (TutorialUtil.getTutorialInProgress() == TutorialType.HOME) {
                    startSlideAnimations();
                }
            }
        });

        return anim;
    }

    private void startSlideAnimations() {
        startAnimationForFakeGraph(binding.fakeGraphGradient, binding.tutorialFrameGraph);
        startAnimationForFakeGraph(binding.fakeGraphGradientTwo, binding.tutorialFrameGraphTwo);
        startAnimationForFakeGraph(binding.fakeGraphGradientThree, binding.tutorialFrameGraphThree);
    }

    private void startAnimationForFakeGraph(View fakeGradient, final View fakeLayout) {

        float percent = (float) (fakeGradient.getWidth() - binding.getRoot().getWidth()) / (float)
                fakeGradient.getWidth();


        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                -percent, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(1500);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AnimationHelper.fadeViewOut(fakeLayout, 200);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        fakeGradient.startAnimation(animation);
    }

    enum GraphUserValueType {
        NORMAL,
        ABNORMAL,
        INVALID
    }

}