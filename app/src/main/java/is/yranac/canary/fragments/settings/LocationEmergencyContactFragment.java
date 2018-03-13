package is.yranac.canary.fragments.settings;

import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.adapter.SettingsAdapter;
import is.yranac.canary.model.SettingsObject;
import is.yranac.canary.model.emergencycontacts.EmergencyContact;
import is.yranac.canary.model.emergencycontacts.ReplaceEmergencyNumbers;
import is.yranac.canary.services.api.EmergencyContctsAPIService;
import is.yranac.canary.services.database.EmergencyContactDatabaseService;
import is.yranac.canary.ui.BaseActivity;
import is.yranac.canary.ui.views.CanaryTextWatcher;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.TinyMessageBus;

import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_LOCATION_EMERGENCY_CONTACT;

/**
 * Fragment that allows the user to see/edit the emergency contact information of the
 * current location
 */
public class LocationEmergencyContactFragment extends SettingsFragment {
    private static final String LOG_TAG = "LocationEmergencyContactFragment";

    private static final int OPTION_POLICE = 0;
    private static final int OPTION_HOSPITAL = 1;
    private static final int OPTION_FIRE = 2;

    private List<SettingsObject> settingsObjects;
    private SettingsAdapter adapter;


    private int locationId;

    private ListView listView;

    private Map<EmergencyContact.ContactType, EmergencyContact> emergencyContacts;

    private Map<Integer, String> initialValueMap = new HashMap<>();
    private Map<Integer, String> currentValueMap = new HashMap<>();
    private AlertDialog mDialog;

