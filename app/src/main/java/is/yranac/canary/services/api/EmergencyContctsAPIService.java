package is.yranac.canary.services.api;

import is.yranac.canary.Constants;
import is.yranac.canary.model.emergencycontacts.EmergencyContact;
import is.yranac.canary.model.emergencycontacts.EmergencyContactPatch;
import is.yranac.canary.model.emergencycontacts.EmergencyContactsResponse;
import is.yranac.canary.retrofit.RetroFitAdapterFactory;
import is.yranac.canary.services.database.EmergencyContactDatabaseService;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PATCH;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Schroeder on 10/2/15.
 */
public class EmergencyContctsAPIService {

    public static EmergencyContactsResponse getEmergencyContactsForLocation(int locationId) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        EmergencyContactsService emergencyContactsService = restAdapter.create(EmergencyContactsService.class);
        EmergencyContactsResponse emergencyContactsResponse = emergencyContactsService.getEmergencyContactsForLocation(locationId);
        EmergencyContactDatabaseService.insertEmergencyContacts(emergencyContactsResponse.emergencyContactList);
        return emergencyContactsResponse;
    }


    public static Void changeEmergencyContact(EmergencyContact emergencyContact) throws RetrofitError {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        EmergencyContactsService emergencyContactsService = restAdapter.create(EmergencyContactsService.class);
        Void aVoid = emergencyContactsService.changeEmergencyContact(emergencyContact.id, new EmergencyContactPatch(emergencyContact));
        EmergencyContactDatabaseService.insertEmergencyContact(emergencyContact);
        return aVoid;
    }


    public static void changeEmergencyContactAsync(EmergencyContact emergencyContact) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        EmergencyContactsService emergencyContactsService = restAdapter.create(EmergencyContactsService.class);
        emergencyContactsService.changeEmergencyContact(emergencyContact.id, new EmergencyContactPatch(emergencyContact), new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        EmergencyContactDatabaseService.insertEmergencyContact(emergencyContact);

    }

    public interface EmergencyContactsService {

        @GET(Constants.EMERGENCY_CONTACT_URI)
        EmergencyContactsResponse getEmergencyContactsForLocation(
                @Query("location") int id
        );

        @PATCH(Constants.EMERGENCY_CONTACT_URI + "{id}/")
        Void changeEmergencyContact(
                @Path("id") int id,
                @Body EmergencyContactPatch emergencyContact);

        @PATCH(Constants.EMERGENCY_CONTACT_URI + "{id}/")
        void changeEmergencyContact(
                @Path("id") int id,
                @Body EmergencyContactPatch emergencyContact,
                Callback<Void> createEmergencyContactCallback);
    }
}
