package is.yranac.canary.services.intent;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.util.Map;

import is.yranac.canary.CanaryApplication;
import is.yranac.canary.model.emergencycontacts.EmergencyContact;
import is.yranac.canary.model.emergencycontacts.ForceUpdateEmergencyNumbers;
import is.yranac.canary.model.emergencycontacts.ReplaceEmergencyNumbers;
import is.yranac.canary.model.google.GooglePlace;
import is.yranac.canary.model.google.GooglePlaceDetailResponse;
import is.yranac.canary.model.google.GooglePlaceDetails;
import is.yranac.canary.model.google.GooglePlacesResponse;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.services.api.EmergencyContctsAPIService;
import is.yranac.canary.services.api.google.GooglePlacesAPIService;
import is.yranac.canary.services.database.EmergencyContactDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.util.TinyMessageBus;
import retrofit.RetrofitError;

import static is.yranac.canary.services.intent.GooglePlacesAPIIntentServiceEmergencyNumbers.EmergencyNumbers.FIRE;
import static is.yranac.canary.services.intent.GooglePlacesAPIIntentServiceEmergencyNumbers.EmergencyNumbers.HOSPITAL;
import static is.yranac.canary.services.intent.GooglePlacesAPIIntentServiceEmergencyNumbers.EmergencyNumbers.POLICE;

/**
 * Created by Schroeder on 9/16/14.
 */
public class GooglePlacesAPIIntentServiceEmergencyNumbers extends IntentService {

    enum EmergencyNumbers {
        POLICE,
        FIRE,
        HOSPITAL
    }

    private static final String TAG = "GooglePlacesAPIIntentServiceEmergencyNumbers";

    private static Context context = CanaryApplication.getContext();

    private static final String ACTION_EMERGENCY_NUMBERES = "is.yranac.canary.services.action.ACTION_EMERGENCY_NUMBERS";
    private static final String ACTION_NEW_EMERGENCY_NUMBERES = "is.yranac.canary.services.action.ACTION_NEW_EMERGENCY_NUMBERES";
    private static final String EXTRA_LOCATION = "is.yranac.canary.services.extra.LOCATION";
    private static final String EXTRA_SAVE = "is.yranac.canary.services.extra.SAVE";

    public GooglePlacesAPIIntentServiceEmergencyNumbers() {
        super(TAG);
    }

    /**
     * Starts the APIEntryJobService to completely replace the invitations for a location
     *
     * @param locationId ID of the location that you want emergency number for
     */
    public static void checkEmergencyNumbersForLocations(int locationId) {
        Intent intent = new Intent(context, GooglePlacesAPIIntentServiceEmergencyNumbers.class);
        intent.setAction(ACTION_EMERGENCY_NUMBERES);
        intent.putExtra(EXTRA_LOCATION, locationId);
        context.startService(intent);
    }

    public static void getNewEmergencyNumbersForLocations(int locationId, boolean save) {
        Intent intent = new Intent(context, GooglePlacesAPIIntentServiceEmergencyNumbers.class);
        intent.setAction(ACTION_NEW_EMERGENCY_NUMBERES);
        intent.putExtra(EXTRA_LOCATION, locationId);
        intent.putExtra(EXTRA_SAVE, save);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null)
            return;
        int locationId = intent.getIntExtra(EXTRA_LOCATION, -1);
        Location location = LocationDatabaseService.getLocationFromId(locationId);

        if (location == null) {
            return;
        }

