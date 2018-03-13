package is.yranac.canary.adapter;


import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import is.yranac.canary.R;
import is.yranac.canary.contentproviders.CanarySubscriptionContentProvider;
import is.yranac.canary.databinding.ListrowLocationSelectBinding;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.subscription.Subscription;
import is.yranac.canary.services.database.SubscriptionPlanDatabaseService;
import is.yranac.canary.util.CustomAsyncHandler;

/**
 * Created by michaelschroeder on 5/10/17.
 */

public class SelectLocationAdapter extends ArrayAdapter<Location> {

    public SelectLocationAdapter(Context context, List<Location> locations) {
        super(context, 0, locations);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        CustomViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            ListrowLocationSelectBinding binding = ListrowLocationSelectBinding.inflate(inflater);
            holder = new CustomViewHolder(getContext(), binding);
            binding.getRoot().setTag(holder);
            convertView = binding.getRoot();
        } else {
            holder = (CustomViewHolder) convertView.getTag();
        }

        holder.setLocation(getItem(position));

        return convertView;
    }

    private class CustomViewHolder {
        protected final ListrowLocationSelectBinding binding;
        protected final Context context;

        public CustomViewHolder(Context context, ListrowLocationSelectBinding binding) {
            this.binding = binding;
            this.context = context;
        }

        public void setLocation(Location location) {
            if (location == null)
                return;

            binding.locationName.setText(location.name);
            binding.locationAddress.setText(location.address);
            if (customAsyncHandler != null) {
                customAsyncHandler.cancelOperation(0);
            } else {
                customAsyncHandler = new CustomAsyncHandler(context.getContentResolver(), asyncQueryListener);
            }

            String where = CanarySubscriptionContentProvider.COLUMN_LOCATION_ID + " == ?";
            String[] whereArgs = new String[]{String.valueOf(location.id)};


            customAsyncHandler.startQuery(0, null, CanarySubscriptionContentProvider.CONTENT_URI,
                    null, where, whereArgs, null);

        }

        private CustomAsyncHandler.AsyncQueryListener asyncQueryListener = new CustomAsyncHandler.AsyncQueryListener() {
            @Override
            public void onQueryComplete(int token, Object cookie, Cursor cursor) {

                Subscription subscription = null;

                if (cursor != null && cursor.moveToFirst()) {
                    subscription = SubscriptionPlanDatabaseService.createServicePlanFromCursor(cursor);
                }

                if (subscription != null) {
                    if (subscription.hasMembership) {

                        binding.membershipStatus.setText(membershipActive());
                        binding.membershipStatus.setTextColor(blueColor());

                    } else if (subscription.onTrial) {

                        String daysRemaining = context.getString(R.string.days_left_membership_preview,
                                subscription.remainingDays());
                        binding.membershipStatus.setText(daysRemaining);
                        binding.membershipStatus.setTextColor(blueColor());
                    } else {
                        binding.membershipStatus.setText(membershipInactive());
                        binding.membershipStatus.setTextColor(grayColor());
                    }
                }
                cursor.close();

            }
        };

        private String membershipInactive;

        private String membershipInactive() {
            if (membershipInactive == null) {
                membershipInactive = getContext().getString(R.string.membership_inactive);
            }
            return membershipInactive;
        }

        private String membershipActive;

        private String membershipActive() {
            if (membershipActive == null) {
                membershipActive = getContext().getString(R.string.membership_active);
            }
            return membershipActive;
        }

        private int grayColor = 0;

        private int grayColor() {
            if (grayColor == 0) {
                grayColor = ContextCompat.getColor(getContext(), R.color.dark_gray);
            }
            return grayColor;
        }

        private int blueColor = 0;

        private int blueColor() {
            if (blueColor == 0) {
                blueColor = ContextCompat.getColor(getContext(), R.color.bright_sky_blue_two);
            }
            return blueColor;
        }

        private CustomAsyncHandler customAsyncHandler;

    }
}
