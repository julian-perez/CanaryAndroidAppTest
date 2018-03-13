package is.yranac.canary.adapter.holder;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Locale;

import is.yranac.canary.R;
import is.yranac.canary.databinding.ListrowEntrySimpleBinding;
import is.yranac.canary.model.avatar.Avatar;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.entry.Entry;
import is.yranac.canary.services.database.AvatarDatabaseService;
import is.yranac.canary.services.database.CustomerDatabaseService;
import is.yranac.canary.ui.BaseActivity;
import is.yranac.canary.util.StringUtils;

import static is.yranac.canary.model.entry.Entry.ENTRY_TYPE_BACKPACK;
import static is.yranac.canary.model.entry.Entry.ENTRY_TYPE_CONNECT;
import static is.yranac.canary.model.entry.Entry.ENTRY_TYPE_DISCONNECT;
import static is.yranac.canary.model.entry.Entry.ENTRY_TYPE_LOCATION;
import static is.yranac.canary.model.entry.Entry.ENTRY_TYPE_MODE;
import static is.yranac.canary.model.entry.Entry.ENTRY_TYPE_OTA;
import static is.yranac.canary.model.entry.Entry.ENTRY_TYPE_SIREN;

/**
 * Created by Schroeder on 3/16/16.
 */
public class EntryViewHolderSimple extends EntryViewHolder {
    private static final String LOG_TAG = "EntryViewHolderSimple";


    private GetCustomerInfoAsyncTask getCustomerInfoAsyncTask;
    private ListrowEntrySimpleBinding binding;

    public EntryViewHolderSimple(View view, BaseActivity context) {
        super(view, context);
        binding = ListrowEntrySimpleBinding.bind(view);
    }

    @Override
    public void setUpEntry(Entry entry) {

        int listRowIcon = -1;
        binding.setEntry(entry);
        binding.customerCircleContainer.setVisibility(View.VISIBLE);
        switch (entry.entryType) {
            case ENTRY_TYPE_LOCATION:
                if (entry.customers != null
                        && entry.customers.size() != 0) {
                    String customerUri = entry.customers.get(0);
                    loadCustomer(customerUri);
                } else {
                    listRowIcon = R.drawable.ic_blankprofile;
                }
                break;
            case ENTRY_TYPE_MODE:
                binding.listrowIcon.setVisibility(View.VISIBLE);
                binding.customerCircleContainer.setVisibility(View.VISIBLE);
                if (entry.description.toLowerCase(Locale.getDefault())
                        .contains(context.getString(R.string.privacy).toLowerCase())
                        && !entry.displayMeta.locationIsPrivate) {
                    binding.customerCircleContainer.setVisibility(View.GONE);
                } else if (entry.displayMeta.locationIsPrivate) {
                    listRowIcon = R.drawable.mode_privacy_on;
                } else if (entry.displayMeta.locationMode != null) {
                    switch (entry.displayMeta.locationMode) {
                        case "home":
                            listRowIcon = R.drawable.mode_home_on;
                            break;
                        case "away":
                            listRowIcon = R.drawable.mode_away_on;
                            break;
                        case "night":
                            listRowIcon = R.drawable.mode_night_on;
                            break;
                        default:
                            binding.customerCircleContainer.setVisibility(View.GONE);
                            break;
                    }
                } else {
                    binding.customerCircleContainer.setVisibility(View.GONE);
                }
                break;
            case ENTRY_TYPE_DISCONNECT:
                listRowIcon = R.drawable.timeline_icn_disconnected;
                break;
            case ENTRY_TYPE_CONNECT:
                listRowIcon = R.drawable.timeline_icn_online;
                break;
            case ENTRY_TYPE_OTA:
            case ENTRY_TYPE_BACKPACK:
                listRowIcon = R.drawable.timeline_icn_updated;
                break;
            case ENTRY_TYPE_SIREN:
                listRowIcon = R.drawable.ic_siren_timeline;
                break;
            default:
                binding.customerCircleContainer.setVisibility(View.GONE);
                return;
        }

        if (listRowIcon != -1) {
            binding.listrowIcon.setImageResource(listRowIcon);
        }

    }

    public void loadCustomer(String customerUrl) {
        if (getCustomerInfoAsyncTask != null && !getCustomerInfoAsyncTask.isCancelled()) {
            getCustomerInfoAsyncTask.cancel(true);
        }
        getCustomerInfoAsyncTask = new GetCustomerInfoAsyncTask(customerUrl);
        getCustomerInfoAsyncTask.execute();
    }


    private void setCustomer(Customer customer) {
        if (customer == null)
            return;

        binding.customerInitials.setText(customer.getInitials());
        loadCustomerImage(customer.avatar);
    }

    private void loadCustomerImage(Avatar customerAvatar) {
        if (customerAvatar == null || StringUtils.isNullOrEmpty(customerAvatar.thumbnailUrl)) {
            binding.listrowIcon.setImageResource(R.drawable.ic_blankprofile);
            return;
        }

        binding.listrowIcon.setImageBitmap(null);
        binding.listrowIcon.setVisibility(View.GONE);

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(
                customerAvatar.thumbnailUrl, binding.listrowIcon, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        binding.listrowIcon.setVisibility(View.VISIBLE);
                    }
                });
    }

    private class GetCustomerInfoAsyncTask extends AsyncTask<Void, Void, Customer> {
        String customerURI;

        public GetCustomerInfoAsyncTask(String customerURI) {
            this.customerURI = customerURI;
        }

        @Override
        protected Customer doInBackground(Void... params) {
            Customer customer = CustomerDatabaseService.getCustomerFromUri(customerURI);
            if (customer == null)
                return null;

            customer.avatar = AvatarDatabaseService.getAvatarFromCustomerId(customer.id);

            return customer;
        }

        @Override
        protected void onPostExecute(Customer customer) {
            setCustomer(customer);
        }
    }

}

