package is.yranac.canary.services.api;

import is.yranac.canary.Constants;
import is.yranac.canary.model.invitation.Invitation;
import is.yranac.canary.model.invitation.InvitationCreate;
import is.yranac.canary.model.invitation.InvitationResponse;
import is.yranac.canary.retrofit.RetroFitAdapterFactory;
import is.yranac.canary.services.database.InvitationDatabaseService;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Schroeder on 8/10/14.
 */
public class InvitationsAPIService {

    public static void createInvitation(Invitation invitation, Callback<Void> callback) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        InvitationCreate invitationCreate = new InvitationCreate(invitation);
        InvitationService invitationService = restAdapter.create(InvitationService.class);
        invitationService.createInvitation(invitationCreate, callback);
    }

    public static InvitationResponse getInvitationsForLocation(int locationId) throws RetrofitError {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        InvitationService invitationService = restAdapter.create(InvitationService.class);

        InvitationResponse invitationResponse = invitationService.getInvitationsForLocation("pending", locationId);
        InvitationDatabaseService.deleteInvitationsAtLocation(locationId);

        for (Invitation invitation : invitationResponse.invitations) {
            InvitationDatabaseService.insertInvitation(invitation);
        }
        return invitationResponse;
    }

    public static void deleteInvitation(int invitationId, Callback<Void> callback) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        InvitationService invitationService = restAdapter.create(InvitationService.class);
        invitationService.deleteInvitations(invitationId, callback);

    }

    public interface InvitationService {
        @POST(Constants.INVITATION_URI)
        void createInvitation(
                @Body InvitationCreate invitationCreate,
                Callback<Void> callback
        );

        @GET(Constants.INVITATION_URI)
        InvitationResponse getInvitationsForLocation(
                @Query("status") String status,
                @Query("location") int locationUri);


        @DELETE(Constants.INVITATION_URI + "{id}/")
        void deleteInvitations(
                @Path("id") int id,
                Callback<Void> callback
        );
    }
}