    /**
     * Factory Method to create a new LocationEmergencyContactFragment for a particular location
     *
     * @param locationId ID of the location that we are edit the emergency contacts
     * @return New LocationEmergencyContactFragment Object
     */
    public static LocationEmergencyContactFragment newInstance(int locationId) {
        Bundle args = new Bundle();
        args.putInt(location_id, locationId);
        LocationEmergencyContactFragment fragment = new LocationEmergencyContactFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings_emergency_contacts, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationId = getArguments().getInt(location_id);
        listView = (ListView) view.findViewById(R.id.list_view);

        emergencyContacts = EmergencyContactDatabaseService.getEmergencyContacts(locationId);
        Log.i(LOG_TAG, String.valueOf(emergencyContacts.size()));

        fragmentStack.showRightButton(R.string.save);
        fragmentStack.setHeaderTitle(R.string.emergency_numbers_header);
        settingsObjects = getSettings();
        adapter = new SettingsAdapter(getActivity(),
                settingsObjects,
                settingsAdapterCallback);
        listView.setAdapter(adapter);


        Button updateBtn = (Button) view.findViewById(R.id.sync_btn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertUtils.showEmergencyContactUpdateAlert(getActivity(), locationId, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog = AlertUtils.initLoadingDialog(getActivity(), getActivity().getString(R.string.syncing));
                        mDialog.show();
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        TinyMessageBus.register(this);
        enableRightButton(settingsHaveChanged());
    }

    @Override
    public void onPause() {
        super.onPause();
        TinyMessageBus.unregister(this);
    }

    @Subscribe
    public void onReplaceEmergencyNumbers(ReplaceEmergencyNumbers replaceEmergencyNumbers) {

        String policeNumber = replaceEmergencyNumbers.policeNumber;
        String hospitalNumber = replaceEmergencyNumbers.emsNumber;
        String fireNumber = replaceEmergencyNumbers.fireNumber;

        settingsObjects.get(OPTION_POLICE).editableText = policeNumber;
        settingsObjects.get(OPTION_HOSPITAL).editableText = hospitalNumber;
        settingsObjects.get(OPTION_FIRE).editableText = fireNumber;
        adapter.notifyDataSetChanged();

        currentValueMap.put(OPTION_POLICE, policeNumber);
        currentValueMap.put(OPTION_HOSPITAL, hospitalNumber);
        currentValueMap.put(OPTION_FIRE, fireNumber);

        enableRightButton(settingsHaveChanged());

        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }

    }

    @Override
    public void onRightButtonClick() {
        BaseActivity baseActivity = (BaseActivity) getActivity();
        if (!baseActivity.hasInternetConnection())
            return;

        hideSoftKeyboard();

        patchNumbers();

        getActivity().onBackPressed();
    }

    private void patchNumbers() {

        SettingsAdapter adapter = (SettingsAdapter) listView.getAdapter();
        String policeNumber = PhoneNumberUtils.stripSeparators(adapter.getItem(OPTION_POLICE).editableText);
        String fireNumber = PhoneNumberUtils.stripSeparators(adapter.getItem(OPTION_FIRE).editableText);
        String hospitalNumber = PhoneNumberUtils.stripSeparators(adapter.getItem(OPTION_HOSPITAL).editableText);


        Log.i(LOG_TAG, policeNumber + ", " + fireNumber + ", " + hospitalNumber);
        if (emergencyContacts.containsKey(EmergencyContact.ContactType.ems)) {
            EmergencyContact contact = emergencyContacts.get(EmergencyContact.ContactType.ems);
            if (!contact.phoneNumber.equalsIgnoreCase(hospitalNumber)) {
                contact.phoneNumber = hospitalNumber;
                contact.provider = "user";
                EmergencyContctsAPIService.changeEmergencyContactAsync(contact);
            }
        }

        if (emergencyContacts.containsKey(EmergencyContact.ContactType.police)) {
            EmergencyContact contact = emergencyContacts.get(EmergencyContact.ContactType.police);
            if (!contact.phoneNumber.equalsIgnoreCase(policeNumber)) {
                contact.phoneNumber = policeNumber;
                contact.provider = "user";
                EmergencyContctsAPIService.changeEmergencyContactAsync(contact);
            }
        }

        if (emergencyContacts.containsKey(EmergencyContact.ContactType.fire)) {
            EmergencyContact contact = emergencyContacts.get(EmergencyContact.ContactType.fire);
            if (!contact.phoneNumber.equalsIgnoreCase(fireNumber)) {
                contact.phoneNumber = fireNumber;
                contact.provider = "user";
                EmergencyContctsAPIService.changeEmergencyContactAsync(contact);
            }
        }


    }

    private List<SettingsObject> getSettings() {

        String policeNumber = "";
        String fireNumber = "";
        String hospitalNumber = "";

        if (emergencyContacts.containsKey(EmergencyContact.ContactType.police)) {
            policeNumber = emergencyContacts.get(EmergencyContact.ContactType.police).phoneNumber;
        }

        if (emergencyContacts.containsKey(EmergencyContact.ContactType.fire)) {
            fireNumber = emergencyContacts.get(EmergencyContact.ContactType.fire).phoneNumber;

        }

        if (emergencyContacts.containsKey(EmergencyContact.ContactType.ems)) {
            hospitalNumber = emergencyContacts.get(EmergencyContact.ContactType.ems).phoneNumber;

        }

        initialValueMap.put(OPTION_POLICE, policeNumber);
        initialValueMap.put(OPTION_HOSPITAL, hospitalNumber);
        initialValueMap.put(OPTION_FIRE, fireNumber);

        currentValueMap.put(OPTION_POLICE, policeNumber);
        currentValueMap.put(OPTION_HOSPITAL, hospitalNumber);
        currentValueMap.put(OPTION_FIRE, fireNumber);
        List<SettingsObject> settingsArray = new ArrayList<>();

        settingsArray.add(
                SettingsObject.editableText(
                        getString(R.string.police_department), getString(R.string.police_department), policeNumber, InputType.TYPE_CLASS_PHONE, CanaryTextWatcher.VALID_PHONE)
        );
        settingsArray.add(SettingsObject.editableText(getString(R.string.emergency_medical_services), getString(R.string.emergency_medical_services), hospitalNumber, InputType.TYPE_CLASS_PHONE, CanaryTextWatcher.VALID_PHONE));
        settingsArray.add(SettingsObject.editableText(getString(R.string.fire_department), getString(R.string.fire_department), fireNumber, InputType.TYPE_CLASS_PHONE, CanaryTextWatcher.VALID_PHONE));

        return settingsArray;
    }

    private SettingsAdapter.SettingsAdapterCallback settingsAdapterCallback = new SettingsAdapter.SimpleSettingsAdapterCallback() {
        @Override
        public void onOptionSelected(int position) {
            switch (position) {
                case OPTION_POLICE:
                    currentValueMap.put(OPTION_POLICE, settingsObjects.get(OPTION_POLICE).editableText);
                    break;
                case OPTION_HOSPITAL:
                    currentValueMap.put(OPTION_HOSPITAL, settingsObjects.get(OPTION_HOSPITAL).editableText);
                    break;
                case OPTION_FIRE:
                    currentValueMap.put(OPTION_FIRE, settingsObjects.get(OPTION_FIRE).editableText);
                    break;
            }
            enableRightButton(settingsHaveChanged());
        }
    };

    private void enableRightButton(boolean enable) {
        fragmentStack.enableRightButton(this, enable);
    }

    private boolean settingsHaveChanged() {
        for (Map.Entry<Integer, String> initialValue : initialValueMap.entrySet()) {
            if (valuesAreDifferent(initialValue.getValue(), currentValueMap.get(initialValue.getKey()))) {
                return true;
            }
        }

        return false;
    }

    private boolean valuesAreDifferent(String initialValue, String newValue) {

        if (initialValue == null && newValue != null) {
            return true;
        }

        if (initialValue != null && newValue == null) {
            return true;
        }

        if (initialValue != null && !initialValue.equals(newValue)) {
            return true;
        }

        return false;
    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_LOCATION_EMERGENCY_CONTACT;
    }
}
