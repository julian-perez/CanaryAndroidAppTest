package is.yranac.canary.fragments.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.adapter.SettingsSingleSelectionAdapter;
import is.yranac.canary.databinding.FragmentSettingsDeviceNamingBinding;
import is.yranac.canary.databinding.ListrawDeviceNamingFooterBinding;
import is.yranac.canary.fragments.setup.OTAFragment;
import is.yranac.canary.fragments.setup.PlacementSuggestionsFragment;
import is.yranac.canary.fragments.setup.SetUpBaseFragment;
import is.yranac.canary.messages.ResumeFragments;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.services.api.DeviceAPIService;
import is.yranac.canary.ui.BaseActivity;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_DEVICE_NAME;
import static is.yranac.canary.util.ga.AnalyticsConstants.SETTINGS_TYPE_DEVICE;
import static is.yranac.canary.util.ga.AnalyticsConstants.SETTINGS_UPDATE_NAME;

/**
 * Created by sergeymorozov on 6/4/15.
 */
public class DeviceNamingFragment extends SetUpBaseFragment {


    public static final String TAG = "device_naming_fragment";

    private FragmentSettingsDeviceNamingBinding binding;
    private ListrawDeviceNamingFooterBinding footerBinding;

    private String[] predefinedDeviceNames;
    private String selectedDeviceName;
    private String currentDeviceName;
    private boolean popOnResume = false;


    public static DeviceNamingFragment newInstance(Bundle args, boolean isOutdoor) {
        DeviceNamingFragment f = new DeviceNamingFragment();
        Bundle bundle = new Bundle();
        bundle.putAll(args);
        bundle.putBoolean(key_isOutdoor, isOutdoor);
        f.setArguments(bundle);

        return f;
    }