        if (intent.getAction()
                .equalsIgnoreCase(ACTION_EMERGENCY_NUMBERES)) {

            getEmergencyNumbers(location);
        } else if (intent.getAction().equalsIgnoreCase(ACTION_NEW_EMERGENCY_NUMBERES)) {
            boolean save = intent.getBooleanExtra(EXTRA_SAVE, true);
            getNewEmergencyNumbers(location, save);
        }
    }

    private void getNewEmergencyNumbers(Location location, boolean save) {

        String policeNumber = getPhoneNumberForType(location, POLICE);
        String fireNumber = getPhoneNumberForType(location, FIRE);
        String emsNumber = getPhoneNumberForType(location, HOSPITAL);

        if (!save) {
            TinyMessageBus.post(new ReplaceEmergencyNumbers(policeNumber, fireNumber, emsNumber));
            return;
        }

        try {
            EmergencyContctsAPIService.getEmergencyContactsForLocation(location.id);
        } catch (RetrofitError error) {
            return;
        }

        Map<EmergencyContact.ContactType, EmergencyContact> emergencyContacts = EmergencyContactDatabaseService.getEmergencyContacts(location.id);

        if (emergencyContacts.containsKey(EmergencyContact.ContactType.ems)) {
            EmergencyContact contact = emergencyContacts.get(EmergencyContact.ContactType.ems);
            contact.phoneNumber = emsNumber;
            contact.provider = "google";
            try {
                EmergencyContctsAPIService.changeEmergencyContact(contact);

            } catch (RetrofitError ignored) {
            }
        }

        if (emergencyContacts.containsKey(EmergencyContact.ContactType.police)) {
            EmergencyContact contact = emergencyContacts.get(EmergencyContact.ContactType.police);
            contact.phoneNumber = policeNumber;
            contact.provider = "google";
            try {
                EmergencyContctsAPIService.changeEmergencyContact(contact);

            } catch (RetrofitError ignored) {
            }
        }


        if (emergencyContacts.containsKey(EmergencyContact.ContactType.fire)) {
            EmergencyContact contact = emergencyContacts.get(EmergencyContact.ContactType.fire);
            contact.phoneNumber = fireNumber;
            contact.provider = "google";
            try {
                EmergencyContctsAPIService.changeEmergencyContact(contact);

            } catch (RetrofitError ignored) {
            }
        }

        TinyMessageBus.post(new ForceUpdateEmergencyNumbers());

    }

    /**
     * Get the closest phone number for a particular type of business
     *
     * @param location   the location to get emergency numbers for
     * @param numberType the type business that you want to search for, must be a business that is compatible with the Google Places API
     * @return The phone number of the business
     */
    private String getPhoneNumberForType(Location location, EmergencyNumbers numberType) {
        GooglePlacesResponse googlePlacesResponse;

        if (location.isFinland()) {
            return "112";
        }

        if (location.isFrance()) {

            switch (numberType) {
                case POLICE:
                    return "17";
                case FIRE:
                    return "18";
                case HOSPITAL:
                    return "112";
            }
        }

        if (location.isUK()) {
            return "999";
        }

        if (location.isCanada()) {
            return "911";
        }

        if (location.isNetherlands()) {
            return "112";
        }

        if (location.isLuxemburg()) {
            switch (numberType) {
                case POLICE:
                    return "113";
                case FIRE:
                    return "112";
                case HOSPITAL:
                    return "112";
            }
        }

        if (location.isBelgium()) {

            switch (numberType) {
                case POLICE:
                    return "101";
                case FIRE:
                case HOSPITAL:
                    return "100";
            }
        }


        if (location.isAustralia()) {
            return "000";
        }
        if (location.isDenmark()) {
            return "112";
        }

        if (location.isSweden()) {
            return "112";

        }


        if (location.isNorway()) {
            switch (numberType) {
                case POLICE:
                    return "112";
                case FIRE:
                    return "110";
                case HOSPITAL:
                    return "113";
            }
        }
        String type;
        String name;
        switch (numberType) {
            case POLICE:
                type = "police";
                name = "police";
                break;
            case FIRE:
                type = "fire_station";
                name = "fire";
                break;
            case HOSPITAL:
                type = "police";
                name = "police";
                break;
            default:
                return "";
        }

        double latitude = location.lat;
        double longitude = location.lng;
        try {
            googlePlacesResponse = GooglePlacesAPIService.searchForPlaces(latitude, longitude, type, name);
        } catch (RetrofitError error) {
            return null;
        }

        if (googlePlacesResponse.status.equalsIgnoreCase("OK")) {
            for (GooglePlace googlePlace : googlePlacesResponse.results) {

                GooglePlaceDetailResponse googlePlaceDetailResponse;
                try {
                    googlePlaceDetailResponse = GooglePlacesAPIService.getPlaceDetail(googlePlace.placeId);
                } catch (RetrofitError error) {
                    return "";
                }
                if (googlePlaceDetailResponse.status.equalsIgnoreCase("OK")) {
                    GooglePlaceDetails googlePlaceDetails = googlePlaceDetailResponse.result;
                    String formattedPhoneNumber = googlePlaceDetails.internationalPhoneNumber;
                    if (formattedPhoneNumber != null) {
                        return formattedPhoneNumber;
                    }
                }
            }
        }

        return "";
    }

    private void getEmergencyNumbers(Location location) {
        try {
            EmergencyContctsAPIService.getEmergencyContactsForLocation(location.id);

        } catch (RetrofitError ignored) {
        }
    }


}
