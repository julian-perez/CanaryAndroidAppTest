package is.yranac.canary.services.api;

import android.content.Context;

import is.yranac.canary.Constants;
import is.yranac.canary.util.cache.location.UpdateMembershipPresenceCache;
import is.yranac.canary.model.membership.ChangeMembershipPresence;
import is.yranac.canary.model.membership.Membership;
import is.yranac.canary.model.membership.MembershipPresenceResponse;
import is.yranac.canary.model.membership.MembershipResponse;
import is.yranac.canary.retrofit.RetroFitAdapterFactory;
import is.yranac.canary.services.database.MembershipDatabaseService;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.PATCH;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Schroeder on 8/8/14.
 */
public class MembershipAPIService {

    public static MembershipPresenceResponse getLocationMemberships(Context context, int locationId) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true, true, true);
        MembershipService membershipService = restAdapter.create(MembershipService.class);

        MembershipPresenceResponse response = membershipService.getMembership(locationId);
        MembershipDatabaseService.insertMemberships(context, locationId, response.membershipPresences);
        return response;
    }

    public static void getMembership(int locationId, int customerId,
                                     Callback<MembershipResponse> membershipGetCallback) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        MembershipService contactService = restAdapter.create(MembershipService.class);

        contactService.getMembership(locationId, customerId, membershipGetCallback);
    }

    public static void updateMembershipPresence(final Context context, final int locationId, final int customerId, final boolean presence) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true, false, true);
        MembershipService contactService = restAdapter.create(MembershipService.class);

        contactService.updateMemberPresence(locationId, customerId, new ChangeMembershipPresence(presence), new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                TinyMessageBus.post(new UpdateMembershipPresenceCache(context, locationId, customerId, presence));

            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
    }


    public static void deleteMembership(int membershipId, Callback<Void> membershipDeleteCallback) {

        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        MembershipService contactService = restAdapter.create(MembershipService.class);

        contactService.deleteMembership(membershipId, membershipDeleteCallback);
    }

    public static void deleteMembership(
            final int locationId, final int customerId, final Callback<Void> membershipDeleteCallback) {
        getMembership(
                locationId, customerId, new Callback<MembershipResponse>() {
                    @Override
                    public void success(MembershipResponse membershipResponse, Response response) {
                        if (response.getStatus() == 200 && membershipResponse.memberships.size() > 0) {
                            final Membership membership = membershipResponse.memberships.get(0);
                            String resourceUri = membership.resourceUri;
                            int membershipId = Utils.getIntFromResourceUri(resourceUri);

                            deleteMembership(
                                    membershipId, new Callback<Void>() {
                                        @Override
                                        public void success(Void aVoid, Response response) {
                                            membershipDeleteCallback.success(aVoid, response);
                                        }

                                        @Override
                                        public void failure(RetrofitError error) {
                                            membershipDeleteCallback.failure(error);
                                        }
                                    });

                        } else {
                            // no members?
                            membershipDeleteCallback.failure(null);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        membershipDeleteCallback.failure(error);
                    }
                });
    }

    public interface MembershipService {

        @GET(Constants.LOCATIONS_URI + "{id}/members/")
        MembershipPresenceResponse getMembership(
                @Path("id") int locationId);

        @GET(Constants.MEMBERSHIP_URI)
        void getMembership(
                @Query("location") int locationId,
                @Query("customer") int customerId,
                Callback<MembershipResponse> membershipGetCallback);

        @DELETE(Constants.MEMBERSHIP_URI + "{id}/")
        void deleteMembership(
                @Path("id") int id,
                Callback<Void> membershipDeleteCallback
        );

        @PATCH(Constants.LOCATIONS_URI + "{id}/members/{customer_id}/")
        void updateMemberPresence(
                @Path("id") int locationId,
                @Path("customer_id") int customerId,
                @Body ChangeMembershipPresence changeMembershipPresence,
                Callback<Void> membershipGetCallback);
    }
}