    public static DeviceNamingFragment newInstance(Bundle args, int deviceType, int deviceID, String resourceURI, boolean isSetup, boolean isOutdoor, String currentDeviceName) {
        DeviceNamingFragment f = new DeviceNamingFragment();
        Bundle bundle = new Bundle();
        bundle.putAll(args);
        bundle.putInt(key_deviceID, deviceID);
        bundle.putInt(device_type, deviceType);
        bundle.putBoolean(key_isSetup, isSetup);
        bundle.putBoolean(key_isOutdoor, isOutdoor);
        bundle.putString(key_currentDeviceName, currentDeviceName);
        bundle.putString(device_uri, resourceURI);
        f.setArguments(bundle);

        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsDeviceNamingBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final int deviceType = getArguments().getInt(device_type);
        if (isSetup()) {
            if (deviceType == DeviceType.FLEX) {
                fragmentStack.enableBackButton();
            } else {
                fragmentStack.disableBackButton();
            }
        }

        binding.setIsSetup(isSetup());
        final ListView listView = binding.listView;
        View customNameView = getLayoutInflater(savedInstanceState).inflate(R.layout.listraw_device_naming_footer, null);


        footerBinding = DataBindingUtil.bind(customNameView);

        footerBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.setItemChecked(listView.getCheckedItemPosition(), false);
                selectFooter(true);
                selectedDeviceName = footerBinding.nameCustom.getEditableText().toString();
                enableRightButton();
                footerBinding.nameCustom.requestFocus();
            }
        });
        footerBinding.nameCustom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    listView.setItemChecked(listView.getCheckedItemPosition(), false);
                    selectFooter(true);
                    selectedDeviceName = footerBinding.nameCustom.getEditableText().toString();
                    enableRightButton();
                }
            }
        });

        footerBinding.nameCustom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                selectedDeviceName = s.toString();
                enableRightButton();
            }
        });
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listView.setItemChecked(position, true);
                selectedDeviceName = getPredefinedNames()[position - 1];
                enableRightButton();
                selectFooter(false);
            }
        });

        //close keyboard when user scrolls up
        binding.listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            int mLastFirstVisibleItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mLastFirstVisibleItem > firstVisibleItem) {
                    if (footerBinding.nameCustom != null)
                        footerBinding.nameCustom.clearFocus();
                    hideSoftKeyboard();
                }
                mLastFirstVisibleItem = firstVisibleItem;
            }
        });

        binding.listView.addHeaderView(customNameView);
        binding.listView.setAdapter(new SettingsSingleSelectionAdapter(getActivity(), getPredefinedNames(), R.layout.setting_row_radio));
        selectedDeviceName = getCurrentDeviceName();
        int headerString = 0;
        switch (deviceType) {
            case DeviceType.CANARY_AIO:
                headerString = R.string.give_your_canary_a_name;
                break;
            case DeviceType.FLEX:
                headerString = R.string.give_your_flex_a_name;
                break;
            case DeviceType.CANARY_VIEW:
                headerString = R.string.give_your_canary_view_a_name;
                break;
        }
        binding.headerTitleTextView.setText(headerString);
        binding.headerTitleTextViewSmall.setText(headerString);

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDeviceName();
            }
        });
    }

    private void selectFooter(boolean select) {


        if (footerBinding == null)
            return;
        footerBinding.checkMark.setChecked(select);

        if (!select) {
            footerBinding.nameCustom.clearFocus();
            hideSoftKeyboard();
        }
    }


    String[] getPredefinedNames() {
        if (predefinedDeviceNames == null) {
            if (isOutdoor()) {
                predefinedDeviceNames = getResources().getStringArray(R.array.outdoor_device_names);
            } else {
                predefinedDeviceNames = getResources().getStringArray(R.array.indoor_device_names);

            }
        }
        return predefinedDeviceNames;
    }


    boolean isOutdoor() {
        return getArguments() != null && getArguments().getBoolean(key_isOutdoor, false);
    }

    private String getCurrentDeviceName() {
        if (currentDeviceName == null)
            if (getArguments() != null) {
                currentDeviceName = getArguments().getString(key_currentDeviceName, null);
            }
        return currentDeviceName;
    }

    private int getDeviceID() {
        if (getArguments() == null || !getArguments().containsKey(key_deviceID))
            return -1;
        return getArguments().getInt(key_deviceID);
    }

    private String getResourceURI() {
        if (getArguments() == null)
            return null;
        return getArguments().getString(device_uri, null);
    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_DEVICE_NAME;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isSetup()) {
            binding.nextBtn.setText(R.string.next);
        } else {
            binding.nextBtn.setText(R.string.save);
        }

        ListView listView = binding.listView;
        enableRightButton();
        fragmentStack.setHeaderTitle(R.string.name);

        if (!TextUtils.isEmpty(getCurrentDeviceName())) {
            ArrayList<String> names = new ArrayList<>(Arrays.asList(getPredefinedNames()));
            int currentNameIndex = names.indexOf(getCurrentDeviceName());
            if (currentNameIndex < 0) {
                listView.setItemChecked(listView.getCheckedItemPosition(), false);
                footerBinding.nameCustom.setText(getCurrentDeviceName());
                selectFooter(true);
            } else {
                listView.setItemChecked(currentNameIndex + 1, true);
                listView.smoothScrollToPosition(currentNameIndex < 0 ? getPredefinedNames().length : currentNameIndex);
            }
        }

        fragmentStack.showHelpButton();
        fragmentStack.enableRightButton(this, true);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStart() {
        super.onStart();
        TinyMessageBus
                .register(this);
    }

    @Override
    public void onStop() {
        TinyMessageBus
                .unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onResumeFragments(ResumeFragments resumeFragments) {
        if (popOnResume)
            fragmentStack.popBackStack(EditDeviceFragment.class);
    }

    void showWarningAlert(String message) {
        AlertDialog alertDialog = AlertUtils.showGenericAlert(getActivity(), message);

        if (alertDialog != null) {
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
        }
    }


    @Override
    public void onRightButtonClick() {
        PlacementSuggestionsFragment fragment = PlacementSuggestionsFragment.newInstance(getArguments(), true);
        addModalFragment(fragment);
    }

    private void saveDeviceName() {
        final String nameToSave = selectedDeviceName;


        final String resourceURI = getResourceURI();
        if (resourceURI != null) {

            showLoading(true, getString(R.string.loading_dialog));
            DeviceAPIService.changeDeviceName(resourceURI, getDeviceID(), nameToSave, new Callback<Void>() {
                @Override
                public void success(Void aVoid, Response response) {
                    String uuid = Utils.getStringFromResourceUri(resourceURI);
                    GoogleAnalyticsHelper.trackSettingsEvent(SETTINGS_UPDATE_NAME, SETTINGS_TYPE_DEVICE, 0, uuid, 0, getCurrentDeviceName(), selectedDeviceName);
                    showLoading(false, getString(R.string.loading_dialog));
                    if (isSetup()) {
                        OTAFragment fragment = getInstance(OTAFragment.class);
                        addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
                    } else {
                        BaseActivity baseActivity = (BaseActivity) getActivity();
                        if (isVisible() || !baseActivity.isPaused())
                            fragmentStack.popBackStack(AboutDeviceFragment.class);
                        else
                            popOnResume = true;
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    showLoading(false, getActivity().getString(R.string.loading_dialog));
                    showWarningAlert(error.getMessage());
                }
            });

        }
    }


    private void enableRightButton() {

        binding.nextBtn.setEnabled(
                !TextUtils.isEmpty(selectedDeviceName) &&
                        (!selectedDeviceName.equalsIgnoreCase(getCurrentDeviceName()) || isSetup()));
    }

}
